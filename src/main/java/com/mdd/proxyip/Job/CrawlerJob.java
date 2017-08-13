package com.mdd.proxyip.Job;

import com.mdd.proxyip.crawler.KuaiProxyIpCrawler;
import com.mdd.proxyip.crawler.QuanWanProxyIpCrawler;
import com.mdd.proxyip.crawler.XiCiProxyIpCrawler;
import com.mdd.proxyip.pipeLine.DataPipeLine;
import com.mdd.proxyip.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import us.codecraft.webmagic.Spider;

/**
 * Created by Administrator on 2017/8/13.
 */
@Component
public class CrawlerJob {
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


    public void testQuanWanSaveProxyIpList(){
        Spider.create(quanWanProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.goubanjia.com/").thread(5).run();
    }

    public void testXiCiSaveProxyIpList(){
        Spider.create(xiCiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.xicidaili.com/").thread(5).run();
    }

    public void testKuaiSaveProxyIpList(){
        Spider.create(kuaiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.kuaidaili.com/free/inha/1/").thread(5).run();
    }

    public void testRedisCache(){
        System.out.println(redisCache.getValue("name"));
    }



}
