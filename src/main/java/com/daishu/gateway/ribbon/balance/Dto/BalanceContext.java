package com.daishu.gateway.ribbon.balance.Dto;

import com.netflix.loadbalancer.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.core.Ordered;

@Api("负载均衡上下文")
public class BalanceContext {
    @ApiParam("从请求中获得的参数")
    private  BalanceDto balanceDto;
    @ApiParam("目标server")
    private Server server;
    @ApiParam("当前优先级")
    private Integer order= Ordered.LOWEST_PRECEDENCE;
    @ApiParam("策略id")
    private String policyId;

    public BalanceContext(BalanceDto balanceDto) {
        this.balanceDto = balanceDto;
    }

    public BalanceDto getBalanceDto() {
        return balanceDto;
    }

    public void setBalanceDto(BalanceDto balanceDto) {
        this.balanceDto = balanceDto;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

}