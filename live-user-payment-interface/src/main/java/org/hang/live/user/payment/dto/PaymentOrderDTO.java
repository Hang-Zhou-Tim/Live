package org.hang.live.user.payment.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class PaymentOrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9209044050847451420L;

    private Long id;
    private String orderId;
    private Integer currencyId;
    private Integer bizCode;
    private Long userId;
    private Integer source;
    private Integer payChannel;
    private Integer status;
    private Date payTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getBizCode() {
        return bizCode;
    }

    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return "PaymentOrderDTO{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", currencyId=" + currencyId +
                ", userId=" + userId +
                ", bizCode=" + bizCode +
                ", source=" + source +
                ", payChannel=" + payChannel +
                ", status=" + status +
                ", payTime=" + payTime +
                '}';
    }
}
