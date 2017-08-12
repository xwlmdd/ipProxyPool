package com.mdd.proxyip.crawler;

import java.util.ArrayList;
import java.util.List;

import com.mdd.proxyip.ProxyIp;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 热刺代理网站ip抓取
 * 
 * @author xwl 2017.6.3
 */
@Component
public class XiCiProxyIpCrawler implements PageProcessor {

	private Logger logger = Logger.getLogger(XiCiProxyIpCrawler.class);

	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1000)
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

	public Site getSite() {
		return site;
	}

	
	public void process(Page page) {
		Document html = page.getHtml().getDocument();
		// 结果集
		List<ProxyIp> proxyIpList = new ArrayList<ProxyIp>();
		Elements trElements = html.getElementById("ip_list").getElementsByTag("tr");
		for (Element trEle : trElements) {
			Elements tdElements = trEle.getElementsByTag("td");
			if (tdElements == null||tdElements.size()<=0) {
				continue;
			}
			try {
				ProxyIp proxyIp = new ProxyIp();
				String ip = tdElements.get(1).text();
				String proxyPort = tdElements.get(2).text();
				String ipAddress = tdElements.get(3).text();
				String anonymity = tdElements.get(4).text();
				String proxyType = tdElements.get(5).text();
				String aliveTime = tdElements.get(6).text();
				proxyIp.setProxyIp(ip);
				proxyIp.setProxyPort(Integer.parseInt(proxyPort));
				proxyIp.setAliveTime(aliveTime);
				proxyIp.setAnonymity(anonymity);
				proxyIp.setIpAddress(ipAddress);
				proxyIp.setProxyType(proxyType);
				logger.info(proxyIp.getProxyIp()+":"+proxyIp.getProxyPort());
				proxyIpList.add(proxyIp);
			} catch (Exception e) {
				logger.error("IP代理解析出错！", e);
			}
		}
		page.putField("proxyIpList", proxyIpList);
	}
}
