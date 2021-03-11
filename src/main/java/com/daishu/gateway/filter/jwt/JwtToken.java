package com.daishu.gateway.filter.jwt;

import com.daishu.gateway.base.DefineEntity;
import com.daishu.gateway.base.DsException;
import com.daishu.gateway.base.GateWayOrder;
import com.daishu.gateway.filter.jwt.back.BackJwt;
import com.daishu.gateway.filter.jwt.stu.StuJwt;
import com.daishu.gateway.filter.jwt.wx.WxJwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Api("校验token的jwt")
@Component
public class JwtToken implements GlobalFilter, Ordered {
    private static Logger log = LoggerFactory.getLogger(JwtToken.class);
    @Autowired
    private DefineEntity defineEntity;
    @Autowired
    @ApiParam("Qualifier 可以用来处理多继承 设置 CheckToken backJwt")
    private BackJwt backJwt;
    @Autowired
    private StuJwt stuJwt;
    @Autowired
    private WxJwt wxJwt;

    private static List<Pattern> list=new ArrayList();

    @PostConstruct
    public void construct(){
        //正则匹配免token认证
        for(String key:defineEntity.getExcludePatterns().keySet()){
            Pattern p = Pattern.compile(defineEntity.getExcludePatterns().get(key));
            list.add(p);
        }
    }

    @Override
    @ApiParam("过滤的主方法")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //上帝模式
        if(defineEntity.isGod()){
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();



        //获得地址
        String url = request.getURI().getPath();
        String urlPath=url.replaceAll("/", "");

        //检查请求者信息是否再黑名单中
        if (checkIsInBlack(request)) {
            log.info("--黑单未通过--" + url);
            throw new DsException(220000);
        }
        //如果完全匹配上了免token认证的
        if (defineEntity.getExcludePath().get(urlPath) != null) {
            return chain.filter(exchange);
        }
        //正则匹配免token认证
        for(Pattern pattern:list){
            boolean isMatch =  pattern.matcher(urlPath).matches();
            if(isMatch){
                log.info("接口免验证匹配:"+url);
                return chain.filter(exchange);
            }
        }
        //正则匹配白名单
        for(String key:defineEntity.getIncludePatterns().keySet()){
            boolean isMatch = Pattern.matches(key, urlPath);
            if(isMatch){
                return chain.filter(exchange);
            }
        }
        //如果匹配上了白名单
        if (defineEntity.getIncludePath().get(urlPath) != null) {
            //return chain.filter(exchange);
            //bigMessage 校验token和url通过内置异常
            boolean b=checkUrl(url,defineEntity.getIncludePath().get(urlPath),exchange,chain);
            //log.info("--白名单--" + url+" b"+b);
          /*  if(b){
                return chain.filter(exchange);
            }else{
                log.info("--权限校验未通过--" + url+" b"+b);
                throw new DsException(9998);
            }*/
        } else {
           // log.info("--接口不在请求列表以内--" + url);
        }

        log.info("--接口不在请求列表以内:通过所有流程--" + url);
        //匹配不上就返回正确
        return chain.filter(exchange);
    }
    @ApiParam("检查url")
    private boolean checkUrl(@ApiParam("请求的url路径") String url,@ApiParam("yml文件中的配置系统值")String ymlPlat, ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean check = false;
        //获取token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(token==null||token.trim().equals("")){
            throw new DsException(9999);
        }
        token=  token.replaceAll("Bearer", "").trim();
        //系统
        String[] system = ymlPlat.split(",");
        //如果是单系统支持
        if (system.length == 1) {
            if (system[0].equals("back")) {
                check = backJwt.check(url, token, defineEntity.getPlatform(), defineEntity.getPlatformIssuer());
            }
            if (system[0].equals("stu")) {
                //TODO 暂时放过学生端校验
                //check =stuJwt.check(url, token, defineEntity.getStudent(), defineEntity.getStuTokenIssuer());
            }
            if(system[0].equals("wx")) {
                //TODO 暂时放过微信端校验
                //check =wxJwt.check(url, token, defineEntity.getWxapi(), defineEntity.getStuTokenIssuer());
            }

        }
        //如果是多系统支持
        if (system.length > 1) {
            log.info("是一个多系统的");
            check=true;
        }
        return check;
    }

    @Override
    public int getOrder() {
        return  GateWayOrder.JWT.getOrder();
    }


    /**
     * 检查是否在黑名单中
     * @param request
     * @return
     */
    public boolean checkIsInBlack(ServerHttpRequest request) {
        if (Objects.nonNull(defineEntity.getAccessBlackList())  ) {
            List<String> ipAddr = defineEntity.getAccessBlackList().getIpAddr();
            if (Objects.nonNull(ipAddr) && ipAddr.size() > 0) {
                String remoteIp = Objects.requireNonNull(request.getRemoteAddress()).getAddress().toString();
                if (Objects.nonNull(remoteIp) && ipAddr.contains(remoteIp)) {
                    return true;
                }
            }
        }

       if (Objects.nonNull(defineEntity.getAccessBlackList())) {
           List<String> failurePath = defineEntity.getAccessBlackList().getFailurePath();

           if (Objects.nonNull(failurePath) && failurePath.size() > 0 ) {
               String path = request.getURI().getPath();
               if (Objects.nonNull(path) && failurePath.contains(path)) {
                   return true;
               }
           }
       }
        return false;
    }
}