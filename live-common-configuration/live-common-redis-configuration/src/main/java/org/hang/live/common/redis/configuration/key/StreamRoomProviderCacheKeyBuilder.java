package org.hang.live.common.redis.configuration.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/**
 * @Author hang
 * @Date: Created in 10:23 2024/8/14
 * @Description
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class StreamRoomProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static String STREAM_ROOM_LIST = "live_stream_room_list";
    private static String STREAM_ROOM_OBJ = "live_stream_room_obj";
    private static String REFRESH_STREAM_ROOM_LIST_LOCK = "refresh_live_stream_room_list_lock";
    private static String STREAM_ROOM_USER_SET = "live_stream_room_user_set";
    private static String STREAM_ROOM_ONLINE_PK = "live_stream_online_pk";

    public String buildLiveStreamRoomOnlinePk(Integer roomId) {
        return super.getPrefix() + STREAM_ROOM_ONLINE_PK + super.getSplitItem() + roomId;
    }

    public String buildLiveStreamRoomUserSet(Integer roomId, Integer appId) {
        return super.getPrefix() + STREAM_ROOM_USER_SET + super.getSplitItem() + appId + super.getSplitItem() + roomId;
    }

    public String buildRefreshLiveStreamRoomListLock() {
        return super.getPrefix() + REFRESH_STREAM_ROOM_LIST_LOCK;
    }

    public String buildLiveStreamRoomObj(Integer roomId) {
        return super.getPrefix() + STREAM_ROOM_OBJ + super.getSplitItem() + roomId;
    }

    public String buildLiveStreamRoomList(Integer type) {
        return super.getPrefix() + STREAM_ROOM_LIST + super.getSplitItem() + type;
    }
}
