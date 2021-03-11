package com.daishu.gateway.filter.jwt.back.permission;

import com.daishu.gateway.filter.jwt.back.permission.impl.model.ZxxbSysPermissionModel;
import io.swagger.annotations.ApiParam;

import java.io.IOException;

public interface ZxxbSysPermissoinService {
    @ApiParam("查看业务系统访问权限")
    ZxxbSysPermissionModel queryPhpSystemAuthorityUrl(Integer staffId) throws IOException;
}