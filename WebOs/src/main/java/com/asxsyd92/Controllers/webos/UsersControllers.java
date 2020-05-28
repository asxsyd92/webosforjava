package com.asxsyd92.Controllers.webos;

import com.asxsyd92.Bll.UsersBll;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jwtTokenPlugin.interceptor.JwtTokenInterceptor;
import io.jsonwebtoken.Claims;


public class UsersControllers extends Controller {
    @Before(JwtTokenInterceptor.class)
    public void GetAppList()
    {
        Claims claims=getAttr("claims");


        String Role = claims.get("role").toString();
        int role= getInt(Role);
         UsersBll.Instance().GetAppList(role);

    }
}
