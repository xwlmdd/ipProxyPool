package com.mdd.proxyip.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import sun.misc.BASE64Encoder;  
  
  
public class Request {  
  
    public static void main(String[] args) throws UnsupportedEncodingException {
        String httpUrl = "http://apis.baidu.com/apistore/idlocr/ocr";  
        String imagePath="C:\\Users\\Administrator\\Desktop\\ygrandimg.png";
        String str=encodeImgageToBase64(imagePath);
        System.out.println(str);
        str = str.replace("\r\n", "");  
        str= URLEncoder.encode(str, "utf-8");//很重要的，刚开始就是因为没有加，所以怎么看结果怎么不合理
        String httpArg = "fromdevice=pc&clientip=172.0.0.1&detecttype=LocateRecognize&"+  
        "languagetype=CHN_ENG&imagetype=1"+  
        "&image="+str;  
        String jsonResult = request(httpUrl, httpArg);  
        System.out.println(jsonResult);  
    }  
      
      
  
    /** 
     * @param httpUrl
     *            :请求接口 
     * @param httpArg 
     *            :参数 
     * @return 返回结果 
     */  
    public static String request(String httpUrl, String httpArg) {  
        BufferedReader reader = null;  
        String result = null;  
        StringBuffer sbf = new StringBuffer();  
  
        try {  
            URL url = new URL(httpUrl);  
            HttpURLConnection connection = (HttpURLConnection) url  
                    .openConnection();  
            connection.setRequestMethod("POST");  
            connection.setRequestProperty("Content-Type",  
                            "application/x-www-form-urlencoded");  
            // 填入apikey到HTTP header  
            connection.setRequestProperty("apikey",  "kdZU5aOeI7FguVfWzql7LOGM");
            connection.setDoOutput(true);  
            connection.getOutputStream().write(httpArg.getBytes("UTF-8"));  
            connection.connect();  
            InputStream is = connection.getInputStream();  
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
            String strRead = null;  
            while ((strRead = reader.readLine()) != null) {  
                sbf.append(strRead);  
                sbf.append("\r\n");  
            }  
            reader.close();  
            result = sbf.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
      
      
      
    /**  
     * 将本地图片进行Base64位编码  
     *   
     * @param imagePath
     *            图片的url路径，如d:\\中文.jpg  
     * @return  
     */    
    public static String encodeImgageToBase64(String imagePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理    
        // 其进行Base64编码处理    
        byte[] data = null;    
        // 读取图片字节数组    
        try {    
            File imageFile = new File(imagePath);  
            InputStream in = new FileInputStream(imageFile);    
            data = new byte[in.available()];    
            in.read(data);    
            in.close();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        
        // 对字节数组Base64编码    
        BASE64Encoder encoder = new BASE64Encoder();    
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串    
    }   
      
  
}  