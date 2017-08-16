package com.mdd.proxyip;

import com.mdd.proxyip.job.CrawlerJob;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= CrawlerJob.class)
@SpringBootApplication
@MapperScan("com.mdd.proxyip.mapper")
public class IpProxyPoolApplicationTests {

    @Autowired
    private CrawlerJob crawlerJob;

    @Test
    public void testQuanWanSaveProxyIpList() {
        crawlerJob.testQuanWanSaveProxyIpList();
    }

    @Test
    public void testXiCiSaveProxyIpList() {
        crawlerJob.testXiCiSaveProxyIpList();
    }

    @Test
    public void testKuaiSaveProxyIpList() {
       crawlerJob.testKuaiSaveProxyIpList();
    }


}
