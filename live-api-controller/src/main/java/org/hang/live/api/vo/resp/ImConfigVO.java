package org.hang.live.api.vo.resp;

/**
 * Return im token and ws server address
 *
 * @Author hang
 * @Date: Created in 10:49 2024/8/13
 * @Description
 */
public class ImConfigVO {

    private String token;
    private String wsImServerAddress;
    private String tcpImServerAddress;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWsImServerAddress() {
        return wsImServerAddress;
    }

    public void setWsImServerAddress(String wsImServerAddress) {
        this.wsImServerAddress = wsImServerAddress;
    }

    public String getTcpImServerAddress() {
        return tcpImServerAddress;
    }

    public void setTcpImServerAddress(String tcpImServerAddress) {
        this.tcpImServerAddress = tcpImServerAddress;
    }
}
