package com.mdd.proxyip.utils;

import java.util.Map;

/**
 * Created by xwl on 2017/8/16.
 * request请求实体封装
 */
public class HttpRequestData {

    private String requestUrl;
    private String requestMethod;
    private Map<String, String> headers;
    private Map<String, String> params;

    @Override
    public String toString() {
        return "HttpRequestData{" +
                "requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                '}';
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
