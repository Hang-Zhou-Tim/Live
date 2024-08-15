package org.hang.live.im.core.server.common;

import org.hang.live.im.constants.ImConstants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author hang
 * @Date: Created in 22:15 2024/8/13
 * @Description
 */
public class ImMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = -6567417873780541989L;
    //version that validates the IM Message
    private short magic;

    //Used to identify the business context to handle the message.
    private int code;

    //Record length of this message
    private int len;

    //Store body in bytes
    private byte[] body;


    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    //Used to build IM message.
    public static ImMsg build(int code,String data) {
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setBody(data.getBytes());
        imMsg.setLen(imMsg.getBody().length);
        return imMsg;
    }

    @Override
    public String toString() {
        return "ImMsg{" +
                "magic=" + magic +
                ", len=" + len +
                ", code=" + code +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
