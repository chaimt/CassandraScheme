package com.turel.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Created by chaimturkel on 1/1/17.
 */
@Configuration
public class CassandraConfig {
    private static final Logger logger = LoggerFactory.getLogger(CassandraConfig.class);

    @Value("${cassandra.contactpoints}")
    private String contactpoints;

    @Value("${cassandra.port}")
    private Integer port;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("${cassandra.user}")
    private String user;

    @Value("${cassandra.password}")
    private String password;


    @Bean Cluster cluster() {
        logger.info(String.format("Cassandra: %s, %s", contactpoints, port));
        final Cluster.Builder builder = Cluster.builder().withPort(port);
        for (String host : contactpoints.split(",")){
            builder.addContactPoint(host);
        }
        if (!StringUtils.isEmpty(user))
            builder.withCredentials(user,password);
        return builder.build();
    }

    private void createKeyspaceIfMissing(){
//        final Session connectSystem = cluster().connect();
//        final ResultSet execute = connectSystem.execute("use system; describe keyspaces;");
    }

    @Bean Session session() {
        return cluster().connect(keyspace);
    }

}
