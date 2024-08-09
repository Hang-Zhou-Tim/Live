package org.hang.live.user.constants;

/**
 * @Author hang
 * @Date: Created in 13:28 2023/5/26
 * @Description
 */
public enum UserTagsEnum {

    IS_RICH((long) Math.pow(2, 0), "Whether is rich", "tag_info_01"),
    IS_VIP((long) Math.pow(2, 1), "Whether is VIP", "tag_info_01"),
    IS_OLD_USER((long) Math.pow(2, 2), "Whether is old user", "tag_info_01");

    long tag;
    String desc;
    String fieldName;

    UserTagsEnum(long tag, String desc, String fieldName) {
        this.tag = tag;
        this.desc = desc;
        this.fieldName = fieldName;
    }

    public long getTag() {
        return tag;
    }

    public String getDesc() {
        return desc;
    }

    public String getFieldName() {
        return fieldName;
    }
}
