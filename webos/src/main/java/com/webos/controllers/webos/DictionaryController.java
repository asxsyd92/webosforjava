package com.webos.controllers.webos;

import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.JosnUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.security.JwtInterceptor;
import com.webcore.modle.Dictionarys;
import com.webcore.modle.Dictionary;
import com.webcore.service.DictionaryService;
import com.webcore.service.LogService;
import com.asxsydutils.utils.Common;
import io.jsonwebtoken.Claims;

@Path("/api/dictionary")
@Before({ POST.class, JwtInterceptor.class})

public class DictionaryController  extends Controller {
    @Inject
    private DictionaryService service;
    @Inject
    LogService logService;
    public void GetByCode() throws Exception {
       String code=getPara("code");

       Object da =  new JosnUtils<Dictionarys>().toJson(service.GetByCode(code));

        setAttr("data", da);
        setAttr("success", true);

    renderJson();
    }


    public void getPageById()  {
        try {
            String id = getPara("id");
            int page = getInt("page");
            int limit = getInt("limit");
            Page<Dictionary> da =  service.getPageById(page,limit,id);
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

        setAttr("success", true);

        renderJson();
    }

    public void AddDictionary() {
        Claims claims = getAttr("claims");
        try {
            String data = getPara("data");
            Dictionary dictionary = JSON.parseObject(data, Dictionary.class);

            if (service.Save(dictionary)) {
                setAttr("msg", "修改成功");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", null);
                setAttr("success", true);
                String logmsg = dictionary.getID().equals("") ? "新增:" + dictionary.getTitle() : "修改:" + dictionary.getTitle();
                logService.addLog("数据字典", logmsg, Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", "", "");
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
            logService.addLog("数据字典", "数据字典报错：" + ex.getMessage(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", "", "");

            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);
            renderJson();
        }
        renderJson();

    }
    public void delDictionary(){
        Claims claims = getAttr("claims");
        try {
            String data = getPara("data");

            Dictionary dictionary = JSON.parseObject(data, Dictionary.class);
            if (service.Del(dictionary.getID())) {
                setAttr("msg", "删除字典项成功");
                setAttr("code", 0);
                setAttr("count", 0);
                setAttr("data", null);
                setAttr("success", true);
              //  String logmsg = dictionary.getID().equals("")  ? "新增:" + dictionary.getTitle() : "修改:" + dictionary.getTitle();
                logService.addLog("数据字典", "删除字典项："+dictionary.getTitle(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "2", "", JSON.toJSONString(dictionary), "");
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
            logService.addLog("数据字典", "删除数据字典报错：" + ex.getMessage(), Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "1", "", JSON.toJSONString(ex), "");

            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);        renderJson();
        }
        renderJson();
    }

    public void GetTypeOptions()
    {
        try {
            String value = getPara("value");
            Record d= DictionaryService.GetOptionsByCode("FlowTypes", value);
            renderJson(d);
        } catch (Exception ex) {

            renderJson("{}");
        }
        //return  DictionaryService.Instance.GetOptionsByCode("FlowTypes", value);
    }
}
