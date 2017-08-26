package com.mdd.proxyip.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.HashMap;

/**
 * Created by xwl on 2017/8/20.
 */
public class VCodeCheckUtils {

    private static final Logger logger = Logger.getLogger(VCodeCheckUtils.class);

    private static  String OCRUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
//    private static String OCRUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/webimage";
    private static final  String ACCESS_TOKEN =  getAccessToken();

    /**
     * 获取AccessToken
     * 百度开发
     * AppId:10028388
     * APIKey:kdZU5aOeI7FguVfWzql7LOGM
     * SecretKey:Xxcze1I2RLUhB8NFd7T4u4fHdBGundrn
     *
     * @return
     */
    public static String getAccessToken() {
        String accessToken = "";
        HttpRequestData httpRequestData = new HttpRequestData();
        HashMap<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", "kdZU5aOeI7FguVfWzql7LOGM");
        params.put("client_secret", "Xxcze1I2RLUhB8NFd7T4u4fHdBGundrn");
        httpRequestData.setRequestMethod("GET");
        httpRequestData.setParams(params);
        httpRequestData.setRequestUrl("https://aip.baidubce.com/oauth/2.0/token");
        HttpResponse response = HttpClientUtils.execute(httpRequestData);
        String json = "";
        try {
            json = IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject != null && !jsonObject.isEmpty()) {
                accessToken = jsonObject.getString("access_token");
            }
        }
        return accessToken;
    }

    /**
     * 获取识别验证码
     * @param imageUrl
     * @return
     */
    public static String OCRVCode(String imageUrl){
        String VCode = "";

        if (StringUtils.isBlank(ACCESS_TOKEN)) {
            logger.error("accessToken为空");
            return VCode;
        }
        OCRUrl = OCRUrl + "?access_token=" + ACCESS_TOKEN;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        HashMap<String, String> params = new HashMap<>();
        imageUrl = ImageBase64ToStringUtils.imageToStringByBase64(imageUrl);
        params.put("image", imageUrl);

        HttpRequestData httpRequestData = new HttpRequestData();
        httpRequestData.setHeaders(headers);
        httpRequestData.setRequestMethod("post");
        httpRequestData.setParams(params);
        httpRequestData.setRequestUrl(OCRUrl);
        HttpResponse response = HttpClientUtils.execute(httpRequestData);
        String json = "";
        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                json = IOUtils.toString(response.getEntity().getContent());
                System.out.println(json);
            } catch (IOException e) {
                logger.error("请求识别失败！", e);
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray wordsResult = jsonObject.getJSONArray("words_result");
        VCode = wordsResult.getJSONObject(0).getString("words");
        return VCode;
    }

    /**
     * 将本地图片进行Base64位编码
     * @param imageFile
     * @return
     */
    public static String encodeImgageToBase64(String imageFile) {
        // 其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imageFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        return Base64Util.encode(data);
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(getAccessToken());
//        System.out.println(OCRVCode("G:ygrandimg.png"));
        System.out.println(OCRVCode("http://proxy.mimvp.com/common/ygrandimg.php?id=7&port=NmTiAmzvMpTI4"));
    }
}
