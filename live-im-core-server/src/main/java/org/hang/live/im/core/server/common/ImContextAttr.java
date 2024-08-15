package org.hang.live.im.core.server.common;

import io.netty.util.AttributeKey;

/**
 * @Author hang
 * @Date: Created in 21:40 2024/8/11
 * @Description
 */
public class ImContextAttr {

    /**
     * Generate keys for user id in constant pool.
     */
    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

    /**
     * Generate keys for app id in constant pool.
     */
    public static AttributeKey<Integer> APP_ID = AttributeKey.valueOf("appId");

    /**
     * Generate keys for live stream room id in constant pool.
     */
    public static AttributeKey<Integer> ROOM_ID = AttributeKey.valueOf("roomId");
}