package com.turel.cassandra.scheme;

import java.util.Date;

/**
 * Created by chaimturkel on 1/1/17.
 */
public class SchemeData {
    String script_name;
    String hash;
    Date run_time;

    public SchemeData(String script_name, String hash, Date run_time) {
        this.script_name = script_name;
        this.hash = hash;
        this.run_time = run_time;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getScript_name() {
        return script_name;
    }

    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

    public Date getRun_time() {
        return run_time;
    }

    public void setRun_time(Date run_time) {
        this.run_time = run_time;
    }
}
