package com.turel.cassandra.utils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by chaimturkel on 1/1/17.
 */
public class UUIDToDate {

    // This method comes from Hector's TimeUUIDUtils class:
    // https://github.com/rantav/hector/blob/master/core/src/main/java/me/prettyprint/cassandra/utils/TimeUUIDUtils.java
    static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
    public static long getTimeFromUUID(UUID uuid) {
        return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
    }

    public static Date uuidToDate(String uuidString) {
        return uuidToDate(UUID.fromString(uuidString));
    }

    public static Date uuidToDate(UUID uuid) {
        long time = getTimeFromUUID(uuid);
        return new Date(time);
    }

}
