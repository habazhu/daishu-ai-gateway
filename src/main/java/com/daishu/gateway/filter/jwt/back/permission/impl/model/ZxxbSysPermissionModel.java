package com.daishu.gateway.filter.jwt.back.permission.impl.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import java.util.HashSet;
import java.util.Set;

public class ZxxbSysPermissionModel {
    @ApiParam("url权限")
    private UrlRoleEnum urlRoleEnum= UrlRoleEnum.Part;
    @ApiParam("服务器接口路径")
    private Set<String> serverUrl=new HashSet();


    public UrlRoleEnum getUrlRoleEnum() {
        return urlRoleEnum;
    }

    public void setUrlRoleEnum(UrlRoleEnum urlRoleEnum) {
        this.urlRoleEnum = urlRoleEnum;
    }

    public Set<String> getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(Set<String> serverUrl) {
        this.serverUrl = serverUrl;
    }



    @ApiModel("Admin 超级管理员 SubAdmin 管理员 User 普通用户 ")
    public enum UrlRoleEnum {
        ALL,Part
    }
}