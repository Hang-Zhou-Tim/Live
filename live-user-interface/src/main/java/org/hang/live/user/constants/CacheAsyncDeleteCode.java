package org.hang.live.user.constants;

/**
 * @Author idea
 * @Date: Created in 16:16 2023/5/28
 * @Description
 */
public enum CacheAsyncDeleteCode {

    USER_INFO_DELETE(0, "User Info Deletion"),
    USER_TAG_DELETE(1, "User Tag Deletion");

    int code;
    String desc;

    CacheAsyncDeleteCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
