package com.daishu.gateway.base;

import io.swagger.annotations.ApiParam;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
@ConfigurationProperties(prefix = "youyou.jwt")
public class DefineEntity {
    @ApiParam("上帝模式")
    private boolean god;
    @ApiParam("token缓存时间")
    private Integer tokenCachime;
    @ApiParam("配置文件环境")
    private String active;

    @ApiParam("不校验的接口")
    Map<String, String> excludePatterns ;
    @ApiParam("免鉴权接口")
    Map<String, String> excludePath;
    @ApiParam("需要鉴权的接口")
    Map<String, String> includePatterns;
    @ApiParam("需要鉴权的接口")
    Map<String,String> includePath;


    @ApiParam("业务系统权限接口")
    private String phpSystemAuthorityUrl;
    @ApiParam("业务系统地址")
    private String phpSystemUrl;

    @ApiParam("业务系统秘钥")
    private String platform;
    @ApiParam("业务系统签名")
    private String platformIssuer;

    @ApiParam("学生系统秘钥")
    private String student;
    @ApiParam("学生系统签名")
    private String stuTokenIssuer;

    @ApiParam("教师系统秘钥")
    private String teacher;
    @ApiParam("微信系统秘钥")
    private String wxapi;

    @ApiParam("黑名单")
    private AccessBlackList accessBlackList;

    public AccessBlackList getAccessBlackList() {
        return accessBlackList;
    }

    public void setAccessBlackList(AccessBlackList accessBlackList) {
        this.accessBlackList = accessBlackList;
    }

    public Map<String, String> getExcludePatterns() {
        return excludePatterns;
    }

    public void setExcludePatterns(Map<String, String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public Map<String, String> getIncludePatterns() {
        return includePatterns;
    }

    public void setIncludePatterns(Map<String, String> includePatterns) {
        this.includePatterns = includePatterns;
    }

    public String getPhpSystemAuthorityUrl() {
        return phpSystemAuthorityUrl;
    }

    public void setPhpSystemAuthorityUrl(String phpSystemAuthorityUrl) {
        this.phpSystemAuthorityUrl = phpSystemAuthorityUrl;
    }

    public String getPhpSystemUrl() {
        return phpSystemUrl;
    }

    public void setPhpSystemUrl(String phpSystemUrl) {
        this.phpSystemUrl = phpSystemUrl;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWxapi() {
        return wxapi;
    }

    public void setWxapi(String wxapi) {
        this.wxapi = wxapi;
    }

    public String getPlatformIssuer() {
        return platformIssuer;
    }

    public void setPlatformIssuer(String platformIssuer) {
        this.platformIssuer = platformIssuer;
    }

    public String getStuTokenIssuer() {
        return stuTokenIssuer;
    }

    public void setStuTokenIssuer(String stuTokenIssuer) {
        this.stuTokenIssuer = stuTokenIssuer;
    }

    public Map<String, String> getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(Map<String, String> excludePath) {
        this.excludePath = excludePath;
    }

    public Map<String, String> getIncludePath() {
        return includePath;
    }

    public void setIncludePath(Map<String, String> includePath) {
        this.includePath = includePath;
    }

    public boolean isGod() {
        return god;
    }

    public void setGod(boolean god) {
        this.god = god;
    }

    public Integer getTokenCachime() {
        return tokenCachime;
    }

    public void setTokenCachime(Integer tokenCachime) {
        this.tokenCachime = tokenCachime;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}