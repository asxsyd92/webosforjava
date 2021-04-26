package com.webos.controllers.webos;


import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtUtils;
import com.webcore.annotation.Route;
import com.webcore.service.LogService;
import com.webcore.service.UsersService;
import com.webos.Common;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.asxsydutils.utils.MD5.MD5_32bit;


@Route(Key = "/api/login")
public class LoginControllers extends Controller {
    @Inject
    LogService logService;
    /**
     * 用户登陆
     */
    @Clear
    @Before(POST.class)
    public void Login() {
        try {
            String user = getPara("user");
            String pw = getPara("pw");
            Record us = UsersService.Login(user);
            if (us != null) {
                Date locktime = null;
                int islock = 0;

                try {
                    islock = Integer.parseInt(us.getStr("Islock"));
                    locktime = us.getDate("locktime");
                } catch (Exception E) {
                }
                String startTime = "", startTime1 = "";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startTime = sdf.format(locktime);
                    startTime1 = sdf1.format(new Date());
                } catch (Exception e) {
                    System.out.println(e);
                }

                if (islock >= 10 && startTime.equals(startTime1)) {
                    setAttr("msg", "账号被锁定，请联系管理员！");
                    setAttr("Success", false);


                    logService.addLog("登陆失败", "账号被锁定，请联系管理员！", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");


                    renderJson();
                    return;
                }
                if (us.getInt("Status") == 1) {
                    setAttr("msg", "账号被冻结，请联系管理员！");
                    setAttr("Success", false);

                    logService.addLog("登陆失败", "账号被冻结，请联系管理员！", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");


                } else {

                    //加密后的字符串
                    String str = us.getStr("ID").toLowerCase() + pw;
                    String newstr = MD5_32bit(str);
                    if (us.getStr("Password").equals(newstr)) {


                        Map<String, Object> claims = new HashMap<String, Object>();
                        claims.put("id", us.getStr("ID"));
                        claims.put("account", us.getStr("Account"));
                        claims.put("orgid", us.getStr("OrganizeID"));
                        claims.put("orgname", us.getStr("Organize"));
                        claims.put("name", us.getStr("name"));
                        claims.put("role", us.getStr("Account") == "com/asxsyd92" ? "110" : us.getInt("identifier").toString());
                        claims.put("orname", us.getStr("Organize"));

                        String token = JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
                        setAttr("access_token", token);
                        setAttr("token_type", "Bearer");
                        setAttr("msg", "登录成功！");
                        setAttr("account", us.getStr("Account"));
                        setAttr("userid", us.getStr("ID"));
                        setAttr("orid", us.getStr("OrganizeID"));
                        setAttr("orname", us.getStr("Organize"));
                        setAttr("name", us.getStr("Name"));
                        setAttr("community", us.getStr("Community"));
                        setAttr("picture", us.getStr("avatar"));
                        setAttr("success", true);
                        try {
                            logService.addLog("登录成功", "登录成功！", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");
                            UsersService.InsertIslock(us.getStr("id"), false);
                        } catch (Exception ex) {
                        }
                    } else {
                        try {
                            logService.addLog("登录失败", "密码错误！", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");
                            UsersService.InsertIslock(us.getStr("id"), true);
                        } catch (Exception ex) {
                        }

                        setAttr("msg", "密码错误！");
                        setAttr("success", false);

                    }
                }

            } else {
                setAttr("msg", "账号不存在！");
                setAttr("success", false);
            }
        }catch (Exception ex) {

            logService.addLog("系统日志", "系统异常！", Common.getRemoteLoginUserIp(this.getRequest()), "", "", "系统日志", ex.toString(), "", "");


            setAttr("msg", "系统异常");
            setAttr("success", false);
        }
        renderJson();
    }
}
