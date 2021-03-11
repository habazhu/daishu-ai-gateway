package com.daishu.gateway.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api("自定义异常处理 https://blog.csdn.net/u010889990/java/article/details/82963682")
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @ApiParam("获取异常属性")
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;
        Throwable error = super.getError(request);
        //没有发现服务(服务器发版中404)
        if(error instanceof org.springframework.web.server.ResponseStatusException){
            return response(1000,"系统升级中敬请期待...",this.buildMessage(request, error));
        }
        //没有发现服务(服务器发版中503)
        if(error instanceof org.springframework.cloud.gateway.support.NotFoundException){
            return response(1000,"系统升级中敬请期待...",this.buildMessage(request, error));
        }
        //没有发现服务(服务器发版中 netty无法连接)
        if (error!=null&&error.getMessage()!=null&& error.getMessage().contains("Connection refused: no further information:" )) {
            return response(1000,"系统升级中敬请期待...",this.buildMessage(request, error));
        }
        if(error instanceof com.daishu.gateway.base.DsException &&((DsException) error).getCode()==9999 ){
            return response(9999,"token失效",this.buildMessage(request, error));
        }
        if(error instanceof com.daishu.gateway.base.DsException &&((DsException) error).getCode()==9998 ){
            return response(9998,"查询不到权限",this.buildMessage(request, error));
        }
        return response(code, this.buildMessage(request, error),null);
    }

    @ApiParam("指定响应处理方法为JSON处理的方法")
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @ApiParam("根据code获取对应的HttpStatus")
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("code");
        //设置业务异常
        if(statusCode==1000||statusCode==9999||statusCode==9998){
            return HttpStatus.valueOf(200);
        }

        return HttpStatus.valueOf(statusCode);
    }

    @ApiParam("构建异常信息")
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }


    @ApiParam("构建返回的JSON数据格式")
    public static Map<String, Object> response(@ApiParam("状态码") int status,@ApiParam("异常信息") String errorMessage,@ApiParam("异常")String data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", status);
        map.put("message", errorMessage);
        map.put("msg", errorMessage);
        map.put("data", data);
        map.put("dsPlatName", "gateway");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("serverTime",simpleDateFormat.format(new Date()));
        return map;
    }
}