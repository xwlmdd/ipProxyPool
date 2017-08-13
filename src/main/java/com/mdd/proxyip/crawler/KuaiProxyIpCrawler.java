package com.mdd.proxyip.crawler;

import com.mdd.proxyip.model.ProxyIp;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 热刺代理网站ip抓取
 * 
 * @author xwl 2017.6.3
 */
@Component
public class KuaiProxyIpCrawler implements PageProcessor {

	private Logger logger = Logger.getLogger(KuaiProxyIpCrawler.class);

	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(3000)
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

	@Override
	public void process(Page page) {
		String html = page.getHtml().toString();
		// 结果集
		List<ProxyIp> proxyIpList = new ArrayList<ProxyIp>();
		Elements trElements = Jsoup.parse(html).select("#list > table > tbody > tr");
		for (Element trEle : trElements) {
			Elements tdElements = trEle.getElementsByTag("td");
			if (tdElements == null||tdElements.size()<=0) {
				continue;
			}
			try {
				ProxyIp proxyIp = new ProxyIp();
				String ip = tdElements.get(0).text();
				String proxyPort = tdElements.get(1).text();
				String anonymity = tdElements.get(2).text();
				String proxyType = tdElements.get(3).text();
				String ipAddress = tdElements.get(4).text();
				String responseSpeed = tdElements.get(5).text();
				proxyIp.setProxyIp(ip);
				proxyIp.setProxyPort(Integer.parseInt(proxyPort));
				proxyIp.setResponseSpeed(responseSpeed);
				proxyIp.setAnonymity(anonymity);
				proxyIp.setIpAddress(ipAddress);
				proxyIp.setProxyType(proxyType);
				logger.info(proxyIp.getProxyIp()+":"+proxyIp.getProxyPort());
				proxyIpList.add(proxyIp);
			} catch (Exception e) {
				logger.error("IP代理解析出错！", e);
			}
		}
		page.addTargetRequest("http://www.kuaidaili.com/free/inha/2/");
		page.addTargetRequest("http://www.kuaidaili.com/free/intr/");
		page.addTargetRequest("http://www.kuaidaili.com/free/intr/2/");
		page.addTargetRequest("http://www.kuaidaili.com/free/outha/");
		page.addTargetRequest("http://www.kuaidaili.com/free/outtr/");
		page.addTargetRequest("http://www.kuaidaili.com/free/outtr/2/");
		page.putField("proxyIpList", proxyIpList);

	}

	public Site getSite() {
		return site;
	}

}
