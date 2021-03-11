package com.daishu.gateway.ribbon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daishu.gateway.DaishuCloudGatewayApplication;
import com.daishu.gateway.utils.DsHttpClient;
import com.netflix.loadbalancer.Server;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * https://blog.csdn.net/lushuaiyin/article/details/104585630
 * https://blog.csdn.net/jijiuqiu6646/article/details/89456328
 */
@Api("consul的服务列表")
@Component
public class ConsulServerList {
    private static Logger log = LoggerFactory.getLogger(ConsulServerList.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<ServiceInstance> serviceUrl(String business) {
        List<ServiceInstance> list = discoveryClient.getInstances(business);
        return list;
    }


    public List<Server> queryList() throws IOException {
        DsHttpClient dsHttpClient = new DsHttpClient();
        //查询consul的已经注册的服务列表
        String consulQueryUrl="http://localhost:8500/v1/agent/services";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String jsonStr=DsHttpClient.httpGet(consulQueryUrl);
        JSONObject json=JSONObject.parseObject(jsonStr);
        if(json!=null) {
            log.info("InitRunner 执行consul检查纠正服务。。。返回 ："+json);
            for(String key:json.keySet()){
                JSONObject j= (JSONObject)json.get(key);

            }
           /* Iterator it= responseData.keySet().iterator();
            while(it.hasNext()) {
                String key= (String) it.next();
                Map instance = (Map) responseData.get(key);
                if(instance!=null) {
                    String Service =(String) instance.get("Service");
                    String Address =(String) instance.get("Address");
                    String Port =null;
                    if(instance.get("Port")!=null) {
                        Port =""+ instance.get("Port");
                    }
                    String ID =(String) instance.get("ID");
                   // Server s=new Server();

                    *//*if(Service.equalsIgnoreCase(servername) && Address.equals(ip) && Port.equals(port)) {
                        log.info("InitRunner 执行consul检查纠正服务，找到本实例的ID==="+ID);
                        isExistSelf=true;
                    }*//*
                }

            }*/
            log.info("InitRunner 执行consul检查纠正服务。。。遍历完毕\r\n");
        }else {
            log.info("InitRunner 执行consul检查纠正服务。。。返回null");
        }

        return null;
    }
}
