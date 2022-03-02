package com.webos.controllers.webos;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.security.JwtInterceptor;
import com.webcore.modle.Log;
import com.webcore.service.LogService;

import java.util.Date;
@Path("/api/log")
@Before({ POST.class, JwtInterceptor.class})
public class LogControllers extends Controller {
    @Inject
    LogService logService;

    public void getLogList(){
        try{
        String title = getPara("title");
        String type = getPara("type");
        Date date1 = getDate("date1");
        Date date2 = getDate("date2");
        String desc = getPara("desc");
        int page = getInt("page");
        int limit = getInt("limit");
        Kv kv= Kv.by("title",title).set("type",type).set("date1",date1).set("date2",date2).set("desc",desc);

        Page<Log> da = logService.getLogList(page, limit,kv);
        setAttr("msg", "获取成功");
        setAttr("code", 0);
        setAttr("count",da.getTotalRow());
        setAttr("data", da.getList());
        setAttr("success", true);}catch (Exception ex){

            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count",0);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }
}
