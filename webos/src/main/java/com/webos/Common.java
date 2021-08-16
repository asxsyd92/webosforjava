package com.webos;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.webcore.config.LoginUsers;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class Common {
    public static String getRemoteLoginUserIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }
public  static LoginUsers getLoginUser(Controller formDesignController){
    Claims claims =formDesignController.getAttr("claims");
     String  claimss= JSON.toJSONString(claims);
    return   JSON.parseObject(claimss, LoginUsers.class);
}
}
