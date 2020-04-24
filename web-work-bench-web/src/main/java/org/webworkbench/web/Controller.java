package org.webworkbench.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器
 */
public class Controller {

    /**
     * 请求方法列表
     */
    private static Map<String , Method> requestMethodMap = new HashMap<>();

    /**
     * 获取请求方法
     * @param requestUrl 请求地址
     * @return 请求方法
     */
    public static Method getRequestMethod(String requestUrl){
        return requestMethodMap.get(requestUrl);
    }

    /**
     * 加载请求方法
     */
    public void loaderRequestMethod(){
        for (Method method : Controller.class.getDeclaredMethods()){
            if(method.getName().equals("getRequestMethod")){
                Controller.requestMethodMap.put("/method",method);
            }
        }
        // 读取
    }

}
