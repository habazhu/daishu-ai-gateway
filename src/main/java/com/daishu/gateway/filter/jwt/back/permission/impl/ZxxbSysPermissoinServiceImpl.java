package com.daishu.gateway.filter.jwt.back.permission.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.daishu.gateway.base.DefineEntity;
import com.daishu.gateway.filter.jwt.back.permission.ZxxbSysPermissoinService;
import com.daishu.gateway.filter.jwt.back.permission.impl.model.ZxxbSysPermissionModel;
import com.daishu.gateway.jedis.RedisUtil;
import com.daishu.gateway.utils.DsHttpClient;
import com.daishu.gateway.utils.DsStringUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class ZxxbSysPermissoinServiceImpl implements ZxxbSysPermissoinService {
    private static Log log = LogFactory.getLog(ZxxbSysPermissoinServiceImpl.class);

    @Autowired
    private DefineEntity defineEntity;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    @ApiParam("查看业务系统权限列表,如果是超级管理员或者是子管理员就返回userId /** ," +
            "其他就返回列表   platform/user/modifyaddr" +
            "  /statistics/lessonscorecontroller/querysatisfaction 结构")
    public ZxxbSysPermissionModel queryPhpSystemAuthorityUrl(Integer staffId) throws IOException {
        //设置返回内容
       String key=defineEntity.getActive()+":ZxxbSysPermissoinServiceImpl:"+staffId;
        String msg = redisUtil.get(key);
        if(DsStringUtil.isEmpty(msg)){
            HashMap<String,String> map = new HashMap();
            map.put("userId", staffId+"");
            DsHttpClient dsHttpClient = new DsHttpClient();
            msg = dsHttpClient.httpGet(defineEntity.getPhpSystemAuthorityUrl(), map);

        }
      /*  HashMap<String,String> map = new HashMap();
        map.put("userId", staffId+"");
        DsHttpClient dsHttpClient = new DsHttpClient();
        String  msg = dsHttpClient.httpGet(defineEntity.getPhpSystemAuthorityUrl(), map);
*/
        JSONObject jsonObject= JSONObject.parseObject(msg);
        JSONObject data=(JSONObject)jsonObject.get("data");
        if(data.get("authList")!=null){
            redisUtil.set(key,msg,defineEntity.getTokenCachime());
            List<String> list= JSONArray.parseArray(data.get("authList").toString(),String.class);
            ZxxbSysPermissionModel zxxbSysPermissionModel=new ZxxbSysPermissionModel();
            for(String url:list){
                if(url.equals("/**")){
                    zxxbSysPermissionModel.setUrlRoleEnum(ZxxbSysPermissionModel.UrlRoleEnum.ALL);
                }
                zxxbSysPermissionModel.getServerUrl().add(url.toLowerCase());
            }
            return zxxbSysPermissionModel;
        }
        return  null;
    }

}