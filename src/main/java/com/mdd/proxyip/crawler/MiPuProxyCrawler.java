package com.mdd.proxyip.crawler;

import com.mdd.proxyip.login.MiPuCrawlerLogin;
import com.mdd.proxyip.model.ProxyIp;
import com.mdd.proxyip.utils.CommonUtils;
import com.mdd.proxyip.utils.HttpClientUtils;
import com.mdd.proxyip.utils.HttpRequestData;
import com.mdd.proxyip.utils.VCodeCheckUtils;
import org.apache.commons.lang3.StringUtils;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 云代理网站ip抓取
 *
 * @author xwl 2017.6.3
 */
public class MiPuProxyCrawler implements PageProcessor {
    private Logger logger = Logger.getLogger(MiPuProxyCrawler.class);

    //    @Autowired
    private MiPuCrawlerLogin miPuCrawlerLogin = new MiPuCrawlerLogin();
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = initSite();

    private Site initSite() {
        Site site = Site.me();
        HttpResponse httpResponse = miPuCrawlerLogin.login();
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            logger.error("米扑代理登录失败！");
            return null;
        }
        for (Cookie cookie : HttpClientUtils.cookieStore.getCookies()) {
            site.addCookie(cookie.getName(), cookie.getValue());
        }
        site.setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1000);
        return site;
    }


    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        String html = page.getHtml().get();
        Document document = Jsoup.parse(html);
        Elements trs = document.select("#mimvp-body > div.free-content > div > table > tbody > tr");
        if (trs == null) {
            return;
        }
        List<ProxyIp> proxyIpList = new ArrayList<ProxyIp>();
        for (Element ele : trs) {
            Elements tds = ele.select("td");
            if (tds == null) {
                continue;
            }
            ProxyIp proxyIp = new ProxyIp();
            //ip
            String ip = tds.get(1).text();
            proxyIp.setProxyIp(ip);
            //端口
            String imageUrl = tds.get(2).select("img").attr("src");
            imageUrl = "http://proxy.mimvp.com/" + imageUrl;
            String port = null;
            port = VCodeCheckUtils.OCRVCode(imageUrl);
            proxyIp.setProxyPort(Integer.parseInt(port));
            //协议
            String proxyType = tds.get(3).text();
            proxyIp.setProxyType(proxyType);
            //匿名
            String anonymity = tds.get(4).text();
            proxyIp.setAnonymity(anonymity);
            //地址
            String ipAddress = tds.get(5).text();
            proxyIp.setIpAddress(ipAddress);
            //速度
            String speed = tds.get(7).text();
            proxyIp.setResponseSpeed(speed);
            proxyIpList.add(proxyIp);
        }
        page.putField("proxyIpList", proxyIpList);
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new MiPuProxyCrawler());
        spider.addUrl("http://proxy.mimvp.com/free.php?proxy=in_tp&sort=&page=1").thread(1).run();
    }


}
