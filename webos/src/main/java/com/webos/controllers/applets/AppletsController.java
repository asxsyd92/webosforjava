package com.webos.controllers.applets;

import com.alibaba.fastjson.JSON;
import com.asxsydutils.config.LoginUsers;
import com.asxsydutils.utils.HttpHelper;
import com.asxsydutils.utils.*;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import com.mailservice.MailUtils;
import com.security.AuthResult;
import com.security.Authorization;
import com.security.JwtUtils;
import com.webcore.modle.xRoleApp;
import com.webcore.service.RoleAppService;
import com.webcore.service.UsersIntegralService;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.asxsydutils.utils.Common;

import kotlin.collections.ArrayDeque;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("/api/applets")
@Clear
public class AppletsController extends Controller {

    @Inject
    RoleAppService roleAppService;
    public void getUserWXLoginInfo() {
        try {
            String code = getPara("code");
            String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
            Record requestUrlParam = new Record();
            requestUrlParam.set("appid", "wxafadbf29bdbd7b6f"); // 小程序 appId
            requestUrlParam.set("secret", "e7b75d74c2ade0b574ffdb9655076d8e"); // 小程序 appSecret
            requestUrlParam.set("js_code", code); // 登录时获取的 code
            requestUrlParam.set("grant_type", "authorization_code"); // 授权类型，此处固定
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : requestUrlParam.getColumns().entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue());
                sb.append("&");
            }
            String s = sb.toString();
            if (s.endsWith("&")) {
                s = StringUtils.substringBeforeLast(s, "&");
            }
            String KK = HttpHelper.sendGet(requestUrl, s);
            Map<String, Object> kk = JSON.parseObject(KK, Map.class);

           Record u= Db.findById("users","openid",kk.get("openid").toString());
            Map<String, Object> claims = new HashMap<String, Object>();
            claims.put("id", u.getStr("ID"));
            claims.put("account",  u.getStr("Account"));
            claims.put("avatar", u.getStr("avatar"));
            claims.put("ip",Common.getRemoteLoginUserIp(this.getRequest()));
            kk.put("unionid", "");
            setAttr("msg", "成功！");
            setAttr("success", true);
            setAttr("data", kk);

        } catch (Exception exception) {
            setAttr("msg", "失败！" + exception.getMessage());
            setAttr("success", false);
            setAttr("data", null);
        }

        renderJson();
    }

    public void fundsave() {
        try {
            String data = getPara("data");
            Map<String, Object> map = JSON.parseObject(data, Map.class);
            String url = "http://fundgz.1234567.com.cn/js/" + map.get("code") + ".js?" + new Date().getTime();
            try {
                String js = HttpHelper.sendAgentChromeGet(url);
                int startIndex = js.indexOf("(");
                int endIndex = js.lastIndexOf(")");
                String json = js.substring(startIndex + 1, endIndex);
                if (json.equals("")) {

                    setAttr("msg", "基金代码有误！");
                    setAttr("success", false);
                    setAttr("data", null);
                    renderJson();
                    return;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                setAttr("msg", "基金代码有误！");
                setAttr("success", false);
                setAttr("data", null);
                renderJson();
                return;
            }

            Record da = null;
            if (map.get("id").equals("")) {
                da = Db.findFirst("select *from fund where userid='" + map.get("userid") + "' and code='"
                        + map.get("code") + "'");//
                if (da == null) {
                    Record record = new Record();
                    record.setColumns(map);
                    if (record.get("id").equals("")) {
                        record.set("id", StringUtil.getPrimaryKey());
                        Db.save("fund", "id", record);
                        setAttr("msg", "新增成功！");
                        setAttr("success", true);
                        setAttr("data", null);
                    }

                } else {
                    setAttr("msg", "数据异常请稍后重试！");
                    setAttr("success", false);
                    setAttr("data", null);
                }

            } else {

                Record record = new Record();
                record.setColumns(map);
                Db.update("fund", "id", record);
                setAttr("msg", "更新成功！");
                setAttr("success", true);
                setAttr("data", null);

            }

        } catch (Exception exception) {
            setAttr("msg", "失败！" + exception.getMessage());
            setAttr("success", false);
            setAttr("data", null);
        }

        renderJson();
    }

    public void qqlogin() {
        try {
            String code = getPara("code");

            String data = HttpHelper.sendGet("https://api.q.qq.com/sns/jscode2session",
                    "appid=1110632218&secret=qxn51EHWjKJXtqJ3&js_code=" + code + "&grant_type=authorization_code");

            renderJson(data);
        } catch (Exception ex) {

        }

    }
   @Before({POST.class})
   public void ByOpenId(){
    String openid = getPara("openid");
    String userid = getPara("userid");

    Record da = Db.findFirst("select *from users where openid='"+openid+"' and id='"+userid+"'");
    if (da != null) {
        setAttr("msg", "成功！");
        setAttr("success", true);
        setAttr("data", da);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", da.getStr("ID"));
        claims.put("account",  da.getStr("Account"));
        claims.put("avatar", da.getStr("avatar"));
        claims.put("ip",Common.getRemoteLoginUserIp(this.getRequest()));
        AuthResult result= JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
        String token =result .getJwt();
        long expires_in =result.getExpires_in();

        setAttr("expires_in",expires_in);
        setAttr("token",token);
    }else {
        setAttr("success", false);
        setAttr("msg", "找不到用户！");

    }
    renderJson();
}


    @Before({ POST.class })
    public void updateUserInfo() {
        try {

            String openid = getPara("openid");
            System.out.println(openid);
            String unionid = getPara("unionid");
            System.out.println(unionid);
            String nickName = getPara("nickName");
            System.out.println(nickName);
            String nickname = getPara("nickname");
            String type = getPara("type");
            System.out.println(nickname);
            String headUrl = getPara("headUrl");
            Record da = null;
            da = Db.findById("users", "openid", openid);
            if (da != null) {
                da.set("unionid", unionid);
                if (type.equals("qq")) {
                    da.set("qqopenid", openid);
                } else {
                    da.set("openid", openid);
                }
                da.set("Name", nickName);
                da.set("avatar", headUrl);
                da.set("Addtime", new Date());
                ;
                Db.update("users", da);
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("data", da);
                Map<String, Object> claims = new HashMap<String, Object>();
                claims.put("id", da.getStr("ID"));
                claims.put("account",  da.getStr("Account"));
                claims.put("avatar", da.getStr("avatar"));
                claims.put("ip",Common.getRemoteLoginUserIp(this.getRequest()));
                AuthResult result= JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
                String token =result .getJwt();
                long expires_in =result.getExpires_in();

                setAttr("expires_in",expires_in);
                setAttr("token",token);
                UsersIntegralService.AddIntegral(da.getStr("id"), 10);
            } else {
                Record users = new Record();
                users.set("id", StringUtil.getPrimaryKey());
                users.set("unionid", unionid);
                users.set("Name", nickName);
                users.set("avatar", headUrl);
                if (type.equals("qq")) {
                    users.set("qqopenid", openid);
                } else {
                    users.set("openid", openid);
                }
                users.set("Addtime", new Date());
                Record record= Db.findFirst("SELECT MAX(UserNo) count FROM users");
                int k=record.getInt("count");
                k=k+1;
                users.set("UserNo",k);
                Db.save("users", users);
                UsersIntegralService.AddIntegral(users.getStr("id"), 10);
                setAttr("msg", "成功！");
                setAttr("success", true);
                if (type.equals("qq")) {
                    users.set("openid", openid);
                } else {
                    users.set("openid", openid);
                }
                Map<String, Object> claims = new HashMap<String, Object>();
                claims.put("id", users.getStr("ID"));
                claims.put("account",  users.getStr("Account"));
                claims.put("avatar", users.getStr("avatar"));
                claims.put("ip",Common.getRemoteLoginUserIp(this.getRequest()));
                AuthResult result= JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
                String token =result .getJwt();
                long expires_in =result.getExpires_in();

                setAttr("expires_in",expires_in);
                setAttr("token",token);
                setAttr("data", users);

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            setAttr("msg", "失败！");
            setAttr("success", false);
            setAttr("data", null);
        }

        renderJson();
    }

    /// <summary>
    /// 点赞
    /// </summary>
    /// <param name="id">文章id</param>
    /// <param name="userid">用户id</param>
    /// <param name="integral">获得的积分</param>
    /// <returns></returns>

    // 小程序点赞功能@Before({Authorization.class})
    @Before({ POST.class, Authorization.class })
    public void ThumbsUp() {
        String id = getPara("id");
        String userid = getPara("userid");
        Integer jf = getParaToInt("jf");
        if (userid == null) {
            setAttr("msg", "请重新登陆再试！");
            setAttr("success", true);
            setAttr("data", null);
        } else {

            // 更新点赞次数
            try {
                Record da = Db.findById("a_article", "ID", id);
                Integer i = da.getInt("thumbsup");
                if (i == null) {
                    i = 1;
                } else {
                    i = i + 1;
                }
                //// 更新阅读量
                da.set("thumbsup", i);

                Db.update("a_article", da);
            } catch (Exception ex) {
            }
            // 更新广告后的积分
            if (jf == 1) {
                if (UsersIntegralService.AddIntegral(userid, 10)) {
                    setAttr("msg", "成功！");
                    setAttr("success", true);
                    setAttr("data", null);

                } else {
                    setAttr("msg", "失败！");
                    setAttr("success", false);
                    setAttr("data", null);
                }
            } else {
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("data", null);
            }
        }
        renderJson();
    }
    @Before({ POST.class, Authorization.class })
    public void GetUserUsersIntegral() {
        String id = getPara("id");
        Record da = Db.findById("usersintegral", "Userid", id);
        if (da == null) {
            da = new Record();
            da.set("integral", 0);
            renderJson(da);
        } else {
            renderJson(da);
        }

    }
    @Before({ POST.class, Authorization.class })
    public void Integral() {
        LoginUsers usersModel = Common.getLoginUser(this);

        if (UsersIntegralService.AddIntegral(usersModel.getId(), 10)) {
            setAttr("msg", "恭喜获得10积分");
            setAttr("success", true);
            setAttr("data", null);

        } else {
            setAttr("msg", "获取失败！");
            setAttr("success", true);
            setAttr("data", null);

        }
        renderJson();
    }

    public void GetBycode() {
        try {
            String code = getPara("code");
            List<Record> da = Db.find("SELECT *FROM dictionary where ParentID IN(SELECT id from dictionary where code='"
                    + code + "' and IsUse=1) and IsUse=1");
            setAttr("msg", "成功！");
            setAttr("success", true);
            setAttr("data", da);
        } catch (Exception ex) {
            setAttr("msg", "失败！");
            setAttr("success", true);
            setAttr("data", null);
        }

        renderJson();
    }

    /**
     * 签到
     */
    @Before({ POST.class, Authorization.class })
    public void Signin() {
        try {
            LoginUsers usersModel = Common.getLoginUser(this);

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
            String tiem = dfs.format(new Date());
            Record record= Db.findFirst( "select * from u_signin where  date_format(SignDate,'%Y/%m/%d')='"+tiem+"' and Userid='" + usersModel.getId() + "'");
            if (record == null) {
                Record o = new Record();
                o.set("ID", StringUtil.getPrimaryKey());
                o.set("SignDate", tiem);
                o.set("Userid", usersModel.getId());
                Db.save("u_signin", o);
                setAttr("msg", "签到成功！");
                setAttr("success", true);

            } else {
                setAttr("msg", "今日已签到！");
                setAttr("success", true);

            }
        } catch (Exception ex) {
            setAttr("msg", "签到失败！");
            setAttr("success", false);
            setAttr("data", null);
        }
        renderJson();
    }
    @Before({ POST.class, Authorization.class })
    public void  getSinList(){
        try{
            LoginUsers usersModel = Common.getLoginUser(this);

            List<Record> qdlist=Db.find("select DISTINCT date_format(SignDate,'%Y/%m/%d') as date,'已签到' as info  from u_signin where SignDate BETWEEN '"+StringUtil.FirstDate()+"' and '"+StringUtil.LastDte()+"' and Userid='"+usersModel.getId()+"'" );

            setAttr("msg", "获取成功！");
            setAttr("success", true);
            setAttr("data", qdlist);

    } catch (Exception ex) {
        setAttr("msg", "获取失败！"+ex.getMessage());
        setAttr("success", false);
        setAttr("data", null);
    }
    renderJson();

    }
    public void fund() {
        try {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();
            ddf1.setMaximumFractionDigits(2);
            String userid = getPara("userid");
            List<Record> list = Db.find("select *from fund where userid='" + userid + "'");

            List<Map<String, Object>> list2 = new ArrayDeque<>();
            if (list.size() > 0) {
                double svg = 0;
                int zhang = 0;
                int die = 0;
                for (Record r : list) {

                    String url = "http://fundgz.1234567.com.cn/js/" + r.get("code") + ".js?" + new Date().getTime();

                    String data = HttpHelper.sendAgentChromeGet(url);
                    int startIndex = data.indexOf("(");
                    int endIndex = data.lastIndexOf(")");
                    String json = data.substring(startIndex + 1, endIndex);
                    if (!json.equals("")) {
                        Map<String, Object> jsons = JSON.parseObject(json, Map.class);
                        if (r.get("fe") != null) {
                            // 预估收益=（估算净值-净值）x 确认份额
                            double gsz = Double.parseDouble(jsons.get("gsz").toString());
                            double fe = Double.parseDouble(r.get("fe").toString());
                            double dwjz = Double.parseDouble(jsons.get("dwjz").toString());
                            double sy = (gsz - dwjz) * fe;
                            double yjs = ((gsz - dwjz) / dwjz) * 100;
                            svg = svg + sy;
                            if (sy > 0) {
                                zhang = 1 + zhang;
                            } else {
                                die = 1 + die;
                            }
                            jsons.put("sy", ddf1.format(sy));
                            jsons.put("id", r.get("id"));
                            jsons.put("fe", r.get("fe"));
                            jsons.put("code", r.get("code"));
                            jsons.put("gsz", ddf1.format(yjs));
                        }

                        list2.add(jsons);
                    }

                }
                Collections.sort(list2, new Comparator<Map<String, Object>>() {
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        Double name1 = Double.parseDouble(o1.get("sy").toString());// name1是从你list里面拿出来的一个
                        Double name2 = Double.parseDouble(o2.get("sy").toString()); // name1是从你list里面拿出来的第二个name
                        return name2.compareTo(name1);
                    }
                });
                setAttr("msg", "获取成功！");
                setAttr("success", true);
                setAttr("data", list2);
                setAttr("ygsy", ddf1.format(svg));
                setAttr("zhang", zhang);
                setAttr("die", die);
            }

        } catch (Exception ex) {
            setAttr("msg", "获取失败！");
            setAttr("success", false);
            setAttr("data", null);
        }
        renderJson();
    }

    public void funddel() {
        try {

            String id = getPara("id");
            boolean record = Db.deleteById("fund", "id", id);
            if (record) {

                setAttr("msg", "删除成功！");
                setAttr("success", true);
                setAttr("data", "");
            } else {
                setAttr("msg", "今日已签到！");
                setAttr("success", false);
                setAttr("data", "");
            }
        } catch (Exception ex) {
            setAttr("msg", "签到失败！");
            setAttr("success", false);
            setAttr("data", null);
        }
        renderJson();
    }
    @Before({POST.class})
//获取小程序菜单
    public void getMenuList(){
        try {

             //获取菜单
            Object  da = RoleAppService.GetxAppList(0);
            //获取公告
           List<Record> notice= Db.find(  "select  * from notice  where  Users is null limit 5");
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", da);
            setAttr("notice", notice);

            setAttr("success", true);
        } catch (Exception ex) {

            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", ex.getMessage());
            setAttr("success", false);
        }
        renderJson();
    }
}
