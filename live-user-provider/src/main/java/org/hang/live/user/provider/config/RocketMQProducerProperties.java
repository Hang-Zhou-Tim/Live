package org.hang.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/22
 * @Description
 */
@ConfigurationProperties(prefix = "hang.rmq.producer")
@Configuration
public class RocketMQProducerProperties {

    private String nameSrv;
    private String groupName;
    private String applicationName;
    private Integer sendMsgTimeout;
    private Integer retryTimes;

    public String getNameSrv() {
        return nameSrv;
    }

    public void setNameSrv(String nameSrv) {
        this.nameSrv = nameSrv;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(Integer sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public String toString() {
        return "RocketMQProducerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", sendMsgTimeout=" + sendMsgTimeout +
                ", retryTimes=" + retryTimes +
                '}';
    }
}
