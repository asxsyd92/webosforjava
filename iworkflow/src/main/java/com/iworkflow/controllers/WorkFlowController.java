package com.iworkflow.controllers;


import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.iworkflow.service.modle.Workflow;
import com.iworkflow.service.oa.WorkflowService;
import com.security.Authorization;
import io.jsonwebtoken.Claims;

import java.util.List;
@Path("/api/workflow")
@Before({ POST.class, Authorization.class})
public class WorkFlowController extends Controller {
    public void WorkFlowList() {
        try {
            String title = getPara("title");
            String type = getPara("type");
            Integer page = getParaToInt("page");
            Integer limit = getParaToInt("limit");
            page = page == 0 ? 1 : page;
            limit = limit == 0 ? 15 : limit;
            String receiveid = "";
            Claims claims = getAttr("claims");
            receiveid = claims.get("id").toString();
            Kv kv = Kv.by("title", title).set("type", type).set("receiveid", receiveid);
            Page<Workflow> da = WorkflowService.WorkFlowList(kv, page, limit);
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("count", da.getTotalRow());
            setAttr("data", da.getList());

        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);

        }
        renderJson();
    }
    public void GetJSON() {
        try {
            String flowid = getPara("flowid");
            String type = getPara("type");


            Workflow da=   CacheKit.get("flowcache",flowid);
            if (da==null){
                da = WorkflowService.Get(flowid);
                CacheKit.put("flowcache",flowid,da);
            }
            String d = "0".equals(type) ? da.getRunJSON() : da.getDesignJSON();
            renderJson(d);
        } catch (Exception ex) {

            renderJson("{}");
        }

    }
    public void Save()
    {
        try {
            String json = getPara("json");
            String op = getPara("op");
            String receiveid = "";
            Claims claims = getAttr("claims");
            receiveid = claims.get("id").toString();
            op = op == null ? "" : op;
            String msg="";
            if (op.equals("save")) {

                msg= WorkflowService.Seve(json, receiveid);
            }
            if (op.equals("install")) {

                msg=  WorkflowService.Install(json, receiveid, false);
            }
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", msg);
            setAttr("success", true);
        }catch (Exception ex){
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);

        }
         renderJson(); //WorkFlowBll.Instance.GetJSON(flowid, type);
    }
    public void GetTable_SqlServer()
    {
       // return DBConnectionBll.Instance.GetTable_SqlServer();
    }
    public void GetFields() {
        String table = getPara("table");

        List<Record> da= Db.find("SELECT COLUMN_NAME f_name,COLUMN_COMMENT value FROM INFORMATION_SCHEMA.COLUMNS where table_name  = '"+table.toLowerCase()+"'");
        renderJson( da );  ;

    }
    public  void  Delinfo(){
        String id = getPara("keyid");
try{

 boolean mm=   WorkflowService.Del(id);
    setAttr("code", 0);
    setAttr("count", 0);
    setAttr("data", null);
    setAttr("msg", mm ?"删除成功":"删除失败");
    setAttr("success", true);
}catch (Exception ex){
    setAttr("code", 0);
    setAttr("count", 0);
    setAttr("data", null);
    setAttr("msg", ex.getMessage());
    setAttr("success", false);

}

renderJson();
    }

}
