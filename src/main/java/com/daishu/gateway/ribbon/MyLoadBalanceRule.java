package com.daishu.gateway.ribbon;

import com.alibaba.fastjson.JSONObject;
import com.daishu.gateway.DaishuCloudGatewayApplication;
import com.daishu.gateway.ribbon.balance.Balance;
import com.daishu.gateway.ribbon.balance.Dto.BalanceContext;
import com.daishu.gateway.ribbon.balance.Dto.BalanceDto;
import com.daishu.gateway.ribbon.balance.entity.MyBalanceEntity;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/*
https://blog.csdn.net/zhou1124/article/details/103773835
 */
@Component
@Api("路由规则")
public class MyLoadBalanceRule extends BestAvailableRule {
    private static Logger log = LoggerFactory.getLogger(MyLoadBalanceRule.class);
    @Autowired
    @Qualifier("grayscaleBalance")
    private Balance grayscaleBalance;

    public static ThreadLocal<String> originHost=new ThreadLocal<>();
    public static  String originHostHeader="originHost";
    @Autowired
    private MyBalanceEntity myBalanceEntity;
    public Server choose(ILoadBalancer lb, Object key) {
        //log.info("步骤2"+key);
        if (lb == null) {
            log.error("MyLoadBalanceRule Exception no load balancer");
            return null;
        }
        if(myBalanceEntity==null||myBalanceEntity.getGrayscale()==null||myBalanceEntity.getGrayscale().size()==0){
            return  grayscaleBalance.loadRandomServer(lb.getReachableServers());
        }
        BalanceDto balanceDto=(BalanceDto) key;
        //consul 上的注册192.168.0.225:9091 192.168.0.225:9092 consul中服务对应的address项目
        List<Server> reachableServers = lb.getReachableServers();
        if(reachableServers==null ||reachableServers.size()==0){
            log.error("MyLoadBalanceRule Exception 没有可用的服务");
            return null;
        }

        //解决consul先挂载但是服务不可用的问题
        balanceDto.setReachableServers(reachableServers);
        //reachableServers.forEach(info -> log.info(info.toString()));
        //balanceDto.setReachableServers(safetyProtection(reachableServers));
        BalanceContext balanceContext=new BalanceContext(balanceDto);
        //进行负载均衡
        grayscaleBalance.chooseServer(balanceContext);
        //log.info("balanceContext:内容 " + JSONObject.toJSONString(balanceContext));
        return  balanceContext.getServer();

    }
    @Autowired
    SpringClientFactory springClientFactory;
    @Override
    public Server choose(Object key) {
        //变更为从spring工厂实时获取,抛弃原有负载,解决consul的负载bug
        BalanceDto balanceDto=(BalanceDto) key;
        ILoadBalancer balance=springClientFactory.getLoadBalancer(balanceDto.getServiceId());
        return choose(balance,key);
       //默认负载服务 return choose(getLoadBalancer(), key);
    }
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub
        //log.info("-----");
    }


    //-----------------------以下内容为了解决启动时先挂载到了consul但是服务不可用状态的问题-------------------------------------------------------------
    @ApiParam("有效的服务列表")
    public static ConcurrentHashMap<Integer, EffectiveServer> effectiveServerMap=new ConcurrentHashMap();

    @ApiParam("服务列表,安全保护模式")
    private List<Server> safetyProtection(List<Server> serverList){
        if(serverList==null||serverList.size()==0){
            return serverList;
        }
        Integer key=serverList.get(0).getPort();
        //如果是首次启动
        if(effectiveServerMap.get(key)==null||effectiveServerMap.get(key).getServerSize()==0 ){
            EffectiveServer effectiveServer=new EffectiveServer();
            effectiveServer.setModifyTime(new Date().getTime());
            effectiveServer.setPort(key);
            effectiveServer.setServerList(serverList);
            effectiveServer.setServerSize(serverList.size());
            effectiveServerMap.put(key,effectiveServer);
            return serverList;
        }
        EffectiveServer effectiveServer=effectiveServerMap.get(key);
        //如果没有上下线
        if(serverList.size()==effectiveServer.getServerSize() ){
            return serverList;
        }
        //如果有下线
        if(serverList.size()< effectiveServer.getServerSize()){
            effectiveServer.setServerList(serverList);
            effectiveServer.setServerSize(serverList.size());
            return serverList;
        }
        //如果有服务上线并且没有做状态标记
        if(serverList.size()>effectiveServer.getServerSize()&&effectiveServer.getServerStatus()==0){
            effectiveServer.setServerStatus(1);
            effectiveServer.setModifyTime(new Date().getTime());
            return effectiveServer.getServerList();
        }
        //如果有服务上线并且超过10秒钟就认为已经挂载完成
        if(serverList.size()>effectiveServer.getServerSize()&&effectiveServer.getServerStatus()==1&&new Date().getTime()-effectiveServer.getModifyTime()>1000000){
            effectiveServer.setServerStatus(0);
            effectiveServer.setModifyTime(new Date().getTime());
            effectiveServer.setServerList(serverList);
            effectiveServer.setServerSize(serverList.size());
            return serverList;
        }else{
            return  effectiveServer.getServerList();
        }
    }

    @Api("有效服务类")
    class EffectiveServer{
        @ApiParam("端口")
        private Integer port;
        @ApiParam("服务列表")
        private List<Server> serverList;
        @ApiParam("更新服务时间")
        private Long modifyTime;
        @ApiParam("服务状态,1 有新服务上线中, 0 服务正常 -1 服务下线中")
        private Integer serverStatus=0;

        @ApiParam("服务列表数量")
        private Integer serverSize=0;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public List<Server> getServerList() {
            return serverList;
        }

        public void setServerList(List<Server> serverList) {
            this.serverList = serverList;
        }

        public Long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public Integer getServerSize() {
            return serverSize;
        }

        public void setServerSize(Integer serverSize) {
            this.serverSize = serverSize;
        }

        public Integer getServerStatus() {
            return serverStatus;
        }

        public void setServerStatus(Integer serverStatus) {
            this.serverStatus = serverStatus;
        }
    }

}