package com.daishu.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.daishu.gateway.base.DsException;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;


/**
 * @author yulei
 */
public class DsHeader {
    private static Log log = LogFactory.getLog(DsHeader.class);


    @ApiParam("版本号")
    private String ver;
    @ApiParam("设备名称system")
    private String system;

    @ApiParam("用户id")
    private String userId;
    @ApiParam("学生id")
    private String studentId;

    @ApiParam("设备id")
    private String deviceId;
    @ApiParam("设备类型 Ipad (1,ipad), PC(2,pc), Andriod(3,Android), Iphone(4,iphone);")
    private Integer clientid;

    public DsHeader(HttpHeaders httpHeaders) {
        this.system = httpHeaders.getFirst("system");
        this.userId = httpHeaders.getFirst("userId");
        this.studentId = httpHeaders.getFirst("studentId");
        this.deviceId = httpHeaders.getFirst("deviceId");
        this.ver = httpHeaders.getFirst("ver");
        if (httpHeaders.getFirst("clientid") != null) {
            try {
                this.clientid = Integer.parseInt(httpHeaders.getFirst("clientid"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("解析header:" + JSON.toJSONString(this));
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getClientid() {
        return clientid;
    }

    public void setClientid(Integer clientid) {
        this.clientid = clientid;
    }

    @ApiParam("判断版本情况  header内容小于输入version版本返回-1    0 等于  header内容大于version返回1")
    public Integer afterVer(String version) {
        //TODO 此处需要增加code
        DsAssert.assertTrue(DsStringUtil.isNotEmpty(version), new DsException(10001));
        String[] ary_version = version.split("\\.");
        String[] ary_ver = ver.split("\\.");
        for (int i = 0; i < ary_version.length; i++) {
            //如果当前版本位数不足就跳过
            if (ary_ver.length <= i) {
                break;
            }
            //如果当前位相等就比较下个位置
            if (Integer.parseInt(ary_version[i]) == Integer.parseInt(ary_ver[i])) {
                continue;
            }
            if (Integer.parseInt(ary_version[i]) > Integer.parseInt(ary_ver[i])) {
                return -1;
            }
            if (Integer.parseInt(ary_version[i]) < Integer.parseInt(ary_ver[i])) {
                return 1;
            }
        }
        return 0;
    }
}