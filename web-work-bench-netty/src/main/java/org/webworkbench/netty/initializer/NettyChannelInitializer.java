package org.webworkbench.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.webworkbench.netty.handler.HttpServerHandler;
import org.webworkbench.web.config.WebConfig;

/**
 * Netty通道初始化
 */
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("HttpServerCodec", new HttpServerCodec());//设置解码器

        pipeline.addLast("HttpResponseEncoder",new HttpResponseEncoder());
        if(WebConfig.LOGIN){
            pipeline.addLast("LoggingHandler",new LoggingHandler(LogLevel.INFO));//设置log监听器
        }
        pipeline.addLast("HttpObjectAggregator",new HttpObjectAggregator(10*1024*1024));//聚合器
        pipeline.addLast("HttpServerHandler", new HttpServerHandler());// 处理业务
    }

}
