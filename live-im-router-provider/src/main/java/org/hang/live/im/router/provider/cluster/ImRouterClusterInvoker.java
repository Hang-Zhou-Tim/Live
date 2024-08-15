package org.hang.live.im.router.provider.cluster;

import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 14:25 2024/8/12
 * @Description
 */
public class ImRouterClusterInvoker<T> extends AbstractClusterInvoker<T> {

    public ImRouterClusterInvoker(Directory<T> directory) {
        super(directory);
    }

    @Override
    protected Result doInvoke(Invocation invocation, List list, LoadBalance loadbalance) throws RpcException {
        checkWhetherDestroyed();
        String ip = (String) RpcContext.getContext().get("ip");
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException("ip can not be null!");
        }
        //Get all RPC service providers, and filtered ip of these provider to get the one that is matched with ip set in context.
        //Notice that the ip in context is set before routers sends batch message and decided which message is sending to which ip.
        List<Invoker<T>> invokers = list(invocation);
        Invoker<T> matchInvoker = invokers.stream().filter(invoker -> {
            //Get exposed address for the provider
            String serverIp = invoker.getUrl().getHost() + ":" + invoker.getUrl().getPort();
            return serverIp.equals(ip);
        }).findFirst().orElse(null);
        if (matchInvoker == null) {
            throw new RuntimeException("ip is invalid");
        }
        return matchInvoker.invoke(invocation);
    }
}
