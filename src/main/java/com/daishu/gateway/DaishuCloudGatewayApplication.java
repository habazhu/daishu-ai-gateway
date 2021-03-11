package com.daishu.gateway;

import com.daishu.gateway.base.GateWayOrder;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@Api( tags = {"参考文献https://blog.csdn.net/autfish/article/details/90637957",
"yml标签 https://blog.51cto.com/1459294/2125866?source=drt https://blog.csdn.net/xingbaozhen1210/article/details/80290588"})
public class DaishuCloudGatewayApplication {
    private static Logger log = LoggerFactory.getLogger(DaishuCloudGatewayApplication.class);

    public static void main(String[] args) {
        log.info("===========开始启动========");
        SpringApplication.run(DaishuCloudGatewayApplication.class, args);
        log.info("===========启动结束========");

    }
    //https://spring.io/projects/spring-cloud-gateway#overview
   /* @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("replace", r ->
                        r.path("/statistics/conponController/queryCoponPage").uri("http://localhost:9090/conponController/queryCoponPage").order(GateWayOrder.LOG.getOrder() + 1)
                )
                .build();
    }*/
}
