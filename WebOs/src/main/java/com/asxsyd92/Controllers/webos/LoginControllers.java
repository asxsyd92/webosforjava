package com.asxsyd92.Controllers.webos;


import com.asxsyd92.Bll.UsersBll;
import com.asxsyd92.service.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jwtTokenPlugin.interceptor.JwtTokenInterceptor;
import com.jwtTokenPlugin.service.IJwtUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.asxsyd92.utils.MD5.MD5_32bit;


public class LoginControllers extends Controller {
    public static String header = "Authorization";  // 默认请求头标识符
    public static String tokenPrefix = "Bearer ";    // 默认token前缀
    public static String secret = "default";         // 默认私钥
    public static Long expiration = 604800L;          // 默认失效时间(秒)
    public static IJwtUserService userService = null;//  需要注入的服务参数
    public static Kv jwtStore = Kv.create();

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Before(POST.class)
    public void Login(){
        try{
        String user = getPara("user");
        String pw = getPara("pw");
        Record us = UsersBll.Instance().Login(user);
        if (us!=null){
        if(us.getInt("Status")==1){
            setAttr("msg","账号被冻结，请联系管理员！");
            setAttr("Success",false);
        }else {

            //加密后的字符串
            String str = us.getStr("ID").toLowerCase() + pw;
           String newstr= MD5_32bit(str);
          if (us.getStr("Password").equals(newstr)) {
                Kv jwtStore = Kv.create();
                jwtStore.set(us.getStr("Account"),
                        new User().setForces(Arrays.asList("登录后台", "管理用户"))
                                .setRoles(Arrays.asList("管理员", "普通用户")).setUserName(us.getStr("Account")).setPassword(us.getStr("Password"))
                );
                String token = generateToken(us);
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
            }else {
                setAttr("msg","密码错误！");
                setAttr("success",false);

            }
        }

        }else {
            setAttr("msg","账号不存在！");
            setAttr("success",false);


        }}catch (Exception ex){
            setAttr("msg",ex.getMessage());
            setAttr("success",false);
        }
        renderJson();
    }

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
    @Before(JwtTokenInterceptor.class)
    public  void  test(){}
}
