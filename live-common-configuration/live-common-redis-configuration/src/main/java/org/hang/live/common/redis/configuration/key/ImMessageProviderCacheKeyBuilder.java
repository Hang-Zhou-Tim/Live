package org.hang.live.common.redis.configuration.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hang
 * @Date: Created in 10:23 2024/8/14
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class ImMessageProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static String SMS_LOGIN_CODE_KEY = "smsLoginCode";

    public String buildSmsLoginCodeKey(String phone) {
        return super.getPrefix() + SMS_LOGIN_CODE_KEY + super.getSplitItem() + phone;
    }

}
