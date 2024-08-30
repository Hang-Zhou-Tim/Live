package org.hang.live.user.payment.constants;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum PayChannelEnum {

    ALIPAY(0,"Alipay"),
    WECHAT_PAY(1,"Wechat Pay"),
    UNION_PAY(2,"Union Pay"),
    CONSOLE(3,"Console Pay");

    PayChannelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
