package com.mdd.proxyip;

import com.mdd.proxyip.crawler.KuaiProxyIpCrawler;
import com.mdd.proxyip.crawler.QuanWanProxyIpCrawler;
import com.mdd.proxyip.crawler.XiCiProxyIpCrawler;
import com.mdd.proxyip.pipeLine.DataPipeLine;
import com.mdd.proxyip.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import us.codecraft.webmagic.Spider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IpProxyPoolApplicationTests {


    @Autowired
    private QuanWanProxyIpCrawler quanWanProxyIpCrawler;

    @Autowired
    private XiCiProxyIpCrawler xiCiProxyIpCrawler;

    @Autowired
    private KuaiProxyIpCrawler kuaiProxyIpCrawler;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DataPipeLine dataPipeLine;

    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    @Test
    public void testQuanWanSaveProxyIpList() {
        Spider.create(quanWanProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.goubanjia.com/").thread(5).run();
    }

    @Test
    public void testXiCiSaveProxyIpList() {
        Spider.create(xiCiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.xicidaili.com/").thread(5).run();
    }

    @Test
    public void testKuaiSaveProxyIpList() {
        Spider.create(kuaiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.kuaidaili.com/free/inha/1/").thread(5).run();
    }

    @Test
    public void testRedisCache() {
        System.out.println(redisCache.getValue("name"));
    }

    @Test
    public void testRedisSentinel() {
        Jedis redis = jedisSentinelPool.getResource();
        String name = redis.get("name");
        System.out.println(name);
    }
}
