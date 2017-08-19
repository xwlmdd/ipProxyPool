package com.mdd.proxyip.crawler;

import com.mdd.proxyip.login.MiPuCrawlerLogin;
import com.mdd.proxyip.utils.CommonUtils;
import com.mdd.proxyip.utils.HttpClientUtils;
import com.mdd.proxyip.utils.HttpRequestData;
import org.apache.commons.lang3.StringUtils;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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

    private Site initSite(){
        Site site = Site.me();
        HttpResponse httpResponse = miPuCrawlerLogin.login();
        if (httpResponse.getStatusLine().getStatusCode()!=200){
            logger.error("米扑代理登录失败！");
            return null;
        }
        for (Cookie cookie : HttpClientUtils.cookieStore.getCookies()) {
            site.addCookie(cookie.getName(),cookie.getValue());
        }
        site.setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1000);
        return site;
    }


	public Site getSite() {
        return site;
	}

	public void process(Page page) {
        System.out.println("------------"+page.getStatusCode());
        String html = page.getHtml().get();
		System.out.println(html);
	}

	public static void main(String[] args) {
        Spider spider = Spider.create(new MiPuProxyCrawler());
        spider.addUrl("http://proxy.mimvp.com/free.php?proxy=in_tp&sort=&page=1").thread(1).run();
	}



}
