package org.hang.live.user.payment.constants;

/**
 * Order status (0 prepare order, 1 paying order, 2 payed order, 3 canceled order, 4 invalid order)
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public enum OrderStatusEnum {

    WAITING_PAY(0,"prepare"),
    PAYING(1,"paying"),
    PAYED(2,"payed"),
    PAY_BACK(3,"cancelled"),
    IN_VALID(4,"invalid");

    OrderStatusEnum(Integer code, String msg) {
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
