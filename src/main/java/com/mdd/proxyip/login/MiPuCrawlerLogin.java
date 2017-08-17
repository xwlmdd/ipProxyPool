package com.mdd.proxyip.login;

import com.mdd.proxyip.utils.HttpClientUtils;
import com.mdd.proxyip.utils.HttpRequestData;
import org.apache.http.HttpResponse;

import java.util.HashMap;

/**
 * Created by xwl on 2017/8/16.
 */
public class MiPuCrawlerLogin {

    private static final String LOGIN_URL = "http://proxy.mimvp.com/lib/user_login_check.php";

    public String login() {
        HttpRequestData httpRequestData = new HttpRequestData();
        httpRequestData.setRequestMethod("POST");
        //请求头
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Host","proxy.mimvp.com");
        headerMap.put("Connection","keep-alive");
        headerMap.put("Content-Length","58");
        headerMap.put("Accept","application/json, text/javascript, */*; q=0.01");
        headerMap.put("Host","http://proxy.mimvp.com");
        headerMap.put("Origin","proxy.mimvp.com");
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
        headerMap.put("X-Requested-With","XMLHttpRequest");
        headerMap.put("Referer","http://proxy.mimvp.com/usercenter/login.php");
        headerMap.put("Accept-Encoding","gzip, deflate");
        headerMap.put("Accept-Language","zh-CN,zh;q=0.8");
        //请求参数
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_email","1072552712@qq.com");
        paramsMap.put("user_pwd","yzq0914");
        paramsMap.put("remember","1");
        httpRequestData.setHeaders(headerMap);
        httpRequestData.setParams(paramsMap);
        httpRequestData.setRequestUrl(LOGIN_URL);
        HttpResponse httpResponse = HttpClientUtils.execute(httpRequestData);
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        return "";
    }

    public static void main(String[] args) {
        new MiPuCrawlerLogin().login();
    }

}
