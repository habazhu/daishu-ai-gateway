package com.daishu.gateway.base;

import java.util.List;

/**
 * 黑名单功能
 */
public class AccessBlackList {
    /**
     * 通过ip进行限制访问
     */
    private List<String> ipAddr;

    /**
     * 通过请求路径进行限制访问
     */
    private List<String> failurePath;

    public List<String> getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(List<String> ipAddr) {
        this.ipAddr = ipAddr;
    }

    public List<String> getFailurePath() {
        return failurePath;
    }

    public void setFailurePath(List<String> failurePath) {
        this.failurePath = failurePath;
    }
}
