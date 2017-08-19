package com.mdd.proxyip.login;

import com.mdd.proxyip.utils.HttpClientUtils;
import com.mdd.proxyip.utils.HttpRequestData;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xwl on 2017/8/16.
 */
public class MiPuCrawlerLogin {

    private static final String LOGIN_URL = "http://proxy.mimvp.com/lib/user_login_check.php";

    public HttpResponse login()  {
        HttpRequestData httpRequestData = new HttpRequestData();
        httpRequestData.setRequestMethod("POST");


        //请求头
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept","application/json, text/javascript, */*; q=0.01");
        headerMap.put("Accept-Encoding","gzip, deflate");
        headerMap.put("Accept-Language","zh-CN,zh;q=0.8");
        headerMap.put("Connection","keep-alive");
        headerMap.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Host","proxy.mimvp.com");
        headerMap.put("Origin","proxy.mimvp.com");
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
        headerMap.put("X-Requested-With","XMLHttpRequest");
        headerMap.put("Referer","http://proxy.mimvp.com/usercenter/login.php");
        //请求参数
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_email","1072552712@qq.com");
        paramsMap.put("user_pwd","yzq0914");
        paramsMap.put("remember","1");

        httpRequestData.setHeaders(headerMap);
        httpRequestData.setParams(paramsMap);
        httpRequestData.setRequestUrl(LOGIN_URL);
        HttpResponse httpResponse = HttpClientUtils.execute(httpRequestData);
        List<Cookie> cookies = HttpClientUtils.cookieStore.getCookies();
        for (Cookie c:cookies){
            System.out.println(c.getName()+"---"+c.getValue());
        }
//        HttpClientUtils.httpClient.
//        System.out.println(httpResponse.getStatusLine().getStatusCode());
////        System.out.println(httpResponse);
//        HttpRequestData hd = new HttpRequestData();
//        hd.setRequestMethod("get");
//        hd.setRequestUrl("http://proxy.mimvp.com/api/fetch.php?orderid=1072552712@qq.com&num=20&result_fields=1,2");
//        HttpResponse e = HttpClientUtils.execute(hd);
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    e.getEntity().getContent()));
//            String content = "";
//            for(String s = reader.readLine(); s != null; s = reader.readLine()){
//                content += s;
//            }
//            System.out.println(content);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
        return httpResponse;
    }

    public static void main(String[] args) throws IOException {
        new MiPuCrawlerLogin().login();
    }

}
