package org.hang.live.im.router.provider.cluster;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

/**
 * Inject self-defined cluster by using SPI mechanism based on RPC context.
 * This cluster will generate self-designed invoker that invoke RPC calls in a specific machine.
 *
 * @Author hang
 * @Date: Created in 14:24 2024/8/12
 * @Description
 */
public class ImRouterCluster implements Cluster {

    @Override
    public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
        return new ImRouterClusterInvoker<>(directory);
    }
}
