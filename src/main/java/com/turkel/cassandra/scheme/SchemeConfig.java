package com.turkel.cassandra.scheme;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.turkel.cassandra.utils.UUIDToDate;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by chaimturkel on 1/1/17.
 */
@Component
public class SchemeConfig {
    private static final Logger logger = LoggerFactory.getLogger(SchemeConfig.class);

    @Value("${cassandra.version.table}")
    private String versionTable;

    @Autowired Session session;
    @Autowired Cluster cluster;


    @PostConstruct
    public void validateSchemeTableExists() {
        final String schemeTable = String.format("CREATE TABLE IF NOT EXISTS %s (script_name varchar, hash varchar, run_time timeuuid, PRIMARY KEY(script_name))", versionTable);
        session.execute(schemeTable);
    }


    public void runScript(String fileName, String hash) throws IOException {
        InputStream is = new FileInputStream(fileName);
        byte[] buffer = new byte[is.available()];
        IOUtils.readFully(is, buffer);
        List<String> commands = Arrays.asList(new String(buffer, "UTF-8").split(";"));
        commands.forEach(script -> {
            session.execute(script);
        });
    }

    public void addSchemeRow(String fileName, String hash) {
        final String schemeInsert = String.format("INSERT INTO %s (script_name, hash, run_time) VALUES ('%s', '%s', now())", versionTable, fileName, hash);
        session.execute(schemeInsert);
    }

    public SchemeData getData(String script_name) {
        Statement statement = QueryBuilder
                .select()
                .from(versionTable)
                .where(QueryBuilder.eq("script_name", script_name));
        ResultSet results = session.execute(statement);
        final Row row = results.one();
        if (row != null) {
            final String hash = row.get("hash", TypeCodec.varchar());
            final UUID uuid = row.get("run_time", TypeCodec.timeUUID());
            final Date run_time = UUIDToDate.uuidToDate(uuid);
            return new SchemeData(script_name, hash, run_time);
        }
        ;
        return null;
    }

    public Map<String, SchemeData> loadData() {
        Statement statement = QueryBuilder
                .select()
                .all()
                .from(versionTable);

        ResultSet results = session.execute(statement);
        final Map<String, SchemeData> filesExecuted = new HashMap<>();
        results.forEach(row -> {
            final String script_name = row.get("script_name", TypeCodec.varchar());
            final String hash = row.get("hash", TypeCodec.varchar());
            final UUID uuid = row.get("run_time", TypeCodec.timeUUID());
            final Date run_time = UUIDToDate.uuidToDate(uuid);
            filesExecuted.put(script_name, new SchemeData(script_name, hash, run_time));
        });

        return filesExecuted;
    }

}
