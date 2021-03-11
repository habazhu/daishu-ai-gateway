package com.daishu.gateway.ribbon.balance.Dto;

import com.netflix.loadbalancer.Server;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
@Api("负载均衡内容")
public class BalanceDto {
    @ApiParam("请求头")
    private HttpHeaders httpHeaders;
    @ApiParam("原始请求地址")
    private URI uri;
    @ApiParam("consul上的服务列表")
    private List<Server> reachableServers;
    public BalanceDto(@ApiParam("请求头")HttpHeaders httpHeaders, @ApiParam("原始请求地址")URI uri) {
        this.httpHeaders = httpHeaders;
        this.uri=uri;
    }


    @ApiParam("consul上的服务列表")
    private LinkedHashMap<String ,Server> reachableServerMap=new LinkedHashMap<>();
    @ApiParam("header中的studentId")
    private String studentId;
    @ApiParam("要访问的服务的id consul的注册标识")
    private String serviceId;
    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }


    public void setReachableServers(List<Server> reachableServers) {
        this.reachableServers = reachableServers;
    }

    public String getStudentId() {
        if(this.httpHeaders!=null&& this.httpHeaders.get("studentId")!=null){
            return this.httpHeaders.getFirst("studentId").toString();
        }
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @ApiParam("内部局部变量")
    private boolean initReachableServerMap=false;
    public LinkedHashMap<String, Server> getReachableServerMap() {
        if(!initReachableServerMap){
            for(Server server:reachableServers){
                reachableServerMap.put(server.getHostPort(),server);
            }
            initReachableServerMap=true;
        }
        return reachableServerMap;
    }

    public void setReachableServerMap(LinkedHashMap<String, Server> reachableServerMap) {
        this.reachableServerMap = reachableServerMap;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}