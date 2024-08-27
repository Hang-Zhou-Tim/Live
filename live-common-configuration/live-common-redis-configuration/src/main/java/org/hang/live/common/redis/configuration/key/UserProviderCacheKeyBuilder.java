package org.hang.live.common.redis.configuration.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static String USER_INFO_KEY = "userInfo";
    private static String USER_TAG_KEY = "userTag";
    private static String USER_TAG_LOCK_KEY = "userTagLock";
    private static String USER_PHONE_LIST_KEY = "userPhoneList";
    private static String USER_PHONE_OBJ_KEY = "userPhoneObj";
    private static String USER_LOGIN_TOKEN_KEY = "userLoginToken";

    private static String ACCOUNT_TOKEN_KEY = "account";
    private static String SMS_LOGIN_CODE_KEY = "smsLoginCode";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }

    public String buildTagLockKey(Long userId) {
        return super.getPrefix() + USER_TAG_LOCK_KEY + super.getSplitItem() + userId;
    }

    public String buildTagKey(Long userId) {
        return super.getPrefix() + USER_TAG_KEY + super.getSplitItem() + userId;
    }

    public String buildUserPhoneListKey(Long userId) {
        return super.getPrefix() + USER_PHONE_LIST_KEY + super.getSplitItem() + userId;
    }

    public String buildUserPhoneObjKey(String phone) {
        return super.getPrefix() + USER_PHONE_OBJ_KEY + super.getSplitItem() + phone;
    }

    public String buildUserLoginTokenKey(String tokenKey) {
        return super.getPrefix() + USER_LOGIN_TOKEN_KEY + super.getSplitItem() + tokenKey;
    }

    public String buildSmsLoginCodeKey(String phone) {
        return super.getPrefix() + SMS_LOGIN_CODE_KEY + super.getSplitItem() + phone;
    }
}
