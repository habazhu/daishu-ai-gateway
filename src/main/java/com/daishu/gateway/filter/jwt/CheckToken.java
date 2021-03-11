package com.daishu.gateway.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.daishu.gateway.jedis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

@Api("校验token")
public abstract class CheckToken {
    @Autowired
    RedisUtil redisUtil;


    @ApiParam("查看是否有权限")
    public abstract boolean check(@ApiParam("url地址") String url, @ApiParam("token") String token, @ApiParam("秘钥") String secret, @ApiParam("发布者") String issuer);

    @ApiParam("HMAC256校验token")
    public DecodedJWT verifyToken(@ApiParam("token") String token, @ApiParam("秘钥") String secret, @ApiParam("发布者") String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }
}