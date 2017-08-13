package com.mdd.proxyip.utils;

import java.io.IOException;

import com.mdd.proxyip.model.ProxyIp;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 * ip代理工具类
 * 
 * @author xwl
 */
public class ProxyIpUtils {

	private static Logger logger = Logger.getLogger(ProxyIpUtils.class);

	// 测试地址
	private static String reqUrl = "http://www.xicidaili.com/";

	/**
	 * 检测代理ip是否有效
	 * 
	 * @param proxyIp
	 * @return
	 */
	public static boolean checkProxyIpUsable(ProxyIp proxyIp) {
		if (proxyIp == null) {
			return false;
		}
		String proxyHost = proxyIp.getProxyIp();
		int proxyPort = proxyIp.getProxyPort();
		if (StringUtils.isBlank(proxyHost) || StringUtils.isBlank(proxyPort + "")) {
			return false;
		}
		int statusCode = 0;
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;
		try {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			httpclient = HttpClients.custom().setProxy(proxy).build();
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
					.setConnectionRequestTimeout(5000).build();
			httpGet = new HttpGet(reqUrl);
			httpGet.setConfig(defaultRequestConfig);
			response = httpclient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			System.out.println(response);
			if (statusCode == 200) {
				return true;
			}
		} catch (Exception e) {
			logger.error("连接超时！");
			return false;
		} finally {
			if (response != null) {
				try {
					response.close();
					httpGet.releaseConnection();
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ProxyIp p = new ProxyIp();
		// p.setProxyIp("60.169.5.32");
		// p.setProxyPort(80);

		p.setProxyIp("123.152.42.222");
		p.setProxyPort(8118);
		System.out.println(checkProxyIpUsable(p));
	}

}
