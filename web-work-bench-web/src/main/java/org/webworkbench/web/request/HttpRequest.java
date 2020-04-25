package org.webworkbench.web.request;

/**
 * HTTP请求
 */
public class HttpRequest {

    /**
     * 请求方法
     */
    private HttpMethod method;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
}
