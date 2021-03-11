package com.daishu.gateway.ribbon.balance.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty( matchIfMissing = true ,prefix = "mybalance",name="open",havingValue = "true" )
@Api("负载均衡")
@ConfigurationProperties(prefix = "mybalance")
public class MyBalanceEntity {
    @ApiParam("灰度")
    private List<Grayscale> grayscale;

    @Constructor
    @ApiParam("所有策略")
    public void sort() {
        if (grayscale != null && grayscale.size() > 1) {
            grayscale.sort((g1, g2) -> {
                if (g2.getOrder() > g1.getOrder()) return 1;
                if (g2.getOrder() < g1.getOrder()) return -1;
                return 0;
            });
        }
    }

    public List<Grayscale> getGrayscale() {
        return grayscale;
    }

    public void setGrayscale(List<Grayscale> grayscale) {
        this.grayscale = grayscale;
    }
}