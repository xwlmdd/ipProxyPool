package com.mdd.proxyip.crawler;

import com.mdd.proxyip.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 云代理网站ip抓取
 * 
 * @author xwl 2017.6.3
 */
public class YunProxyCrawler implements PageProcessor {

	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(3)
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setDomain("www.ip3366.net")
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0")
			.addHeader("Referer", "http://www.ip3366.net/")
			.addHeader("Accept-Encoding", "gzip, deflate")
			.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
			.addHeader("Connection", "keep-alive")
			.addHeader("Upgrade-Insecure-Requests", "1")
			.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//			.addCookie("UM_distinctid=1", "5c7db8069170-05e20c35a51c048-46524130-100200-15c7db806af1f9")
//			.addCookie("safedog-flow-item", "01582A2F6D7F69AE7CA124F779C399B8")
//			.addCookie("CNZZDATA1256284042", "2131148624-1496757874-%7C1496757874")
			;
	public Site getSite() {
		
		return site;
	}

	public void process(Page page) {
		
		String html = page.getHtml().get();
		System.out.println(html);
		String nextPage = CommonUtils.simpleMatch(html,"<a href=\"(.*?)\">下一页</a>");
		System.out.println(nextPage);
		if(StringUtils.isNotBlank(nextPage)){
			page.addTargetRequest(nextPage);
		}
	}

	public static void main(String[] args) {
		Spider.create(new YunProxyCrawler()).addUrl("http://www.ip3366.net/?stype=1&page=6").thread(1).run();
//		http://api.share.baidu.com/s.gif?l=http://www.ip3366.net/?stype=1&page=8
	}

}
