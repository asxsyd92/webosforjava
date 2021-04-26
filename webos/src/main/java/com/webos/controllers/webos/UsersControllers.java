package com.webos.controllers.webos;

import com.alibaba.fastjson.JSON;

import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Inject;
import com.webcore.annotation.Route;
import com.webcore.service.LogService;
import com.webcore.service.RoleAppService;
import com.webcore.service.UsersService;
import com.webcore.modle.RoleApp;
import com.webcore.modle.Users;
import com.webcore.modle.xRoleApp;
import com.webos.Common;
import com.webos.socket.user.FriendItem;
import com.webos.socket.user.Im;
import com.webos.socket.user.Mine;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.jwt.JwtInterceptor;
import io.jsonwebtoken.Claims;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static com.asxsydutils.utils.MD5.MD5_32bit;


@Route(Key = "/api/users")
public class UsersControllers extends Controller {
    @Inject
    LogService logService;
    @Before({JwtInterceptor.class, POST.class})

    public void GetAppList() {
        try {

            Claims claims = getAttr("claims");
            String Role = claims.get("role").toString();
            int role = Integer.parseInt(Role);
            List<Record> list = new ArrayList<>();
            Object da = null;
            if (role == 1) {
                da = new JosnUtils<xRoleApp>().toJson(RoleAppService.GetxAppList(role));
            } else {
                da = new JosnUtils<RoleApp>().toJson(RoleAppService.GetAppList(role));
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
    public void UsersList() {
        try {
            //   Record record=   getModel(Record.class, "");
            int page = getParaToInt("page");
            int limit = getParaToInt("limit");
            // String title = getPara("title");
            Page<Record> tag = UsersService.GetUserList(page, limit, "");
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
    public void OrganizeAndRole() {

        try {

            List<Record> tag = UsersService.GetOrganizeAndRole();
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
    public void UsersSave() {
        try {
            String data = getPara("data");
            Claims claims = getAttr("claims");
            Record record = new Record();

            Map user = JSON.parseObject(data, Map.class);
            record.setColumns(user);
            if (record.getStr("ID").equals(StringUtil.GuidEmpty())) {
                //检查帐号是否重复
                Record us = UsersService.GetUserByAccount(record.getStr("Account")); //SqlFromData.GetFromData("S_Teacher", new { S_Account = ter["S_Account"].ToString().ToLower() }).FirstOrDefault();
                if (us != null) {
                    setAttr("msg", "系统中存在相同帐号，请更换账号再试！");
                    setAttr("Success", false);

                } else {
                    logService.addLog("新增用户", "新增用户！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "新增", "", "", "");

                    String id = UUID.randomUUID().toString();
                    record.set("ID", id);
                    String str = id.toLowerCase() + "123456";
                    String newstr = MD5_32bit(str);
                    //获取配置中的密码
                    record.set("Account", user.get("Account").toString().toLowerCase());
                    record.set("Password", newstr);

                    Boolean ids = UsersService.Insert(record);
                    if (ids) {

                        setAttr("msg", "保存成功");
                        setAttr("Success", true);
                    } else {
                        setAttr("msg", "保存失败");
                        setAttr("Success", true);
                    }

                }


            } else {
                Boolean ids = UsersService.Insert(record);
                if (ids) {

                    setAttr("msg", "保存成功");
                    setAttr("Success", true);
                } else {
                    setAttr("msg", "保存失败");
                    setAttr("Success", true);
                }
            }
        } catch (Exception ex) {
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
                var ps = Asxsyd92Core.Utils.Common.MD5System(Asxsyd92Core.Utils.Common.EncryptionPassword(account.ToString().ToLower()) + Asxsyd92Core.Utils.Common.EncryptionPassword(pw));
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

    @Before({JwtInterceptor.class, POST.class})
    public void EditPw() {
        Claims claims = getAttr("claims");
        String id = claims.get("id").toString();
        String old = getPara("old");
        String pw = getPara("pw");

        try {

            Record da = Db.findById("Users", "id", id);
            if (da != null) {
                //加密后的字符串
                String str = da.getStr("ID").toLowerCase() + old;
                String newstr = MD5_32bit(str);
                if (da.getStr("Password").equals(newstr)) {
                    String nstr = da.getStr("ID").toLowerCase() + pw;
                    String newstrs = MD5_32bit(nstr);
                    da.set("Password", newstrs);
                    if (Db.update("Users", "ID", da)) {
                        setAttr("msg", "修改成功！");
                        setAttr("success", true);
                        logService.addLog("用户修改密码", "修改成功！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "修改", "", "", "");

                    } else {
                        logService.addLog("用户修改密码", "修改失败！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "修改", "", "", "");

                        setAttr("msg", "修改失败！");
                        setAttr("success", false);
                    }

                } else {
                    setAttr("msg", "原密码错误！");
                    setAttr("success", false);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        renderJson();
    }

    @Before({JwtInterceptor.class, POST.class})
    public void DelMenu() {
        String iD = getPara("id");
        if (RoleAppService.DelByID(iD)) {
            setAttr("msg", "成功！");
            setAttr("success", true);
        } else {
            setAttr("msg", "失败！");
            setAttr("success", false);
        }
        renderJson();
    }

    public void GetUsersTreeAsync() {
        try {
            List<Users> users = UsersService.GetUsersTreeAsync();
            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", users);
            setAttr("success", true);
        } catch (Exception ex) {
            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);

        }
        renderJson();
    }

    public void GetUsersListAsync()
    {
     try {
            System.out.println("jn");
        List<Record> da = UsersService.getUserAll();//.GetUsersList("", "", 0, 100);
       // Claims claims = getAttr("claims");
            String id = getPara("id");
        List<Record> my=  da.stream().filter(c->c.get("id").equals(id)).collect(Collectors.toList());
        Mine mine = new Mine();
        if (my.size()>0){
            Record m=my.get(0);
            mine.username = m.getStr("name");
            mine.avatar = "http://cdn.firstlinkapp.com/upload/2016_6/1465575923433_33812.jpg";
            mine.id = m.getStr("id");
            mine.sign = m.getStr("sign");
            mine.status = "1";
        }

        //查找组织架构

    Map<String, List<Record>> map =da.stream().collect(Collectors.groupingBy(c->c.get("organize")));
        Set<Map.Entry<String, List<Record>>> entry =  map.entrySet();
        List<FriendItem>  j= new ArrayList();
        for (Map.Entry<String, List<Record>> m : entry) {
            FriendItem o=new FriendItem();
            o.groupname=m.getKey();
            o.id=m.getValue().get(0).getStr("id");
          o.online=1;
          o.list=m.getValue();
          j.add(o);
        }
        List<Im> list = new ArrayList<Im>();
        Im o=new Im();
        o.mine=mine;
        o.group="";
        o.friend=j;
            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", o);
        }catch (Exception ex){

            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
        }
        renderJson();
     //   return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = da.Count == 0 ? 0 : da.First().Count, data = list[0] }, false);
    }

}