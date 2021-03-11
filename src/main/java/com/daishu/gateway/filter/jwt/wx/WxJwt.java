package com.daishu.gateway.filter.jwt.wx;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.daishu.gateway.filter.jwt.CheckToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Service;

@Api("微信系统")
@Service("wxJwt")
public class WxJwt extends CheckToken {
    @Override
    public boolean check(@ApiParam("url地址") String url, @ApiParam("token") String token, @ApiParam("秘钥") String secret, @ApiParam("发布者") String issuer) {
        //解析token
        Integer id = null;
        try {
            DecodedJWT decodedJWT = super.verifyToken(token, secret, issuer);
        } catch (Exception e) {
            //token 异常
            return false;
        }
        return true;
    }
}