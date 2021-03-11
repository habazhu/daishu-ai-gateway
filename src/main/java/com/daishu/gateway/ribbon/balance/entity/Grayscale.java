package com.daishu.gateway.ribbon.balance.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("灰度策略")
public class Grayscale {
    @ApiParam("优先级,值越小优先级越高,当出现了高优先级的负载均衡以后低优先级的就不再执行")
    private Integer order;
    @ApiParam("策略的唯一标识")
    private String id;
    @ApiParam("策略的ip标识,正则  consul中的address  ip或者ip+端口")
    private String ip;
    @ApiParam("header中studentId,正则,如果是*就代表所有")
    private String studentId;
    @ApiParam("请求的url,正则,如果是*就代表所有")
    private String url;
    @ApiParam("强制,true如果找不到对应ip就抛出异常,false:如果找不到对应的ip就随机返回一台 ")
    private boolean enforce=false;
    @ApiParam("权重0~100 当ip 有内容的时候本字段不生效")
    private Integer weight;
    @ApiParam("排他 如果之前的条件没有完全命中,那么就会执行exclusive过滤, 比如 trueStu 代表如果stu判断是true就会从服务列表摘除, falseStu代表如果stu是false就从列表摘除  ")
    private String exclusive;
    @ApiParam("版本号")
    private String ver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnforce() {
        return enforce;
    }

    public void setEnforce(boolean enforce) {
        this.enforce = enforce;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getExclusive() {
        return exclusive;
    }

    public void setExclusive(String exclusive) {
        this.exclusive = exclusive;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}