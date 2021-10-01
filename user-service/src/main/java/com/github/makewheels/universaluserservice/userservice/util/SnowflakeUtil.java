package com.github.makewheels.universaluserservice.userservice.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowflakeUtil {
    private static final Snowflake snowflake;

    static {
        int workerId = 1;
        int datacenterId = 1;
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public static long getId() {
        return snowflake.nextId();
    }
}
