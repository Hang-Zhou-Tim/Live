package org.hang.live.im.core.server.starter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import jakarta.annotation.Resource;
import org.hang.live.im.core.server.common.ChannelHandlerContextCache;
import org.hang.live.im.core.server.handler.impl.WebsocketEncoder;
import org.hang.live.im.core.server.handler.ws.WsHandshakeHandler;
import org.hang.live.im.core.server.handler.ws.WsImServerCoreHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @Author hang
 * @Date: Created in 20:35 2024/8/11
 * @Description
 */
@Configuration
public class WsNettyImServerStarter implements InitializingBean {

    private static Logger LOGGER = LoggerFactory.getLogger(WsNettyImServerStarter.class);

    //Targeted Port info
    @Value("${hang.im.ws.port}")
    private int port;
    @Resource
    private WsHandshakeHandler wsHandshakeHandler;
    @Resource
    private WsImServerCoreHandler wsImServerCoreHandler;
    @Resource
    private Environment environment;

    //Initialise netty server with Non-Blocking I/O Reactor Architecture and bind designated port to listen connection.
    //The boss group only accepts connection and worker group only accepts read/write operation for accepted socket channel.
    public void startApplication() throws InterruptedException {
        //Deal with accept operation
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //Deal with read & write operation
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        //Init and registers netty handler to channels listened by the workers.
        bootstrap.childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                //Logs
                LOGGER.info("Connection Init!!!!!");
                //Use HTTP Server Encoder/Decoder for HTTP message. This is bidirectional.
                ch.pipeline().addLast(new HttpServerCodec());
                //Write Big File Messages in chunks. If the first sending time is bigger than default, it will create task event to async write unfinished chunks.
                ch.pipeline().addLast(new ChunkedWriteHandler());
                //Aggregate HTTP Chunks. This is inbound handler.
                ch.pipeline().addLast(new HttpObjectAggregator(8192));
                //Encode outbound Websocket message. This is outbound handler.
                ch.pipeline().addLast(new WebsocketEncoder());
                //Apply handshakes for any client's websocket connection request.
                ch.pipeline().addLast(wsHandshakeHandler);
                //Handle Business Logic
                ch.pipeline().addLast(wsImServerCoreHandler);
            }
        });
        //Gracefully shutdown this jvm program.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));
        //Get ip and port
        String registryIp = environment.getProperty("DUBBO_IP_TO_REGISTRY");
        String registryPort = environment.getProperty("DUBBO_PORT_TO_REGISTRY");
        if (StringUtils.isEmpty(registryPort) || StringUtils.isEmpty(registryIp)) {
            throw new IllegalArgumentException("IP or Port should not be null in the environment");
        }
        ChannelHandlerContextCache.setServerIpAddress(registryIp + ":" + registryPort);
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        LOGGER.info("Server Operation Started and Expose Port: {}", port);
        //Sync The Main Thread.
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread nettyServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startApplication();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        nettyServerThread.setName("live-im-server-ws");
        nettyServerThread.start();
    }
}
