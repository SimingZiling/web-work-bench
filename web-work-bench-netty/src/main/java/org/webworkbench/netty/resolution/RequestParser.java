package org.webworkbench.netty.resolution;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.webworkbench.netty.common.ContentType;
import org.webworkbench.netty.converter.PrimitiveType;
import org.webworkbench.web.request.HttpMethod;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

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

    /**
     * 请求提解析
     * @param fullHttpRequest 完整请求
     * @return 请求参数
     */
    public static Map<String,Object> body(FullHttpRequest fullHttpRequest) throws IOException {
        String contentType = getContentType(fullHttpRequest.headers());
        System.out.println(contentType);
        // 判断内容的类型
        if(ContentType.APPLICATION_JSON.toString().equals(contentType)){
            // 内容为json格式
            String paramJsonStr = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            // 转换为json对象数据
//            JSONObject paramJsonObject = JSON.parseObject(paramJsonStr);
//            return jsonObjectToMap(paramJsonObject);
            // 一行代码搞定
            return JSON.parseObject(paramJsonStr,Map.class);
        }else if(ContentType.MULTIPART_FORM_DATA.toString().endsWith(contentType)){
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
            List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            for (InterfaceHttpData interfaceHttpData : httpPostData){
                if(interfaceHttpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute){
                    MemoryAttribute attribute = (MemoryAttribute) interfaceHttpData;
                    paramMap.put(interfaceHttpData.getName(),attribute.getValue());
//                    System.out.println("key ="+interfaceHttpData.getName() + " v = " +attribute.getValue());
                }else if(interfaceHttpData.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload){
                    FileUpload httpFileUpload = (FileUpload) interfaceHttpData;
                    org.webworkbench.web.request.FileUpload fileUpload = new org.webworkbench.web.request.FileUpload();
                    fileUpload.setBytes(httpFileUpload.get());
                    fileUpload.setFileName(httpFileUpload.getFilename());
                    fileUpload.setFileType(httpFileUpload.getContentType());
                    paramMap.put(interfaceHttpData.getName(),fileUpload);
                }
            }
            return paramMap;
//            System.out.println(fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        }else if(ContentType.APPLICATION_FORM_URLENCODED.toString().endsWith(contentType)){
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(true), fullHttpRequest);
            List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            for (InterfaceHttpData interfaceHttpData : httpPostData){
                MemoryAttribute attribute = (MemoryAttribute) interfaceHttpData;
                paramMap.put(interfaceHttpData.getName(),attribute.getValue());
            }
            return paramMap;
        }
        return null;
    }

//    public static void uploud(byte[] bytes){
//        String filepath ="E:\\" + "a.jpg";
//
//        File file  = new File(filepath);
//        if(file.exists()){
//            file.delete();
//        }
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(bytes,0,bytes.length);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * json转换为map数据
     * @param jsonObject json数据
     * @return map对象
     */
    private static Map<String,Object> jsonObjectToMap(JSONObject jsonObject){
        Map<String, Object> map = new HashMap<>();
        // 遍历json对象
        for (Map.Entry<String,Object> item : jsonObject.entrySet()){
            String key = item.getKey();
            Object value = item.getValue();
            // 获取值类型
            Class<?> valueType = value.getClass();

//             判断数据是否已经存在
            List<Object> valueList = null;
            if(map.containsKey(key)){
                valueList = new ArrayList<>();
                valueList.add(map.get(key));
            }

            // 判断数据类型
            if(PrimitiveType.isPriType(valueType)){
                // 数据为基础类型
                if(valueList != null){
                    valueList.add(value.toString());
                    map.put(key, valueList);
                }else{
                    map.put(key,value.toString());
                }
                
            }else if(valueType.isArray()) {
                // 判断类型是否为数组
                if(valueList == null){
                    valueList = new ArrayList<>();
                }
                for (int i = 0; i < Array.getLength(value); i++) {
                    valueList.add(Array.get(value, i));
                }
                map.put(key, valueList);
            }else if(JSONObject.class.isAssignableFrom(valueType)){

                if(valueList != null) {
                    valueList.add(jsonObjectToMap((JSONObject) value));
                    map.put(key, valueList);
                }else {
                    map.put(key,jsonObjectToMap((JSONObject) value));
                }
            }else if(JSONArray.class.isAssignableFrom(valueType)){
               map.put(key,jsonArrayToList((JSONArray) value));
            }
        }
        return map;
    }

    /**
     * JSON数组转列表
     * @param jsonArray json数组
     * @return 列表
     */
    private static List<Object> jsonArrayToList(JSONArray jsonArray){
        List<Object> list = new ArrayList<>();
        // 遍历json数组
        for (Object value : jsonArray) {
            // 获取数组中元素对象
            Class<?> valueType = value.getClass();
            // 判断元素对象
            if (PrimitiveType.isPriType(valueType)) {
                // 如果元素对象为基础对象则直接添加
                list.add(value);
            } else if (valueType.isArray()) {
                // 如果元素对象为列表 则遍历出来之后添加
                for (int j = 0; j < Array.getLength(value); j++) {
                    list.add(Array.get(value, j));
                }
            } else if (JSONObject.class.isAssignableFrom(valueType)) {
                // 如果元素为json 则读取json数并保存
                list.add(jsonObjectToMap((JSONObject) value));
            } else if (JSONArray.class.isAssignableFrom(valueType)) {
                // 如果元素为json数组，则读取数据列表并保存
                list.add(jsonArrayToList((JSONArray) value));
            }
        }
        return list;
    }

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
