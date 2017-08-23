package com.mdd.proxyip.utils;

import org.apache.http.HttpResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xwl on 2017/8/23.
 */
public class DownLoadUtils {

    public static void downLoadImage(String url) throws IOException {
        HttpRequestData httpRequestData = new HttpRequestData();
        httpRequestData.setRequestMethod("get");
        httpRequestData.setRequestUrl(url);
        HttpResponse response = HttpClientUtils.execute(httpRequestData);
        if (response.getStatusLine().getStatusCode()==200){
            try {
                InputStream inputStream = response.getEntity().getContent();
                OutputStream outputStream = new FileOutputStream("g:/xxx.jpg");
                byte[]buffer = new byte[1024];
                while (inputStream.read(buffer)!=-1){
                    outputStream.write(buffer);
                }
                if (inputStream!=null){
                    inputStream.close();
                }
                if (outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        downLoadImage("http://jwgl.jxau.edu.cn/User/Validation/");
    }
}
