package com.daishu.gateway.base;

import org.springframework.core.Ordered;

public enum  GateWayOrder {
    LOG(Ordered.HIGHEST_PRECEDENCE),
    Redirect(-100),
    JWT(-1)
    ;

    GateWayOrder(Integer order) {
        this.order = order;
    }

    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}