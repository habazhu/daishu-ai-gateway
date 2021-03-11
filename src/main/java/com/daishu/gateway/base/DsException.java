package com.daishu.gateway.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

public class DsException extends RuntimeException  {
    private static Log log = LogFactory.getLog(DsException.class);

    public static final HashMap<Integer, String> codeMap = new HashMap<Integer, String>();

    static {
        codeMap.put(0,"正常");
        //business 从1000~1500
        //sms从1501~2000
        codeMap.put(9998,"暂无权限");
        codeMap.put(9999,"token异常");
        codeMap.put(10000,"系统异常");
        codeMap.put(10001,"参数缺失");
        codeMap.put(10002,"创建Excel工作薄为空！");
        codeMap.put(10003,"excel未发现要导入内容");
        codeMap.put(10004,"模板title不匹配");
        codeMap.put(10005,"有空行 ");
        codeMap.put(10006,"上传文件格式错误,只能上传xls或者xlsx");
        codeMap.put(10007,"title数量不符!");
        codeMap.put(10008,"excel文件为null!");
        codeMap.put(10009,"没有从consul中找到服务!");
        codeMap.put(10010,"没有从consul中找到服务,强制执行策略失败!");
        codeMap.put(210524,"没有找到staffId！");
        codeMap.put(220000,"黑名单拦截");

    }


    private DsException(){}

    /**
     *
     * @param code 异常码
     */
    public DsException(int code){
        if(codeMap.get(code)==null){
            throw new RuntimeException("类DsCode 没有发现自定义code码:"+code);
        }
        this.code=code;
        this.msg=codeMap.get(code);
    }

    /**
     *
     * @param code 异常码
     * @param message 附加信息
     */
    public DsException(int code, String message){
        if(codeMap.get(code)==null){
            throw new RuntimeException("类DsCode 没有发现自定义code码:"+code);
        }
        this.code=code;
        this.msg=codeMap.get(code)+message;
    }

    //code码
    private int code;
    //提示消息
    private String msg;

    public static HashMap<Integer, String> getCodeMap() {
        return codeMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void print(Object o) {
        log.info(o.getClass().getName()+"抛出异常:code:"+this.getCode()+" msg:"+this.getMsg());
    }
}