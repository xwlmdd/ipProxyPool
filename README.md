项目介绍
基于Spring-boot+Mybatis+redis+webmagic+百度orc开发系统架构。主要爬取的网站有全网代理、西刺代理、米扑代理、快代理。主要可以使用的IP代理分布在米扑和西刺，其他两个比较少可以用。其中米扑代理IP端口是图片，这里使用了百度免费orc识别图片。（有兴趣的同学可以看一下Tess4j技术训练识别类库）

环境搭建
该项目采用maven统一构建，首先创建一个maven项目，在pom.xml文件中以下依赖
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mdd</groupId>
    <artifactId>ipproxypool</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>ipProxyPool</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.7</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <!--<scope>runtime</scope>-->
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.restdocs</groupId>
            <artifactId>spring-restdocs-mockmvc</artifactId>
            <scope>test</scope>
        </dependency>
        <!--<dependency>-->
        <!--<groupId>org.springframework.boot</groupId>-->
        <!--<artifactId>spring-boot-starter-log42j</artifactId>-->
        <!--</dependency>-->
        <!--webMagic -->
        <dependency>
            <groupId>us.codecraft</groupId>
            <artifactId>webmagic-core</artifactId>
            <version>0.5.3</version>
        </dependency>
        <dependency>
            <groupId>us.codecraft</groupId>
            <artifactId>webmagic-extension</artifactId>
            <version>0.5.3</version>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.4</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/commons-lang/commons-lang -->
        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>


    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.2</version>
                <configuration>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
编写Application程序启动类
package com.mdd.proxyip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

   public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
   }
}
```
关键代码如下
使用webmagic获取ip代理详情（全网代理ip分散在不同标签【找规律解析出来】|| 米扑代理ip端口是图片【采用百度orc】）
```
/**
 * 云代理网站ip抓取
 * @author xwl 2017.6.3
 */
@Component
public class QuanWanProxyIpCrawler implements PageProcessor {

   private Logger logger = Logger.getLogger(QuanWanProxyIpCrawler.class);

   // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
   private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1000)
         .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

   public Site getSite() {
      return site;
   }

   public void process(Page page) {
      Html html = page.getHtml();
      List<String> urlList = page.getHtml().regex("http://www\\.goubanjia\\.com/index\\d*\\.shtml").all();
      // 结果集
      List<ProxyIp> proxyIpList = new ArrayList<ProxyIp>();
      List<String> proxyIpTrList = html.xpath("//*[@id='list']/table/tbody/tr").all();
      if (proxyIpTrList != null && proxyIpTrList.size() > 0) {
         for (String trHtml : proxyIpTrList) {
            ProxyIp proxyIp = new ProxyIp();
            String ip = analyzeIp(trHtml);
            // ip地址
            proxyIp.setProxyIp(ip.substring(0, ip.indexOf(":")));
            // 端口号
            proxyIp.setProxyPort(Integer.parseInt(ip.substring(ip.indexOf(":") + 1, ip.length())));
            analyzeProxyIp(trHtml, proxyIp);
            proxyIpList.add(proxyIp);
         }
         page.putField("proxyIpList", proxyIpList);
      }
      page.addTargetRequests(urlList);
   }

   public static void main(String[] args) {
      Spider.create(new QuanWanProxyIpCrawler()).addUrl("http://www.goubanjia.com/").thread(5).run();
   }
   
   
   /**
    * 该网站ip每个数字分开，没有规律
    * 
    * @param trHtml
    * @return
    */
   private String analyzeIp(String trHtml) {
      trHtml = "<html><head></head><body><table>" + trHtml + "</table></body></html>";
      StringBuffer ip = new StringBuffer();
      if (StringUtils.isBlank(trHtml)) {
         return null;
      }
      List<Node> nodeList = Jsoup.parse(trHtml).getElementsByTag("td").get(0).childNodes();
      if (nodeList == null || nodeList.size() <= 0) {
         return null;
      }
      for (int i = 0; i < nodeList.size() - 1; i++) {
         Node node = nodeList.get(i);
         String nodeHtml = node.outerHtml();
         // display:none;不显示的
         if (StringUtils.isNotBlank(nodeHtml) && nodeHtml.contains("display:none;")
               || nodeHtml.contains("display: none;")) {
            continue;
         }
         String text = CommonUtils.simpleMatch(nodeHtml, ">\\s*(.*)\\s*</");
         ip.append(text);
      }
      String resultIp = ip.toString().replace("null", "").replace(" ", "").trim();
      String port = CommonUtils.simpleMatch(nodeList.get(nodeList.size() - 1).outerHtml(), ">\\s*(.*)\\s*</");
      resultIp = resultIp + ":" + port;
      return resultIp;
   }

   /**
    * 解析除了ip、port之外的属性
    * 
    * @param trHtml
    * @param proxyIp
    * @return
    */
   private void analyzeProxyIp(String trHtml, ProxyIp proxyIp) {
      trHtml = "<html><head></head><body><table>" + trHtml + "</table></body></html>";
      if (StringUtils.isBlank(trHtml)) {
         return;
      }
      try {
         Elements tdElements = Jsoup.parse(trHtml).select("tr > td");
         if (tdElements == null || tdElements.isEmpty()) {
            return;
         }
         // 匿名度
         String anonymity = tdElements.get(1).select("a").text();
         // 类型
         String proxyType = tdElements.get(2).select("a").text();
         // 地址
         StringBuffer ipAddress = new StringBuffer();
         Elements aElements = tdElements.get(3).select("a");
         for (Element a : aElements) {
            ipAddress.append(a.text());
         }
         // 响应速度
         String responseSpeed = tdElements.get(5).text();
         proxyIp.setAnonymity(anonymity);
         proxyIp.setProxyType(proxyType);
         proxyIp.setIpAddress(ipAddress.toString());
         proxyIp.setResponseSpeed(responseSpeed);
      } catch (Exception e) {
         logger.debug("解析失败",e);
      }
   }
}
 米扑代理ip端口是图片【采用百度orc】
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
        params.put("client_id", "xxxxx");
        params.put("client_secret", "xxxxx");
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
        return Base64Util.encode(data);
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(getAccessToken());
//        System.out.println(OCRVCode("G:ygrandimg.png"));
        System.out.println(OCRVCode("http://proxy.mimvp.com/common/ygrandimg.php?id=7&port=NmTiAmzvMpTI4"));
    }
}
```
运行如下（存储在redis）


