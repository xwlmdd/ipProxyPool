package com.mdd.proxyip.mapper;

import com.mdd.proxyip.model.ProxyIp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProxyIpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProxyIp record);

    int insertSelective(ProxyIp record);

    ProxyIp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProxyIp record);

    int updateByPrimaryKey(ProxyIp record);

    void saveProxyIpList(List<ProxyIp> ProxyIpList);
}