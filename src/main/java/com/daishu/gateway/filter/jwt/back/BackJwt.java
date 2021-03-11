package com.daishu.gateway.filter.jwt.back;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.daishu.gateway.base.DsException;
import com.daishu.gateway.filter.jwt.CheckToken;
import com.daishu.gateway.filter.jwt.back.permission.ZxxbSysPermissoinService;
import com.daishu.gateway.filter.jwt.back.permission.impl.model.ZxxbSysPermissionModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Api("业务系统Jwt")
@Service("backJwt")
public class BackJwt extends CheckToken {
    @Autowired
    private ZxxbSysPermissoinService zxxbSysPermissoinService;

    @Override
    public boolean check(@ApiParam("url地址") String url, @ApiParam("token") String token, @ApiParam("秘钥") String secret, @ApiParam("发布者") String issuer) {
        //解析token
        Integer id = null;
        try {
            DecodedJWT decodedJWT = super.verifyToken(token, secret, issuer);
            Map<String, Claim> claims = decodedJWT.getClaims();
            Claim claim = claims.get("user");
            Map<String, Object> map = claim.asMap();
            id = Integer.parseInt(map.get("id").toString());
        } catch (Exception e) {
            //token 异常
            throw new DsException(9999);
        }
        try {
            ZxxbSysPermissionModel zxxbSysPermissionModel = zxxbSysPermissoinService.queryPhpSystemAuthorityUrl(id);
            if (zxxbSysPermissionModel.getUrlRoleEnum().equals(ZxxbSysPermissionModel.UrlRoleEnum.ALL)) {
                return true;
            } else {
                String[] ary = url.toLowerCase().split("/");
                String lowUrl = ary[0] + "/" + ary[1] + "/" + ary[2] ;
                if (zxxbSysPermissionModel.getServerUrl().contains(url.toLowerCase()) || zxxbSysPermissionModel.getServerUrl().contains(lowUrl+ "/**") ||  zxxbSysPermissionModel.getServerUrl().contains(lowUrl+ "/*")) {
                    return true;
                } else {
                    throw new DsException(9998);
                }
            }
        } catch (Exception e) {
            //如果调用业务系统异常
            throw new DsException(9998);
        }
    }
}