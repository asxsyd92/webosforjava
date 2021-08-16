package com.webos.controllers.webos;

import com.alibaba.fastjson.JSON;

import com.asxsyd92.swagger.annotation.Api;
import com.asxsyd92.swagger.annotation.ApiOperation;
import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.ehcache.CacheKit;
import com.webcore.modle.*;
import com.webcore.service.ImService;
import com.webcore.service.LogService;
import com.webcore.service.RoleAppService;
import com.webcore.service.UsersService;
import com.webos.Common;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.jwt.JwtInterceptor;
import com.webos.socket.util.util;
import io.jsonwebtoken.Claims;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.asxsydutils.utils.MD5.MD5_32bit;
@Api(tag = "Users", description = "用户控制器")
@Before({JwtInterceptor.class, POST.class})
@Path("/api/users")
public class UsersControllers extends Controller {
    @Inject
    LogService logService;
@Inject
    RoleAppService roleAppService;
@Inject
    UsersService usersService;
@Inject
    ImService imService;
    public void GetAppList() {
        try {
            //从缓存中读取
            Claims claims = getAttr("claims");
            String userid = claims.get("id").toString();
            Object  menucache=   CacheKit.get("menucache",userid);

            if (menucache!=null){
                setAttr("msg", "");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", menucache);
                setAttr("success", true);

            }else {

                String Role = claims.get("role").toString();
                int role = Integer.parseInt(Role);
                List<Record> list = new ArrayList<>();
                Object da = null;
                if (role == 1) {
                    da = new JosnUtils<xRoleApp>().toJson(roleAppService.GetxAppList(""));
                } else {
                    da = new JosnUtils<RoleApp>().toJson(roleAppService.GetAppList(role));
                }
                //设置缓存
                CacheKit.put("menucache",userid,da);
                setAttr("msg", "");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", da);
                setAttr("success", true);
            }


        } catch (Exception ex) {

            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", ex.getMessage());
            setAttr("success", false);
        }
        renderJson();
    }

    /**
     * 获取菜单项
     */

    public void getAppList(){
        try {

            Claims claims = getAttr("claims");
            String Role = claims.get("role").toString();
            int role = Integer.parseInt(Role);
            List<Record> list = new ArrayList<>();

          Object  da = new JosnUtils<xRoleApp>().toJson(roleAppService.GetxAppList(""));
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
public void getPageById(){
    try {
        String id = getPara("id");
        int page = getInt("page");
        int limit = getInt("limit");
        Page<xRoleApp> da =  roleAppService.getPageById(page,limit,id);
        setAttr("msg", "获取成功");
        setAttr("code", 0);
        setAttr("count",da.getTotalRow());
        setAttr("data", da.getList());
        setAttr("success", true);
    }catch (Exception ex){
        setAttr("msg", ex.getLocalizedMessage());
        setAttr("code", 0);
        setAttr("count",0);
        setAttr("data", null);
        setAttr("success", false);
    }


    renderJson();
}
    public void UsersList() {
        try {
            //   Record record=   getModel(Record.class, "");
            int page = getParaToInt("page");
            int limit = getParaToInt("limit");
            String organizeid = getPara("organizeid");
            String title = getPara("title");
            Page<Record> tag = usersService.GetUserList(page, limit, Kv.by("organizeid",organizeid).set("title",title));
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


    public void OrganizeAndRole() {

        try {

            List<Record> tag = usersService.GetOrganizeAndRole();
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

     public void UsersSave() {
        try {

            String data = getPara("data");
            Claims claims = getAttr("claims");
            Record record = new Record();

            Map user = JSON.parseObject(data, Map.class);
            record.setColumns(user);
            //获取用户实体
            Users users=JSON.parseObject(data,Users.class);

            //新增
            if (users.getID().equals(StringUtil.GuidEmpty())){
                Record us = usersService.GetUserByAccount(users.getAccount()); //SqlFromData.GetFromData("S_Teacher", new { S_Account = ter["S_Account"].ToString().ToLower() }).FirstOrDefault();
                if (us != null) {
                    setAttr("msg", "系统中存在相同帐号，请更换账号再试！");
                    setAttr("success", false);
                    renderJson();
                }
                users.setID(StringUtil.getPrimaryKey());
                users.setAddtime(new Date());
           if (usersService.save(users,user)) {
               String logmsg =  "新增账号:" + users.getAccount() ;
               logService.addLog(logmsg, "更新详情："+JSON.toJSONString(users), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", JSON.toJSONString(users), "", "");

               setAttr("msg", "保存成功");
                        setAttr("success", true);
                    } else {
                        setAttr("msg", "保存失败");
                        setAttr("success", false);
                    }
            }
            else {
                if ( usersService.update(users)) {
                    String logmsg =  "更新账号:" + users.getAccount() ;
                    logService.addLog(logmsg, "更新详情："+JSON.toJSONString(users), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", JSON.toJSONString(users), "", "");
                    setAttr("msg", "保存成功");
                    setAttr("success", true);
                } else {
                    setAttr("msg", "保存失败");
                    setAttr("success", false);
                }

            }
//            if (record.getStr("ID").equals(StringUtil.GuidEmpty())) {
//                //检查帐号是否重复
//                Record us = usersService.GetUserByAccount(record.getStr("Account")); //SqlFromData.GetFromData("S_Teacher", new { S_Account = ter["S_Account"].ToString().ToLower() }).FirstOrDefault();
//                if (us != null) {
//                    setAttr("msg", "系统中存在相同帐号，请更换账号再试！");
//                    setAttr("success", false);
//
//                } else {
//                    logService.addLog("新增用户", "新增用户！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "新增", "", "", "");
//
//                    String id = UUID.randomUUID().toString();
//                    record.set("ID", id);
//                    String str = id.toLowerCase() + "123456";
//                    String newstr = MD5_32bit(str);
//                    //获取配置中的密码
//                    record.set("Account", user.get("Account").toString().toLowerCase());
//                    record.set("Password", newstr);
//
//                    Boolean ids = usersService.Insert(record);
//                    if (ids) {
//
//                        setAttr("msg", "保存成功");
//                        setAttr("Success", true);
//                    } else {
//                        setAttr("msg", "保存失败");
//                        setAttr("Success", false);
//                    }
//
//                }
//
//
//            }
//            else {
//                Boolean ids = usersService.Insert(record);
//                if (ids) {
//
//                    setAttr("msg", "保存成功");
//                    setAttr("success", true);
//                } else {
//                    setAttr("msg", "保存失败");
//                    setAttr("success", false);
//                }
//            }
        } catch (Exception ex) {
            setAttr("msg", ex.getMessage());
            setAttr("success", false);
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


    public void delMenu() {
        Claims claims = getAttr("claims");
        try {
            String data = getPara("data");

            RoleApp roleApp = JSON.parseObject(data, RoleApp.class);

        if (roleAppService.DelByID(roleApp)) {
            setAttr("msg", "成功！");
            setAttr("success", true);
            logService.addLog("菜单管理", "删除菜单管理："+roleApp.getTitle(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "2", "", JSON.toJSONString(roleApp), "");

        } else {
            setAttr("msg", "失败！");
            setAttr("success", false);
        }

        }catch (Exception ex){
            setAttr("msg", "失败！");
            setAttr("success", false);
            logService.addLog("菜单管理", "删除菜单管理报错：" + ex.getMessage(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", JSON.toJSONString(ex), "");

        }
        renderJson();
    }
    public void ddMenu() {
        Claims claims = getAttr("claims");
        try {
            String data = getPara("data");
            RoleApp dictionary = JSON.parseObject(data, RoleApp.class);

            if (roleAppService.Save(dictionary)) {
                setAttr("msg", "修改成功");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", null);
                setAttr("success", true);
                String logmsg = dictionary.getID().equals("") ? "新增:" + dictionary.getTitle() : "修改:" + dictionary.getTitle();
                logService.addLog("菜单管理", logmsg, Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", "", "");
                renderJson();
            } else {
                setAttr("msg", "修改失败");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", null);
                setAttr("success", false);
                renderJson();
            }
        } catch (Exception ex) {
            // String logmsg=dictionary.getID()==null?"新增:"+dictionary.getTitle():"修改:"+dictionary.getTitle();
            logService.addLog("菜单管理", "菜单管理报错：" + ex.getMessage(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", "", "");

            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);
            renderJson();
        }
        renderJson();

    }
    public void GetUsersTreeAsync() {
        try {
            List<Users> users = usersService.GetUsersTreeAsync();
            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", users);
            setAttr("success", true);
        } catch (Exception ex) {
            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);  setAttr("success", false);

        }
        renderJson();
    }
    @Before({JwtInterceptor.class, POST.class})
   @ApiOperation(url = "/api/users/GetUsersListAsync", tag = "GetUsersListAsync", httpMethod = "post", description = "需要授权：im用户列表 ")
    //获取用户朋友、群列表
    public void GetUsersListAsync()
    {
     try {
         Record data=new Record();
         Claims claims = getAttr("claims");
         String id=claims.get("id").toString();
        Sysim im= imService.getImUser(id);
         data.set("mine",im);
         //获取朋友
         Object  friends =  new util<Sysfriend>().toJson(imService.getImFriend(id));
         data.set("friend",friends);
         //获取群
         Object  groups =  new util<Sysgroup>().toJson(imService.getImgroup(id));
         data.set("group",groups);

            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", data);
        }catch (Exception ex){

            setAttr("msg", ex.getMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
         setAttr("success", false);
        }
        renderJson();
  }
    @Before({JwtInterceptor.class, POST.class})
    @ApiOperation(url = "/api/users/getMembers", tag = "GetUsersListAsync", httpMethod = "post", description = "需要授权：im用户列表 ")
    //获取群员
    public void getMembers()
    {
        try {
            String gid = getPara("id");
            Record data=new Record();
            Claims claims = getAttr("claims");
            String id=claims.get("id").toString();
            Sysim im= imService.getImUser(id);
            //群主
            data.set("owner",im);
            //获取群
            Object  groups =  new util<Sysim>().toJson(imService.getImgroupSysim(gid));
            data.set("list",groups);

            setAttr("msg", "获取成功");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", data);
        }catch (Exception ex){

            setAttr("msg", ex.getMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }
}