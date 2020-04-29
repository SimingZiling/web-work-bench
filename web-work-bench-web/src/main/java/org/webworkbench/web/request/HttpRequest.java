package org.webworkbench.web.request;

import java.util.List;
import java.util.Map;

/**
 * HTTP请求
 */
public class HttpRequest {

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 通一资源标志符
     */
    private String uri;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求参数
     */
    private Map<String, List<String>> parameter;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, List<String>> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, List<String>> parameter) {
        this.parameter = parameter;
    }
}
