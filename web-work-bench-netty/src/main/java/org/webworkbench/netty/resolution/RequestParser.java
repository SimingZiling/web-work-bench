package org.webworkbench.netty.resolution;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.webworkbench.netty.common.ContentType;
import org.webworkbench.netty.converter.PrimitiveType;
import org.webworkbench.web.request.HttpMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求解析
 */
public class RequestParser {

    /**
     * 私有构造函数 防止用户实例化
     */
    private RequestParser() {}

    /**
     * 参数解析
     * @param httpRequest http请求
     * @return 请求参数Map
     */
    public static Map<String, List<String>> parm(HttpRequest httpRequest){
        String uri = httpRequest.uri();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
        return queryDecoder.parameters();
    }

    /**
     * 路径解析
     * @param httpRequest 请求路径
     * @return 请求路径
     */
    public static String path(HttpRequest httpRequest){
        String uri = httpRequest.uri();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
        return queryDecoder.path();
    }

    /**
     * 头解析
     * @param fullHttpRequest 全部http请求
     * @return 请求头
     */
    public static Map<String,String> headers(FullHttpRequest fullHttpRequest){
        Map<String,String> headerMap = new HashMap<String,String>();
        for (Map.Entry<String,String> header : fullHttpRequest.headers().entries()){
            headerMap.put(header.getKey(),header.getValue());
        }
        return headerMap;
    }

    /**
     * 请求方法解析
     * @param fullHttpRequest 全部Http请求
     * @return 请求方法
     */
    public static HttpMethod method(FullHttpRequest fullHttpRequest){
        if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.POST)){
            return HttpMethod.POST;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.PUT)){
            return HttpMethod.PUT;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.GET)){
            return HttpMethod.GET;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.DELETE)){
            return HttpMethod.DELETE;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.PATCH)){
            return HttpMethod.PATCH;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.TRACE)){
            return HttpMethod.TRACE;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.CONNECT)){
            return HttpMethod.CONNECT;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.OPTIONS)){
            return HttpMethod.OPTIONS;
        }else if(fullHttpRequest.method().equals(io.netty.handler.codec.http.HttpMethod.HEAD)){
            return HttpMethod.HEAD;
        }else {
            return HttpMethod.UNKNOWN;
        }
    }

    public static Map<String,List<String>> bodyParse(FullHttpRequest fullHttpRequest){
        Map<String, List<String>> paramMap = new HashMap<>();
        String contentType = getContentType(fullHttpRequest.headers());
        //  判断内容类型
        if(ContentType.APPLICATION_JSON.toString().equals(contentType)){
            // 获取内容冰转成json格式
            String jsonStr = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            JSONObject obj = JSON.parseObject(jsonStr);
            // 遍历json数据
            for (Map.Entry<String,Object> item : obj.entrySet()){
                // 获取Key与value
                String key = item.getKey();
                Object value = item.getValue();
                Class<?> valueType = value.getClass();

                System.out.println("KEY为"+key+"的数据类型为"+valueType.getName());

                // 判断数据是否已经存在
                List<String> valueList;
                if(paramMap.containsKey(key)){
                    valueList = paramMap.get(key);
                }else{
                    valueList = new ArrayList<String>();
                }
                // 判断是否为基础类型
                if(PrimitiveType.isPriType(valueType)){
                    valueList.add(value.toString());
                    paramMap.put(key, valueList);
                }else
                    // 判断类型是否为数组
                    if(valueType.isArray()){
                    int length = Array.getLength(value);
                    for(int i=0; i<length; i++){
                        String arrayItem = String.valueOf(Array.get(value, i));
                        valueList.add(arrayItem);
                    }
                    paramMap.put(key, valueList);
                }else
                    // 判断数据是否为列表
                    if(JSONObject.class.isAssignableFrom(valueType)){
//                        if(valueType.equals(JSONArray.class)){
//                            JSONArray jArray = JSONArray.parseArray(value.toString());
//                            for(int i=0; i<jArray.size(); i++){
//                                valueList.add(jArray.getString(i));
//                            }
//                        }else{
//                            valueList = (ArrayList<String>) value;
//                        }
//                        paramMap.put(key, valueList);
                }else
                    // 判断数据是否为map
                    if(Map.class.isAssignableFrom(valueType)){
                        Map<String, String> tempMap = (Map<String, String>) value;
                        for(Map.Entry<String, String> entry : tempMap.entrySet()){
                            List<String> tempList = new ArrayList<String>();
                            tempList.add(entry.getValue());
                            paramMap.put(entry.getKey(), tempList);
                        }
                    }
            }
        }
        return paramMap;
    }

//    public Map<String, Object> bodyParse() throws IOException {
//        Map<String, Object> bodyMap = new HashMap<>();
//        if(!fullHttpRequest.method().equals(HttpMethod.GET)){
//            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
//            decoder.offer(fullHttpRequest);
//            List<InterfaceHttpData> bodyList = decoder.getBodyHttpDatas();
//            for (InterfaceHttpData body : bodyList) {
//                if(body instanceof FileUpload){
//                    FileUpload file =  (FileUpload) body;
//                    bodyMap.put(file.getName(),file.getFile().getCanonicalFile());
////                    System.out.println(file.getName());
////                    System.out.println(file.getFile());
//                }else {
//                    Attribute data = (Attribute) body;
//                    bodyMap.put(data.getName(), data.getValue());
//                }
//            }
//            return bodyMap;
//        }
//        return null;
//    }

    /**
     * 获取ContentType
     * @param headers http请求头
     * @return 内容类型
     */
    private static String getContentType(HttpHeaders headers){
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        String[] list = contentType.split(";");
        return list[0];
    }

}
