package remoting.transport.netty.server;

import config.CustomShutdownHook;
import config.RpcServiceConfig;
import factory.SingletonFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import provider.ServiceProvider;
import provider.impl.ZkServiceProviderImpl;
import remoting.transport.netty.codec.RpcMessageDecoder;
import remoting.transport.netty.codec.RpcMessageEncoder;
import utils.RuntimeUtil;
import utils.concurrent.threadpool.ThreadPoolFactoryUtils;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NettyRpcServer {

    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtils.createThreadFactory("service" +
                        "-handler-group", false));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // Nagle ??????????????????????????????????????????????????????????????????????????????????????????
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // ???????????? TCP ??????????????????
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(host, PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server", e);
        }finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }
}
