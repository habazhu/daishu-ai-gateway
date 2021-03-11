package com.daishu.gateway.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class DaiShuBaseUtil {

    private static Log log = LogFactory.getLog(DaiShuBaseUtil.class);

    /**
     * @description 检测出空的项目并返回
     * @param t
     * @param exceptParamterNames
     * @return
     */
    public static String hasEmputyParamter(Object t, HashMap<String, String> exceptParamterNames) {
        String emputyStr="";
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f : fields) {
            // 说明该字段需要校验
            if (exceptParamterNames != null && exceptParamterNames.get(f.getName()) != null) {
                continue;
            } else {
                Object value = getFieldValueByName(f.getName(), t);
                if (hasEmputy(value)) {
                    log.info("hasEmputyParamter:f.getName()" + f.getName() + "value:" + value + "--");
                    emputyStr=emputyStr+f.getName()+" -- ";
                }
            }
        }
        return emputyStr;
    }

    /**
     * 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            // log.error(e.getMessage(), e);
            return null;
        }
    }
    /**
     * 检查对象是否为空
     **/
    public static boolean hasEmputy(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return true;
            }
            // 如果是字符串
            if (obj.getClass().getName().equals("java.lang.String")) {
                if (obj.toString().trim().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
    /****
     * @description 将不同类型的数据根据映射放入其中
     * @since 2017/3/8
     * @author YuLei
     *
     * ****/
    public static void setFieldValueByNameNew(String fieldName, Class<?>[] parameterTypes, Object setValue, Object o) {
        if (parameterTypes[0].getName().indexOf("Integer") > -1) {
            setValue = Integer.parseInt(setValue.toString());
        }
        setFieldValueByName(fieldName, parameterTypes, setValue, o);
    }

    /******
     * 根据属性名,执行set方法
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     *
     *
     * ***/
    public static void setFieldValueByName(String fieldName, Class<?>[] parameterTypes, Object setValue, Object o) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + fieldName.substring(1);
        try {
            o.getClass().getMethod(setMethodName, parameterTypes).invoke(o, setValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}