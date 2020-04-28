package org.webworkbench.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.webworkbench.netty.resolution.RequestParser;
import org.webworkbench.web.request.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            // 封装请求方法
            packageRequestMethod(httpRequest,fullHttpRequest.method());
            // 添加通一资源标志符
            httpRequest.setUri(fullHttpRequest.uri());

            // 获取请求头
            Map<String,String> headerMap = new HashMap<String,String>();
            for (Map.Entry<String,String> header : fullHttpRequest.headers().entries()){
                headerMap.put(header.getKey(),header.getValue());
            }
            httpRequest.setHeaders(headerMap);

            System.out.println(new RequestParser(fullHttpRequest).bodyParse());


            System.out.println(httpRequest.getHeaders());
            System.out.println(httpRequest.getUri());
            System.out.println("2222"+httpRequest.getMethod().getMethod());
            System.out.println("该请求为Http请求");
        }
    }


    /**
     * 封装请求方法
     */
    private void packageRequestMethod(HttpRequest httpRequest,HttpMethod method){
        if(method.equals(HttpMethod.POST)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.POST);
        }else if(method.equals(HttpMethod.PUT)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.PUT);
        }else if(method.equals(HttpMethod.GET)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.GET);
        }else if(method.equals(HttpMethod.DELETE)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.DELETE);
        }else if(method.equals(HttpMethod.PATCH)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.PATCH);
        }else if(method.equals(HttpMethod.TRACE)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.TRACE);
        }else if(method.equals(HttpMethod.CONNECT)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.CONNECT);
        }else if(method.equals(HttpMethod.OPTIONS)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.OPTIONS);
        }else if(method.equals(HttpMethod.HEAD)){
            httpRequest.setMethod(org.webworkbench.web.request.HttpMethod.HEAD);
        }else {
            System.err.println("未知请求！"+method.name());
        }
    }
}


