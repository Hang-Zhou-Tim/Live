package org.hang.live.common.interfaces.enums;

/**
 * Record the header name that is passed from gateway to the users.
 *
 * @Author hang
 * @Date: Created in 08:44 2024/8/11
 * @Description
 */
public enum GatewayHeaderEnum {

    USER_LOGIN_ID("User ID","hang_gh_user_id");

    String desc;
    String name;

    GatewayHeaderEnum(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
