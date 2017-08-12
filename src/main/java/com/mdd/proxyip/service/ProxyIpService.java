package com.mdd.proxyip.service;

import com.mdd.proxyip.mapper.ProxyIpMapper;
import com.mdd.proxyip.ProxyIp;
import com.mdd.proxyip.redis.RedisCache;
import com.mdd.proxyip.utils.CheckIPUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProxyIpService {

	private static Logger logger = Logger.getLogger(ProxyIpService.class);

	@Autowired
	private ProxyIpMapper proxyIpMapper;

	@Autowired
	private RedisCache redisCache;

	/**
	 * mysql 存储
	 * 
	 * @param ProxyIpList
	 */
	public void saveProxyIpList(List<ProxyIp> ProxyIpList) {
		proxyIpMapper.saveProxyIpList(ProxyIpList);
	}

	/**
	 * redis 存储
	 * 
	 * @param ProxyIpList
	 */
	public void saveProxyListIpInRedis(List<ProxyIp> ProxyIpList) {
		for (ProxyIp proxyIp : ProxyIpList) {
			if (!CheckIPUtils.checkValidIP(proxyIp.getProxyIp(), proxyIp.getProxyPort())) {
				logger.info(proxyIp.getProxyIp() + ":" + proxyIp.getProxyPort() + "代理失效！");
				continue;
			}
			byte[] key = SerializationUtils.serialize("proxyip:" + proxyIp.getProxyIp() + ":" + proxyIp.getProxyPort());
			byte[] value = SerializationUtils.serialize(proxyIp);
			if (!redisCache.isExist(key)) {
				redisCache.setValue(key, value);
				logger.info(proxyIp.getProxyIp() + proxyIp.getProxyPort() + "已存入redis中！");
			} else {
				logger.info("key已存在！");
			}
		}
	}

}
