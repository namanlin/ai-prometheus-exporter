package com.ai.prometheus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "srv")
@Component
public class SrvConfig {
    private String accesskey;
    private String securityKey;

    // for mq
    private String mqPlatform;
    private String mqUrl;
    private String mqRegionId;
    private String mqConsumerId;
    private String mqTopicDetail;

    // for edas
    private String edasUrl;
    private String edasLogUrl;
    private String edasAction;
    private String edasLogIndex;
    private String edasLogTimeRange;
    private String edasLogMessageKey;
    private String edasLogLevel;

    // for rdsList
    private List<Map<String, Object>> rdsList = new ArrayList<>();

    public List<Map<String, Object>> getRdsList() {
        return rdsList;
    }

    public void setRdsList(List<Map<String, Object>> rdsList) {
        this.rdsList = rdsList;
    }

    // for redisList
    private List<Map<String, Object>> redisList = new ArrayList<>();

    public List<Map<String, Object>> getRedisList() {
        return redisList;
    }

    public void setRedisList(List<Map<String, Object>> redisList) {
        this.redisList = redisList;
    }

    // for slbList
    private List<Map<String, Object>> slbList = new ArrayList<>();

    public List<Map<String, Object>> getSlbList() {
        return slbList;
    }

    public void setSlbList(List<Map<String, Object>> slbList) {
        this.slbList = slbList;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getMqPlatform() {
        return mqPlatform;
    }

    public void setMqPlatform(String mqPlatform) {
        this.mqPlatform = mqPlatform;
    }

    public String getMqUrl() {
        return mqUrl;
    }

    public void setMqUrl(String mqUrl) {
        this.mqUrl = mqUrl;
    }

    public String getMqRegionId() {
        return mqRegionId;
    }

    public void setMqRegionId(String mqRegionId) {
        this.mqRegionId = mqRegionId;
    }

    public String getMqConsumerId() {
        return mqConsumerId;
    }

    public void setMqConsumerId(String mqConsumerId) {
        this.mqConsumerId = mqConsumerId;
    }

    public String getMqTopicDetail() {
        return mqTopicDetail;
    }

    public void setMqTopicDetail(String mqTopicDetail) {
        this.mqTopicDetail = mqTopicDetail;
    }

    public String getEdasUrl() {
        return edasUrl;
    }

    public void setEdasUrl(String edasUrl) {
        this.edasUrl = edasUrl;
    }

    public String getEdasLogUrl() {
        return edasLogUrl;
    }

    public void setEdasLogUrl(String edasLogUrl) {
        this.edasLogUrl = edasLogUrl;
    }

    public String getEdasAction() {
        return edasAction;
    }

    public void setEdasAction(String edasAction) {
        this.edasAction = edasAction;
    }

    public String getEdasLogIndex() {
        return edasLogIndex;
    }

    public void setEdasLogIndex(String edasLogIndex) {
        this.edasLogIndex = edasLogIndex;
    }

    public String getEdasLogTimeRange() {
        return edasLogTimeRange;
    }

    public void setEdasLogTimeRange(String edasLogTimeRange) {
        this.edasLogTimeRange = edasLogTimeRange;
    }

    public String getEdasLogMessageKey() {
        return edasLogMessageKey;
    }

    public void setEdasLogMessageKey(String edasLogMessageKey) {
        this.edasLogMessageKey = edasLogMessageKey;
    }

    public String getEdasLogLevel() {
        return edasLogLevel;
    }

    public void setEdasLogLevel(String edasLogLevel) {
        this.edasLogLevel = edasLogLevel;
    }

}
