package com.mdd.proxyip.job;

import com.mdd.proxyip.crawler.KuaiProxyIpCrawler;
import com.mdd.proxyip.crawler.QuanWanProxyIpCrawler;
import com.mdd.proxyip.crawler.XiCiProxyIpCrawler;
import com.mdd.proxyip.pipeLine.DataPipeLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by xwl on 2017/8/13.
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
    private DataPipeLine dataPipeLine;

    @Scheduled(cron="0 50 23 * * ?")
    public void quanWanProxyIpCrawlerJob() {
        Spider.create(quanWanProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.goubanjia.com/").thread(5).run();
    }
    @Scheduled(cron="0 50 23 * * ?")
    public void xiCiProxyIpCrawlerJob() {
        Spider.create(xiCiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.xicidaili.com/").thread(5).run();
    }
    @Scheduled(cron="0 50 23 * * ?")
    public void kuaiProxyIpCrawlerJob() {
        Spider.create(kuaiProxyIpCrawler)
                .addPipeline(dataPipeLine)
                .addUrl("http://www.kuaidaili.com/free/inha/1/").thread(5).run();
    }



//@Scheduled 参数可以接受两种定时的设置，一种是我们常用的cron="*/6 * * * * ?",一种是 fixedRate = 6000，两种都表示每隔六秒打印一下内容。
//fixedRate 说明
//@Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
//@Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
//@Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次

}
