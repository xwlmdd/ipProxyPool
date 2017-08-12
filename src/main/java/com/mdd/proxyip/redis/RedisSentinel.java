package com.mdd.proxyip.redis;

import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisSentinelPool;

@Component
public class RedisSentinel {

	private JedisSentinelPool  jedisSentinelPool;
	
	public void test(){
//		new JedisSentinelPool(masterName, sentinels)
	}
	
}
