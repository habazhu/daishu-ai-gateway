package com.daishu.gateway.filter.log;


import com.daishu.gateway.utils.EnvironmentUtils;
import com.daishu.gateway.utils.IpUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 日志实体类，方便后续接入ELK
 */
public class Log {
    private TYPE logType;
    private LEVEL level;
    private String appEnv;
    private String applicationName;
    private String hostName;
    private String ip;
    private Long handleTime;
    private String timeStamp;
    private String requestUrl;
    private String userName;
    private String account;
    private String requestBody;
    private String responseBody;
    private String requestId;
    private String requestMethod;
    private Integer status;
    private String serverIp;
    private String sessionId;
    private String _class;

    public Log() {
        this(TYPE.REQUEST);
    }

    public Log(TYPE logType) {
        this.logType = logType;
        this.applicationName = EnvironmentUtils.getAppEnv();
        this.hostName = IpUtils.getHostName();
        this.timeStamp = ZonedDateTime.now(ZoneOffset.of("+08:00")).toString();
        this.serverIp = IpUtils.getLocalIp();
    }

    /**
     * 日志级别枚举类
     */
    public static enum LEVEL {
        OFF,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE,
        ALL;

        private LEVEL() {
        }
    }

    /**
     * 日志类型枚举
     */
    public static enum TYPE {
        REQUEST,
        RESPONSE,
        OUT;

        private TYPE() {
        }
    }

    public TYPE getLogType() {
        return logType;
    }

    public void setLogType(TYPE logType) {
        this.logType = logType;
    }

    public LEVEL getLevel() {
        return level;
    }

    public void setLevel(LEVEL level) {
        this.level = level;
    }

    public String getAppEnv() {
        return appEnv;
    }

    public void setAppEnv(String appEnv) {
        this.appEnv = appEnv;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }
}
