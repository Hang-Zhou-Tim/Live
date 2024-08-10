package org.hang.live.common.interfaces.utils;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/*
 *
 * @Author hang
 * @Description
 */
public class IpLogConversionRule extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return this.getLogIndex();
    }

    private String getLogIndex() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000));
    }
}
