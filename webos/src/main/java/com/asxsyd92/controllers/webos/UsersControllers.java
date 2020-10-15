package com.asxsyd92.controllers.webos;

import com.alibaba.fastjson.JSON;
import com.asxsyd92.annotation.Route;
import com.asxsyd92.bll.RoleAppBll;
import com.asxsyd92.bll.UsersBll;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtInterceptor;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.asxsyd92.utils.MD5.MD5_32bit;


@Route(Key = "/api/User")
public class UsersControllers extends Controller {

    @Before({JwtInterceptor.class, POST.class})

    public void GetAppList() {
        try {

            Claims claims = getAttr("claims");
            String Role = claims.get("role").toString();
            int role = Integer.parseInt(Role);
            List<Record> list = new ArrayList<>();
            List<Record> da = UsersBll.Instance().GetAppList(role);
            for (Record perm : da) {
                List<Record> id = UsersBll.Instance().getChildren(perm.getStr("ID"), Integer.parseInt(Role));
                perm.set("children", id);
                list.add(perm);
            }
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", da);
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
    @Before({JwtInterceptor.class, GET.class})

    public void UserList() {
        try {
            int page = getParaToInt("page");
            int limit = getParaToInt("limit");
           // String title = getPara("title");
            Page<Record> tag = UsersBll.Instance().GetUserList(page, limit, "");
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", tag.getTotalPage());
            setAttr("data", tag.getList());
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
    @Before({JwtInterceptor.class, POST.class})
   // @ActionKey("api/Users/OrganizeAndRole")

    public void OrganizeAndRole()
    {

        try {

            List<Record> tag = UsersBll.Instance().GetOrganizeAndRole();
            setAttr("msg", "1部门，0角色");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", tag.toArray());
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
    @Before({JwtInterceptor.class, POST.class})
    //@ActionKey("api/Users/UsersSave")
    public  void UsersSave(){
        try {
            String data=getPara("data");

            Record record=new Record();

            Map user = JSON.parseObject(data, Map.class);
            record.setColumns(user);
          if (record.getStr("ID")=="00000000-0000-0000-0000-000000000000"){
              //检查帐号是否重复
              Record us = UsersBll.Instance().GetUserByAccount(record.getStr( "Account")); //SqlFromData.GetFromData("S_Teacher", new { S_Account = ter["S_Account"].ToString().ToLower() }).FirstOrDefault();
              if (us != null)
              {   setAttr("msg", "系统中存在相同帐号，请更换账号再试！");
                  setAttr("Success", false);

              }else {
                  String id=UUID.randomUUID().toString();
                  record.set("ID",id);
                  String str = id.toLowerCase() + "123456";
                  String newstr= MD5_32bit(str);
                  //获取配置中的密码
                  record.set("Account",user.get("Account").toString().toLowerCase());
                  record.set("Password" , newstr);

                  Boolean ids = UsersBll.Instance().Insert(record);
                  if (ids)
                  {

                      setAttr("msg", "保存成功");
                      setAttr("Success", true);
                  }
                  else {
                      setAttr("msg", "保存失败");
                      setAttr("Success", true);
                  }

              }



          }
else {
              Boolean ids = UsersBll.Instance().Insert(record);
              if (ids)
              {

                  setAttr("msg", "保存成功");
                  setAttr("Success", true);
              }
              else {
                  setAttr("msg", "保存失败");
                  setAttr("Success", true);
              }
          }
        }catch (Exception ex){
            setAttr("msg", ex.getMessage());
            setAttr("Success", true);
        }

     /*   data = Models.Unity.OperationJson(data);
        var ter = Newtonsoft.Json.JsonConvert.DeserializeObject<Users>(data);
        if (ter != null)
        {
            //新增初始化密码
            if (ter.ID == Guid.Empty)
            {
                //检查帐号是否重复
                var us = WebOS.Dal.UsersDal.Instance.Get(new { Account = ter.Account.ToLower() }); //SqlFromData.GetFromData("S_Teacher", new { S_Account = ter["S_Account"].ToString().ToLower() }).FirstOrDefault();
                if (us != null)
                {
                    return JSONhelper.ToJson(new { msg = "系统中存在相同帐号，请更换账号再试！", Success = false });
                }

                //获取配置中的密码
                var pw = Asxsyd92Core.Utils.Provider.AppSetting.GetConfig("ConfigPassword");//ter["S_PW"].ToString();
                var account = ter.Account.ToString();
                var ps = Asxsyd92Core.Utils.Tools.MD5System(Asxsyd92Core.Utils.Tools.EncryptionPassword(account.ToString().ToLower()) + Asxsyd92Core.Utils.Tools.EncryptionPassword(pw));
                ter.Password = ps;
                ter.Account = ter.Account.ToLower();
                ter.ID = Guid.NewGuid();
                var id = WebOS.Dal.UsersDal.Instance.Insert(ter);
                if (id > 0)
                {

                    var ors=  WebOS.Dal.UsersRelationDal.Instance.Get(new { OrganizeID= ter.OrganizeID, ID = ter.ID });
                    if (ors == null)
                    {
                        UsersRelation usersRelation = new UsersRelation();
                        usersRelation.OrganizeID = ter.OrganizeID;
                        usersRelation.ID = ter.ID;
                        usersRelation.IsMain = 1;
                        usersRelation.Sort = 1;
                        WebOS.Dal.UsersRelationDal.Instance.Insert(usersRelation);
                    }
                    else {
                        ors.OrganizeID = ter.OrganizeID;
                        WebOS.Dal.UsersRelationDal.Instance.Update(ors);
                    }
                    return JSONhelper.ToJson(new { msg = "添加失败！", Success = true });
                }
                else { return JSONhelper.ToJson(new { msg = "添加失败！", Success = false }); }
            }
            else
            {
                var ks = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<object, object>>(data);
                if (ks == null) {
                    return JSONhelper.ToJson(new { msg = "数据解析失败！", Success = false });
                }
                ks["Account"] = ks["Account"].ToString().ToLower();
                var iss = SqlFromData.SaveFromData("Users", ks.ToJson());
                if (iss == Guid.Empty)
                {
                    return JSONhelper.ToJson(new { msg = "添加失败！", Success = false });
                }
                else
                {
                    var ors = WebOS.Dal.UsersRelationDal.Instance.Get(new { OrganizeID = ks["OrganizeID"].ToString().ToGuid(), ID = iss });
                    if (ors == null)
                    {
                        UsersRelation usersRelation = new UsersRelation();
                        usersRelation.OrganizeID = ter.OrganizeID;
                        usersRelation.ID = Guid.NewGuid();
                        usersRelation.UserID = ter.ID;
                        usersRelation.IsMain = 1;
                        usersRelation.Sort = 1;
                        WebOS.Dal.UsersRelationDal.Instance.Insert(usersRelation);
                    }
                    else
                    {
                        ors.OrganizeID = ter.OrganizeID;
                        ors.UserID = ter.ID;

                        WebOS.Dal.UsersRelationDal.Instance.Update(ors);
                    }
                    return JSONhelper.ToJson(new { msg = "添加成功！", Success = true });

                }
            }

        }
        else { return JSONhelper.ToJson(new { msg = "数据解析失败！", Success = false }); }
*/
        renderJson();
    }


    public  void  GetToMenu(){
         RoleAppBll.Instance().GetAllApp();
    }
}