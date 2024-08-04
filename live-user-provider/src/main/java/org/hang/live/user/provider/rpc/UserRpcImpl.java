package org.hang.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.hang.user.interfaces.IUserRPC;

@DubboService
public class UserRpcImpl implements IUserRPC {
    @Override
    public String test(){
        System.out.println("this is Dubbo test");
        return "success";
    }

}
