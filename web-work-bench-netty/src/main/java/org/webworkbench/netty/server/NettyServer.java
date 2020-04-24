package org.webworkbench.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.webworkbench.netty.initializer.NettyChannelInitializer;
import org.webworkbench.web.config.WebConfig;
import org.webworkbench.web.init.WebInit;

/**
 * 服务启动
 */
public class NettyServer {

    private static void run(){

        WebInit webInit = new WebInit();
        webInit.init();

        System.out.println(WebConfig.SERVER_POST);

        // 启动线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 工作线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyChannelInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(WebConfig.SERVER_POST).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        run();
    }

}
