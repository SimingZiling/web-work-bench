package org.webworkbench.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.webworkbench.netty.resolution.RequestParser;
import org.webworkbench.web.request.HttpRequest;

import java.io.IOException;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;

            // 封装请求方法
            httpRequest.setMethod(RequestParser.method(fullHttpRequest));

            // 添加统一资源标志符
            httpRequest.setUri(fullHttpRequest.uri());

            // 获取请求路径
            httpRequest.setPath(RequestParser.path(fullHttpRequest));

            // 获取请求头
            httpRequest.setHeaders(RequestParser.headers(fullHttpRequest));

            // 请求参数
            httpRequest.setParameter(RequestParser.parm(fullHttpRequest));


            System.out.println(RequestParser.body(fullHttpRequest));
            System.out.println("该请求为Http请求");
        }
    }


}


