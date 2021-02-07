package com.asxsyd92.controllers.oa;

import com.asxsyd92.annotation.Route;
import com.asxsyd92.service.WorkflowService;
import com.asxsyd92.modle.Workflow;
import com.asxsyd92.utils.data.MySql;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jwt.JwtInterceptor;
import io.jsonwebtoken.Claims;

@Route(Key="/api/workflow")
@Before({JwtInterceptor.class})
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

            Workflow da = WorkflowService.Get(flowid);
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
    public void GetTypeOptions()
    {
        //return  DictionaryService.Instance.GetOptionsByCode("FlowTypes", value);
    }
    public void GetTable_SqlServer()
    {
       // return DBConnectionBll.Instance.GetTable_SqlServer();
    }
    public void GetFields() {
        String table = getPara("table");
     renderJson( MySql.getMySqlableStructure(table));  ;

    }
}
