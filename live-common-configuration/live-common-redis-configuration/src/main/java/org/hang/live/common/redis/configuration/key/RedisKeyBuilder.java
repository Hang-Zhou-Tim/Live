package org.hang.live.common.redis.configuration.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author hang
 * @Date: Created in 20:19 2024/8/14
 * @Description
 */
public class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}