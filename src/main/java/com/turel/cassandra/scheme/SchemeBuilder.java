package com.turel.cassandra.scheme;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chaimturkel on 1/1/17.
 */
@Component
public class SchemeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(SchemeBuilder.class);

    @Autowired SchemeConfig schemeConfig;

    @Value("${scheme.dir}")
    private String schemeDir;

    MessageDigest messageDigest;

    @Autowired Session session;
    @Autowired Cluster cluster;

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private Try<String> hashOfFile(File file)  {
        return Try.of(() -> {
            byte[] buffer = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(buffer);
            }
            messageDigest.update(buffer);
            byte[] digest = messageDigest.digest();
            return bytesToHex(digest);
        });
    }

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException {
        logger.info("************************* starting ************************************");
        messageDigest = MessageDigest.getInstance("MD5");
        File file = new File(schemeDir);
        if (!file.exists()) {
            logger.error("directory not found " + schemeDir);
        } else {
            Files.list(file.toPath())
                    .filter(cqlScript -> cqlScript.getFileName().toString().endsWith(".cql"))
                    .forEach(path -> {
                final String cqlScript = path.getFileName().toString();
                final SchemeData data = schemeConfig.getData(cqlScript);
                hashOfFile(path.toFile())
                        .onSuccess(hash -> {
                            if (data == null) {
                                logger.info("running script " + cqlScript);
                                try {
                                    schemeConfig.runScript(path.toString(), hash);
                                    schemeConfig.addSchemeRow(cqlScript, hash);
                                } catch (IOException e) {
                                    logger.error(String.format("file %s. Error %s",cqlScript,e.getMessage()));
                                }
                            }
                            else{
                                logger.info("already ran script " + cqlScript);
                                if (!data.getHash().equals(hash)){
                                    logger.error(String.format("expected %s but found %s for %s",data.getHash(),hash,cqlScript));
                                }
                            }

                })
                        .onFailure(Throwable::printStackTrace);
            });
        }
        logger.info("************************* finished ************************************");
        //allow spring to shutdown
        session.close();
        cluster.close();
    }
}
