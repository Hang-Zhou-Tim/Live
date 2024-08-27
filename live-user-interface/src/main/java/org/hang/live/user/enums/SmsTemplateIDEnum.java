package org.hang.live.user.enums;

/**
 * @Author hang
 * @Date: Created in 09:08 2024/8/14
 * @Description
 */
public enum SmsTemplateIDEnum {

    SMS_LOGIN_CODE_TEMPLATE("1","User Login Template");

    String templateId;
    String desc;

    SmsTemplateIDEnum(String templateId, String desc) {
        this.templateId = templateId;
        this.desc = desc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getDesc() {
        return desc;
    }
}
