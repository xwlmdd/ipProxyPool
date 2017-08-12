package com.mdd.proxyip.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author xwl redis缓存
 */
@Component
public class RedisCache {

	private static Logger logger = Logger.getLogger(RedisCache.class);

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 释放资源
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public void returnResource(Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 是否存在该key
	 * @param key
	 * @return
	 */
	public boolean isExist(String key) {
		boolean result = false;
		Jedis jedis = jedisPool.getResource();
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			returnResource(jedis);
		} finally {
			logger.error("该key:" + key + "不存在！");
			returnResource(jedis);
		}
		return result;

	}

	/*******************************************字符串*************************************/
	
	
	/**
	 * 是否存在该key
	 * @param key: byte[]
	 * @return
	 */
	public boolean isExist(byte[] key) {
		boolean result = false;
		Jedis jedis = jedisPool.getResource();
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			logger.error("该key:" + key + "不存在！");
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;

	}

	/**
	 * 根据key获取value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = "";
		try {
			value = jedis.get(key);
			return value;
		} catch (Exception e) {
			logger.error("获取key:" + key + "失败！", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return key;
	}

	/**
	 * 存对象，序列化对象
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("存储序列号对象失败！", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 存字符串
	 * @param key
	 * @param value
	 * @return
	 */
	public void setValue(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			System.out.println(jedis.set(key, value));
		} catch (Exception e) {
			logger.error("存储key：" + "失败！", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
	}

	/*******************************************List*************************************/
	
	
	
	
}
