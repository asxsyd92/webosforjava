package com.webos.controllers.webos;


import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtUtils;
import com.webcore.annotation.Route;
import com.webcore.service.LogService;
import com.webcore.service.UsersService;
import com.webos.Common;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.asxsydutils.utils.MD5.MD5_32bit;


@Route(Key = "/api/login")
public class LoginControllers extends Controller  {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    public static String header = "Authorization";  // 默认请求头标识符
    public static String tokenPrefix = "Bearer ";    // 默认token前缀
    public static String secret = "default";         // 默认私钥
    public static Long expiration = 604800L;          // 默认失效时间(秒)
  public static Kv jwtStore = Kv.create();

    private static String generateToken(Record userName) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(CLAIM_KEY_USERNAME, userName.getStr("Account"));
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put("id", userName.getStr("ID"));
        claims.put("name", userName.getStr("Name"));
        claims.put("account", userName.getStr("Account"));
        claims.put("community", userName.getStr("Community"));
        claims.put("role",userName.getStr("Account")== "com/asxsyd92" ?"110":userName.getInt("identifier").toString());

        return generateToken(claims);
    }

    /**
     * 根据Claims信息来创建Token
     *
     * @param claims
     * @returns
     */
    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成令牌的过期日期
     *
     * @return
     */
    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    @Clear
    @Before(POST.class)
    public void Login(){
        try{
        String user = getPara("user");
        String pw = getPara("pw");
        Record us = UsersService.Login(user);
        if (us!=null){
            Date locktime=null; int islock=0;

            try {
                 islock = Integer.parseInt(us.getStr("Islock"));
                locktime = us.getDate("locktime");
            }catch (Exception E){}
            String startTime="",startTime1="";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startTime= sdf.format(locktime);
                startTime1  = sdf1.format(new Date());
            }catch (Exception e){
                System.out.println(e);
            }

            if (islock>=10&&startTime.equals(startTime1)){
                setAttr("msg","账号被锁定，请联系管理员！");
                setAttr("Success",false);

                try{
                    LogService.addLog("登陆失败","账号被锁定，请联系管理员！", Common.getRemoteLoginUserIp(this.getRequest()),us.getStr("name"),us.getStr("id"),"用户登陆","","","");

                }catch (Exception ex){}
                renderJson();
                return;
            }



        if(us.getInt("Status")==1){
            setAttr("msg","账号被冻结，请联系管理员！");
            setAttr("Success",false);

            try{
                LogService.addLog("登陆失败","账号被冻结，请联系管理员！", Common.getRemoteLoginUserIp(this.getRequest()),us.getStr("name"),us.getStr("id"),"用户登陆","","","");

            }catch (Exception ex){}
        }else {

            //加密后的字符串
            String str = us.getStr("ID").toLowerCase() + pw;
           String newstr= MD5_32bit(str);
          if (us.getStr("Password").equals(newstr)) {
                Kv jwtStore = Kv.create();
           /*     jwtStore.set(us.getStr("Account"),
                        new User().setForces(Arrays.asList("登录后台", "管理用户"))
                                .setRoles(Arrays.asList("管理员", "普通用户")).setUserName(us.getStr("Account")).setPassword(us.getStr("Password"))
                );*/


              Map<String, Object> claims = new HashMap<String, Object>();
              claims.put("id", us.getStr("ID"));
              claims.put("account", us.getStr("Account"));
              claims.put("orgid", us.getStr("OrganizeID"));
              claims.put("orgname", us.getStr("Organize"));
              claims.put("name",us.getStr("name"));
              claims.put("role",us.getStr("Account")== "com/asxsyd92" ?"110":us.getInt("identifier").toString());


              String token = JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
/*              System.out.println(jwt);

              Claims parseJwt = JwtUtils.parseJwt(jwt);
              for (Map.Entry<String, Object> entry : parseJwt.entrySet()) {
                  System.out.println(entry.getKey() + "=" + entry.getValue());
              }
              Date d1 = parseJwt.getIssuedAt();
              Date d2 = parseJwt.getExpiration();
              System.out.println("令牌签发时间：" + sdf.format(d1));
              System.out.println("令牌过期时间：" + sdf.format(d2));*/



            //  String token =   JwtUtils.sign(, ,us.getStr("Password"));
            //  generateToken(us);
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
              try{
                  LogService.addLog("登录成功","登录成功！", Common.getRemoteLoginUserIp(this.getRequest()),us.getStr("name"),us.getStr("id"),"用户登陆","","","");
                  UsersService.  InsertIslock(us.getStr("id"),false);
              }catch (Exception ex){}
            }else {
              try{
                  LogService.addLog("登录失败","密码错误！", Common.getRemoteLoginUserIp(this.getRequest()),us.getStr("name"),us.getStr("id"),"用户登陆","","","");
                  UsersService.  InsertIslock(us.getStr("id"),true);
              }catch (Exception ex){}

                setAttr("msg","密码错误！");
                setAttr("success",false);

            }
        }

        }else {
            setAttr("msg","账号不存在！");
            setAttr("success",false);


        }}catch (Exception ex){
            try{
                LogService.addLog("系统日志","系统异常！", Common.getRemoteLoginUserIp(this.getRequest()),"","","系统日志",ex.toString(),"","");

            }catch (Exception esx){}
            setAttr("msg","系统异常");
            setAttr("success",false);
        }
        renderJson();
    }




}
