package org.hang.live.common.redis.configuration.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hang
 * @Date: Created in 10:23 2024/8/20
 * @Description
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class BankProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static String BALANCE_CACHE = "balance_cache";

    private static String PAY_PRODUCT_CACHE = "pay_product_cache";

    private static String PAY_PRODUCT_ITEM_CACHE = "pay_product_item_cache";

    public String buildPayProductItemCache(Integer productId) {
        return super.getPrefix() + PAY_PRODUCT_ITEM_CACHE + super.getSplitItem() + productId;
    }

    /**
     * Based on payment type to build product key
     *
     * @param type
     * @return
     */
    public String buildPayProductCache(Integer type) {
        return super.getPrefix() + PAY_PRODUCT_CACHE + super.getSplitItem() + type;
    }

    /**
     * cache key for user balance
     *
     * @param userId
     * @return
     */
    public String buildUserBalance(Long userId) {
        return super.getPrefix() + BALANCE_CACHE + super.getSplitItem() + userId;
    }

}
