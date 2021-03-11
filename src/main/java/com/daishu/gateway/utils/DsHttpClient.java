package com.daishu.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public class DsHttpClient {
    private static Logger log = LoggerFactory.getLogger(DsHttpClient.class);


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    /**
     * @param url
     * @param json
     * @return
     * @throws IOException
     * @description 发送post请求
     */
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /***
     * 发送get请求
     * @param url
     * @param paramterMap 参数
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, HashMap<String, String> paramterMap) throws IOException {
        StringBuffer urlGet = new StringBuffer(url + "?");
        //拼装url
        for (String key : paramterMap.keySet()) {
           // log.info(url + "请求解析 Key: " + key + " Value: " + paramterMap.get(key));
            if (!DaiShuBaseUtil.hasEmputy(paramterMap.get(key))) {
                urlGet.append(key + "=" + paramterMap.get(key) + "&");
            }
        }
        //最后一位一定是&或者是?,所以删除
        urlGet.deleteCharAt(urlGet.length() - 1);

        return httpGet(urlGet.toString());
    }


    /***
     * 发送get请求
     * @param url
     * @param bean
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, Object bean) throws IOException {
        StringBuffer urlGet = new StringBuffer(url + "?");
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            Object value = DaiShuBaseUtil.getFieldValueByName(f.getName(), bean);
            if (value != null && !value.equals("")) {
                urlGet.append(f.getName() + "=" + value + "&");
            }
        }
        //最后一位一定是&或者是?,所以删除
        urlGet.deleteCharAt(urlGet.length() - 1);
        return httpGet(urlGet.toString());
    }


    /***
     * 发送get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGet(String url) throws IOException {
        log.info("MathHttpClient.httpGet----发送请求:" + url);
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        String result = response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
       // log.info("MathHttpClient.httpGet----发送请求:" + url + "  返回结果:" + result);
        return result;
    }


    public static void main(String[] args) throws IOException {
       String url = "http://localhost:9090/conponSentController/importConponUserExcel";
        HashMap map = new HashMap();
        map.put("conponId", 1);
        String msg = new DsHttpClient().post(url, JSONObject.toJSONString(map));
        System.out.printf("===" + msg);

    }


}