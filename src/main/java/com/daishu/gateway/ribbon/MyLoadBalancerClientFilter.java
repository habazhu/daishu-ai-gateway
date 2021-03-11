package com.daishu.gateway.ribbon;

import com.daishu.gateway.ribbon.balance.Dto.BalanceDto;
import com.daishu.gateway.ribbon.balance.entity.MyBalanceEntity;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * https://blog.csdn.net/zhou1124/article/details/103773835
 */
@Api("负载均衡,先执行MyLoadBalancerClientFilter,再执行MyLoadBalanceRule")
@Component
public class MyLoadBalancerClientFilter extends LoadBalancerClientFilter {
   // public static ThreadLocal<ServerWebExchange> exchange = new ThreadLocal<>();
    private static Logger log = LoggerFactory.getLogger(MyLoadBalancerClientFilter.class);

    @Autowired
    private MyBalanceEntity myBalanceEntity;
    public MyLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        //如果没有任何策略就使用
        if(myBalanceEntity==null||myBalanceEntity.getGrayscale()==null||myBalanceEntity.getGrayscale().size()==0){
            return super.choose(exchange);
        }
        //这里可以拿到web请求的上下文，可以从header中取出来自己定义的数据。
        //MyLoadBalancerClientFilter.exchange.set(exchange);
        //获得真实的请求路径lb://statistics/testServer/queryPort
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();

        BalanceDto balanceDto=new BalanceDto(httpHeaders,uri);
        balanceDto.setHttpHeaders(httpHeaders);
        balanceDto.setUri(uri);

        //log.info("步骤1");
        //如果在已有的ThreadLocal中没有连接
       /* if (MyLoadBalanceRule.originHost.get() == null) {
            //获得所属ip
            List<String> originHostHeader = httpHeaders.get(MyLoadBalanceRule.originHostHeader);
            if (originHostHeader == null || originHostHeader.size() == 0) {
                String host = exchange.getRequest().getURI().getHost();
                //设置请求头
                exchange.getRequest().mutate().header(MyLoadBalanceRule.originHostHeader, host).build();
                //设置本机地址
                MyLoadBalanceRule.originHost.set(host);
            } else {
                MyLoadBalanceRule.originHost.set(originHostHeader.get(0));
            }
        }*/
        //开始路由 loadBalancer 里面看到了business和static
        if (this.loadBalancer instanceof RibbonLoadBalancerClient) {
            RibbonLoadBalancerClient client = (RibbonLoadBalancerClient) this.loadBalancer;
            String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost();
            balanceDto.setServiceId(serviceId);
            //这里使用userId做为选择服务实例的key, 调用的是MyLoadBalanceRule的choose, balanceDto 就是那边接收到的key
            return client.choose(serviceId, balanceDto);
        }else{
           // log.info("this is not ribbon"+JSONObject.toJSONString(this.loadBalancer ));
        }
        return super.choose(exchange);
    }

}