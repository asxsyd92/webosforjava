package com.asxsyd92.controllers.applets;

import com.asxsyd92.annotation.Route;
import com.asxsyd92.service.UsersIntegralService;
import com.asxsyd92.utils.HttpHelper;
import com.asxsyd92.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import java.util.Date;
import java.util.List;


@Route(Key = "/api/applets")
@Clear
public class AppletsController  extends Controller {

    public void qqlogin() {
        String code = getPara("code");
        String data= HttpHelper.doGet("https://api.q.qq.com/sns/jscode2session?appid=1110632218&secret=qxn51EHWjKJXtqJ3&js_code="+ code + "&grant_type=authorization_code");

        renderJson(data);

    }

    @Before({POST.class})
    public void updateUserInfo() {
        try {

            String openid = getPara("openid");
            System.out.println(openid);
            String unionid = getPara("unionid");
            System.out.println(unionid);
            String nickName = getPara("nickName");
            System.out.println(nickName);
            String nickname = getPara("nickname");
            System.out.println(nickname);
            String headUrl = getPara("headUrl");
            Record da= Db.findById("users","qqopenid",openid);
            if (da != null)
            {
                da.set("unionid",unionid) ;
                da.set("Name",nickName) ;
                da.set("avatar",headUrl) ;
                da.set("Addtime",new Date()) ;
                da.set("unionid",unionid) ;
                Db.update("users",da);
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("data", da);
                UsersIntegralService.AddIntegral(da.getStr("id"), 10);
            }
            else{
                Record users = new Record();
                users.set("id" , StringUtil.getPrimaryKey());
                users.set("unionid" , unionid);
                users.set("Name" ,nickName);
                users.set("avatar" ,headUrl);
                users.set("qqopenid" , openid);
                users.set("Addtime" ,new Date());
                users.set("UserNo" ,Db.queryLong("select count(*) FROM users")+1);
                Db.save("users",users);
                UsersIntegralService.AddIntegral(users.getStr("id"), 10);
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("data", users);

            }
        }catch (Exception ex){
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

    //小程序点赞功能
    @Before({POST.class})
    public void ThumbsUp() {
        String id = getPara("id");
        String userid = getPara("userid");
        Integer jf = getParaToInt("jf");
        if (userid==null){

        }else {
            setAttr("msg", "请重新登陆再试！");
            setAttr("success", true);
            setAttr("data", null);
        //更新点赞次数
        try
        {
         Record da=   Db.findById("a_article","ID",id);
            Integer i=da.getInt("thumbsup");
        if (i==null){
             i=1;
        }else {
            i=i+1;
        }
            ////更新阅读量
        da. set("thumbsup",i)  ;

            Db.update("a_article",da);
     }
        catch (Exception ex) { }
        //更新广告后的积分
        if (jf == 1) {
            if(UsersIntegralService.AddIntegral(userid, 10))
            {
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("data", null);

            }
            else
            {
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

    public void GetUserUsersIntegral() {
        String id = getPara("id");

        renderJson(  Db.findById("usersintegral","Userid",id));
    }

    public void Integral()
    {
        String id = getPara("id");
        if(UsersIntegralService.AddIntegral(id, 10))
        {
            setAttr("msg", "点赞成功！");
            setAttr("success", true);
            setAttr("data", null);

        }
        else {
            setAttr("msg", "点赞失败！");
            setAttr("success", true);
            setAttr("data", null);

        }
        renderJson();
    }


    public  void  GetBycode(){
        try {
            String code=getPara("code");
            List<Record> da= Db.find("SELECT *FROM dictionary where ParentID IN(SELECT id from dictionary where code='"+code+"' and IsUse=1) and IsUse=1");
            setAttr("msg", "成功！");
            setAttr("success", true);
            setAttr("data", da);
        }catch (Exception ex){
            setAttr("msg", "失败！");
            setAttr("success", true);
            setAttr("data", null);
        }


        renderJson();
    }
}
