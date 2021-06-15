package com.webos.controllers.webos;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jwt.JwtUtils;
import com.webcore.annotation.Route;
import com.webcore.service.LogService;
import com.webcore.service.UsersService;
import com.webos.Common;
import live.autu.plugin.jfinal.swagger.annotation.Api;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParam;
import live.autu.plugin.jfinal.swagger.annotation.ApiImplicitParams;
import live.autu.plugin.jfinal.swagger.annotation.ApiOperation;
import live.autu.plugin.jfinal.swagger.config.RequestMethod;

import java.util.HashMap;
import java.util.Map;

import static com.asxsydutils.utils.MD5.MD5_32bit;

@Api(tags = "test", description = "登陆控制器")
@Route(Key = "/api/login")
public class LoginControllers extends Controller {
    @Inject
    LogService logService;
    @Inject
    UsersService usersService;
    /**
     * 用户登陆
     */
    @Clear
    @Before(POST.class)
    @ApiOperation(tags = "index", methods = RequestMethod.POST, description = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", description = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pw", description = "密码", required = true, dataType = "String")
    })
    public void Login() {
        try {
            String user = getPara("user");
            String pw = getPara("pw");
            Record us = usersService.Login(user);
            if (us != null) {

                Integer islock=   CacheKit.get("logincache",user);
                if (islock!=null) {

                    if (islock > 10) {
                        logService.addLog( "登陆失败", user+"锁定30分钟", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");

                        setAttr("msg", "账户被锁定30分钟，请30分钟后重试");
                        setAttr("default", false);
                        setAttr("success", false);
                        renderJson();
                        return;
                    }
                }
                if (us.getInt("Status") == 1) {
                    setAttr("msg", "账号被冻结，请联系管理员！");
                    setAttr("success", false);

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
                            usersService.InsertIslock(us.getStr("id"), false);
                        } catch (Exception ex) {
                        }
                    } else {
                        try {
                            Integer u=   CacheKit.get("logincache",user);
                            logService.addLog("登录失败", "密码错误！连续10错误账户将锁定30分钟", Common.getRemoteLoginUserIp(this.getRequest()), us.getStr("name"), us.getStr("id"), "用户登陆", "", "", "");

                            if (u!=null){

                                u++;
                                CacheKit.put("logincache",user,u);
                            }else {
                                CacheKit.put("logincache",user,1);
                            }
                            int s=10-u;
                            setAttr("msg", "密码错误！连续10错误账户将锁定30分钟,剩余次数："+s);
                        } catch (Exception ex) {
                        }


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
