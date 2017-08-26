package com.mdd.proxyip.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xwl on 2017/8/23.
 */
public class ImageBase64ToStringUtils {

    public static String imageToStringByBase64(String url) {
        String imageString = "";
        HttpRequestData httpRequestData = new HttpRequestData();
        httpRequestData.setRequestUrl(url);
        httpRequestData.setRequestMethod("get");
        HttpResponse response = HttpClientUtils.execute(httpRequestData);
        if (response.getStatusLine().getStatusCode() == 200) {
            InputStream inputStream = null;
            try {
                inputStream = response.getEntity().getContent();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                imageString = Base64Util.encode(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            BASE64Encoder base64Encoder = new BASE64Encoder();
        }
        return imageString;
    }

    public static void main(String[] args) {
        System.out.println(imageToStringByBase64("http://jwgl.jxau.edu.cn/User/Validation/"));
    }
}
