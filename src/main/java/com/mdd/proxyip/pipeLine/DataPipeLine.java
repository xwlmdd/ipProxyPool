package com.mdd.proxyip.pipeLine;

import com.mdd.proxyip.ProxyIp;
import com.mdd.proxyip.service.ProxyIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class DataPipeLine implements Pipeline {

	@Autowired
	private ProxyIpService proxyIpService;

	/**
	 * mysql 存储
	 */
	/*
	 * public void process(ResultItems resultItems, Task task) {
	 * List<ProxyIp>proxyIpList = resultItems.get("proxyIpList");
	 * if(proxyIpList!=null&&!proxyIpList.isEmpty()){
	 * proxyIpService.saveProxyIpList(proxyIpList); }
	 * 
	 * }
	 */

	/**
	 * redis 存储
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<ProxyIp> proxyIpList = resultItems.get("proxyIpList");
		if (proxyIpList != null && !proxyIpList.isEmpty()) {
			proxyIpService.saveProxyListIpInRedis(proxyIpList);
		}

	}

}
