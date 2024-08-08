package org.hang.live.id.generate.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author Hang
 * @Date: Created in 19:59 2023/5/23
 * @Description
 */
@TableName("t_id_generate_config")
public class IdGeneratePO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * id description
     */
    private String remark;

    /**
     * Initialised number
     */
    private long initNum;

    /**
     * Steps
     */
    private int step;

    /**
     * Is sequential id
     */
    private int isSeq;

    private long currentStart;

    private long nextThreshold;

    private String idPrefix;

    private int version;

    private Date createTime;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getInitNum() {
        return initNum;
    }

    public void setInitNum(long initNum) {
        this.initNum = initNum;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(long currentStart) {
        this.currentStart = currentStart;
    }

    public long getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(long nextThreshold) {
        this.nextThreshold = nextThreshold;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsSeq() {
        return isSeq;
    }

    public void setIsSeq(int isSeq) {
        this.isSeq = isSeq;
    }

    @Override
    public String toString() {
        return "IdGeneratePO{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", initNum=" + initNum +
                ", step=" + step +
                ", isSeq=" + isSeq +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                ", idPrefix='" + idPrefix + '\'' +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}