package org.webworkbench.netty.resolution;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {

    private FullHttpRequest fullHttpRequest; // 全部请求信息

    public RequestParser(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public Map<String, String> parmParse(){
        Map<String, String> parmMap = new HashMap<>();
        QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
        decoder.parameters().forEach((key, value) -> {
            // entry.getValue()是一个List, 只取第一个元素
            parmMap.put(key.trim(), value.get(0));
        });
        return parmMap;
    }

    public Map<String, Object> bodyParse() throws IOException {
        Map<String, Object> bodyMap = new HashMap<>();
        if(!fullHttpRequest.method().equals(HttpMethod.GET)){
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
            decoder.offer(fullHttpRequest);
            List<InterfaceHttpData> bodyList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData body : bodyList) {
                if(body instanceof FileUpload){
                    FileUpload file =  (FileUpload) body;
                    bodyMap.put(file.getName(),file.getFile());
//                    System.out.println(file.getName());
//                    System.out.println(file.getFile());
                }else {
                    Attribute data = (Attribute) body;
                    bodyMap.put(data.getName(), data.getValue());
                }
            }
            return bodyMap;
        }
        return null;
    }

}
