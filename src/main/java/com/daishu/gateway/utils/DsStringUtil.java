package com.daishu.gateway.utils;

import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Administrator on 2019/6/27 0027.
 */
public class DsStringUtil {
    /**
     * @param str
     * @return
     * @description 有空就返回true, 全都有值返回false
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    @ApiParam("null或者空就返回false, 全都有值返回true")
    public static boolean isNotEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        return true;
    }


    /**
     * @description 对两个,分割的字符串取并集
     * @param str1
     * @param str2
     * @return
     */
    public static String getSetString(String str1,String str2){
        String [] ary1=str1.split(",");
        String [] ary2=str2.split(",");
        Set<String> stringSet=new LinkedHashSet<String>();
        for(String a:ary1){
            if(DsStringUtil.isNotEmpty(a)){
                stringSet.add(a);
            }
        }
        for(String a:ary2){
            if(DsStringUtil.isNotEmpty(a)){
                stringSet.add(a);
            }
        }
        StringBuffer buffer=new StringBuffer();
        for(String s:stringSet){
            buffer.append(s+",");
        }
        return buffer.substring(0,buffer.length()-1);
    }

    /**
     * @description 获得当前秒级原点
     * @return
     */
    public static Date getNowZero(){
        Date now=new Date();
        long l=now.getTime()/1000*1000;
        return new Date(l);
    }

}