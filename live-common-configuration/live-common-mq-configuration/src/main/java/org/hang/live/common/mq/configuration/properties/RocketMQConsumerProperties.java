package org.hang.live.common.mq.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
@ConfigurationProperties(prefix = "hang.rmq.consumer")
@Configuration
public class RocketMQConsumerProperties {

    private String nameSrv;
    private String groupName;

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

    @Override
    public String toString() {
        return "RocketMQConsumerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
