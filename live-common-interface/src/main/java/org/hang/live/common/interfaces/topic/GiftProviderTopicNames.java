package org.hang.live.common.interfaces.topic;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class GiftProviderTopicNames {

    /**
     * cache key name used to remove gift.
     */
    public static final String REMOVE_GIFT_CACHE = "remove_gift_cache";

    /**
     * cache key name used to send gift.
     */
    public static final String SEND_GIFT = "send_gift";

    //cache key name used to receive red packet.
    public static final String SNATCH_RED_PACKET = "snatch-red-packet";

    /**
     * cache key name used to roll back stock.
     */
    public static final String ROLL_BACK_STOCK = "rollback-stock";
    /**
     * cache key name used to prepare stock.
     */
    public static final String PREPARE_STOCK = "prepare-stock";
    public static final String SEND_PK_GIFT = "send_pk_gift";
}
