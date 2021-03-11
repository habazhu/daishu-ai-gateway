package com.daishu.gateway.ribbon.balance.grayscale;

import com.alibaba.fastjson.JSONObject;
import com.daishu.gateway.DaishuCloudGatewayApplication;
import com.daishu.gateway.ribbon.balance.Balance;
import com.daishu.gateway.ribbon.balance.Dto.BalanceContext;
import com.daishu.gateway.ribbon.balance.entity.Grayscale;
import com.daishu.gateway.ribbon.balance.entity.MyBalanceEntity;
import com.daishu.gateway.utils.DsStringUtil;
import com.netflix.loadbalancer.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api("灰度负载均衡")
@Component
public class GrayscaleBalance extends Balance {
    private static Logger log = LoggerFactory.getLogger(GrayscaleBalance.class);

    @Autowired
    private MyBalanceEntity myBalanceEntity;

    @Override
    public void chooseServer(BalanceContext balanceContext) {
        if (myBalanceEntity.getGrayscale() != null && myBalanceEntity.getGrayscale().size() > 0) {
            for (Grayscale grayscale : myBalanceEntity.getGrayscale()) {
                //如果还没有产生有效策略
                if (balanceContext.getServer() == null) {
                    //根据策略加载服务到context
                    super.loadServer(grayscale, balanceContext);
                } else {
                    //因为本身就已经进行过
                    break;
                }
            }
            //如果所有的都执行完了还没有拿到有效的策略
            if (balanceContext.getServer() == null) {
                super.loadRandomServer(balanceContext);
            }
        }
    }

    @ApiParam("随机返回一台服务")
    @Override
    public Server loadRandomServer(List<Server> serverList) {
        List<Server>  l=new ArrayList();
        for(Server s:serverList){
            if(s.isAlive()){
                l.add(s);
            }else {
               // log.info("发现不可用服务"+ JSONObject.toJSONString(s));
            }
        }
        if(l.size()==0){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(l.size());
        Server server=serverList.get(index);
        //如果服务不可用了,就将内容强制打在第一台可用的上面
        return server;
    }

}