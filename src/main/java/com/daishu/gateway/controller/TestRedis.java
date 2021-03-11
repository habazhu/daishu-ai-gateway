package com.daishu.gateway.controller;

import com.daishu.gateway.jedis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("testRedis")
@RestController
public class TestRedis {
    @Autowired
    private RedisUtil redisUtil;
    @RequestMapping("getKey")
    public String getKey(String key){
        return redisUtil.get(key);
    }
    @RequestMapping("setMessage")
    public String setMessage(String key,String value){
        redisUtil.set(key,value);
        return redisUtil.get(key);
    }
}
