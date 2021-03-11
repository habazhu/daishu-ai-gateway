package com.daishu.gateway.utils;

import com.daishu.gateway.base.DsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassNameDsAssert
 * @Description 断言
 * @Author
 * @Date2019/7/23 0023 14:13
 * @Version V1.0
 **/
public class DsAssert {
    private static Log log = LogFactory.getLog(DsAssert.class);

    /**
     * @param o
     * @param ex
     * @description 断言不为空
     */
    public static void assertNotNull(Object o, DsException ex) {
        if (o == null) {
            log.info(ex.getCode()+ex.getMessage());
            throw ex;
        }
    }

    public static void assertIsNull(Object o, DsException ex){
        if (o != null) {
            log.info(ex.getCode()+ex.getMessage());
            throw ex;
        }
    }

    /**
     * @param b
     * @description 断言正确,如果不正确就抛异常
     */
    public static void assertTrue(boolean b, DsException ex) {
       if(!b){
           log.info(ex.getCode()+ex.getMessage());
           throw ex;
       }
    }
    /**
     * @param b
     * @description 断言错误,如果正确就抛异常
     */
    public static void assertFalse(boolean b, DsException ex){
        if(b){
            log.info(ex.getCode()+ex.getMessage());
            throw ex;
        }
    }
}