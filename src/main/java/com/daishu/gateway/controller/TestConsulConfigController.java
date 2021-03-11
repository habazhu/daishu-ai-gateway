package com.daishu.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.daishu.gateway.base.DefineEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testConsulConfigController")
public class TestConsulConfigController {
    private final Log log = LogFactory.getLog(TestConsulConfigController.class);

    @Autowired
    private DefineEntity defineEntity;

    @GetMapping("getCurrentConfig")
    public String getCurrentConfig() {
        log.info("获得当前consul信息");
        return JSONObject.toJSONString(defineEntity);
    }
}
