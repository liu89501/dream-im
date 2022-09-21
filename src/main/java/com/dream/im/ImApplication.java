package com.dream.im;

import com.dream.container.anno.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(proxy = false)
public class ImApplication {

    @Assign
    private ImServerInitializer initializer;

    @LaunchArg("-port")
    private int listenPort;

    @Exec(value = EExecPriority.HIGH, runPriority = EExecRunPriority.STANDALONE)
    public void run() throws InterruptedException
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(initializer);

            log.info("Open your web browser and navigate to ws://127.0.0.1:" + listenPort + '/');
            Channel ch = b.bind(listenPort).sync().channel();
            ch.closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
