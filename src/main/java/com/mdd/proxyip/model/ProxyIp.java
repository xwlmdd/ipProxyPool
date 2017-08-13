package com.mdd.proxyip.model;

import java.io.Serializable;
import java.util.Date;

public class ProxyIp implements Serializable{
    private Integer id;

    private String proxyIp;

    private Integer proxyPort;

    private String anonymity;

    private String proxyType;

    private String ipAddress;

    private String responseSpeed;

    private String aliveTime;

    private Date insertTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp == null ? null : proxyIp.trim();
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(String anonymity) {
        this.anonymity = anonymity == null ? null : anonymity.trim();
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType == null ? null : proxyType.trim();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getResponseSpeed() {
        return responseSpeed;
    }

    public void setResponseSpeed(String responseSpeed) {
        this.responseSpeed = responseSpeed == null ? null : responseSpeed.trim();
    }

    public String getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(String aliveTime) {
        this.aliveTime = aliveTime == null ? null : aliveTime.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}