package com.webos.controllers.webos;

import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.webos.jwt.JwtInterceptor;
import com.webcore.config.LoginUsers;
import com.webcore.service.LogService;
import com.webos.Common;

import java.util.Date;

@Before({JwtInterceptor.class, POST.class})
@Path("/api/formdesign")
public class FormDesignController extends Controller {
    @Inject
    LogService logService;
    @ActionKey("fromsave")
    public void FormSave(){
        try{
        LoginUsers usersModel= Common.getLoginUser(this);
        String json = getPara("json");
        String table = getPara("table");
            String title = getPara("title");
            Record record=new Record();
            record.set("Id", StringUtil.getPrimaryKey());
            record.set("DesignHtml",json);
            record.set("RunHtml",json);
            record.set("Title",title);
            record.set("Tab",table);
            record.set("UserID",usersModel.getId());
            record.set("DateTime",new Date());
           if( Db.save("sysformdesign",record)){
               setAttr("msg", "成功");
               setAttr("code", 0);
               setAttr("count", 0);
               setAttr("data", null);
               setAttr("success", false);
           }else {
               setAttr("msg", "保存失败！");
               setAttr("code", 0);
               setAttr("count", 0);
               setAttr("data", null);
               setAttr("success", false);

           }
    }catch (Exception ex){
            setAttr("success", false);
            setAttr("msg", ex.getMessage());
        }
    }
}
