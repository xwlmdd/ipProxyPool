package com.mdd.proxyip.crawler;

import com.mdd.proxyip.ProxyIp;
import com.mdd.proxyip.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

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
				System.out.println("---------"+ip);
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
