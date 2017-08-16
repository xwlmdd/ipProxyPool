package com.mdd.proxyip.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xwl on 2017/8/16.
 * http工具类
 */
public class HttpClientUtils {

    private static Logger logger = Logger.getLogger(HttpClientUtils.class);

    public static HttpClient httpClient = HttpClients.createDefault();

    public static HttpResponse postRequest(HttpRequestData httpRequestData) {
        if (httpRequestData != null && "post".equalsIgnoreCase(httpRequestData.getRequestMethod())) {
            HttpPost httpPost = new HttpPost();
            httpPost.setHeaders(mapConvertHeader(httpRequestData.getHeaders()));
            List<BasicNameValuePair> basicNameValuePairs = mapCovertBasicNameValuePair(httpRequestData.getParams());
            HttpEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(basicNameValuePairs, "utf-8");
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                logger.error("构造参数失败！", e);
            } catch (IOException e) {
                logger.error("请求失败！", e);
            }

        }
        return null;
    }

    /**
     * post请求参数转换
     *
     * @return
     */
    private static List<BasicNameValuePair> mapCovertBasicNameValuePair(Map<String, String> paramMap) {
        if (paramMap == null) {
            logger.info("请求参数为空！");
        }
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            basicNameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return basicNameValuePairs;
    }


    /**
     * map转Header数组
     *
     * @return
     */
    private static Header[] mapConvertHeader(Map<String, String> headerMap) {
        if (headerMap == null) {
            logger.info("请求头信息为空！");
        }
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return (Header[]) headers.toArray();
    }
}
