package com.iworkflow.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asxsydutils.config.LoginUsers;
import com.asxsydutils.utils.Common;
import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.google.gson.GsonBuilder;
import com.iworkflow.service.oa.workflow.*;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.iworkflow.service.modle.WorkFlowTask;
import com.iworkflow.service.modle.Workflow;
import com.iworkflow.service.oa.WorkFlowTaskService;
import com.iworkflow.service.oa.WorkflowService;
import com.iworkflow.service.oa.task.Query;
import com.iworkflow.service.oa.task.Result;
import com.security.Authorization;
import io.jsonwebtoken.Claims;
import kotlin.collections.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


@Path("/api/workflowtasks")
@Before({ POST.class, Authorization.class})
public class WorkFlowTaskController extends Controller {
    @Inject
    WorkFlowTaskService workFlowTaskService;
    //获取待办事项
    public  void  WaitList(){
try {
    String title = getPara("title");
    String type = getPara("type");
    Integer page =  getParaToInt("page");
    Integer limit = getParaToInt("limit");
    page=  page==0?1:page;
    limit=limit==0?15:limit;
    String receiveid="";  Claims claims = getAttr("claims");
    receiveid=claims.get("id").toString();
    Kv kv= Kv.by("title",title).set("type",type).set("receiveid",receiveid);
    Page<WorkFlowTask> da= WorkFlowTaskService.getWaitList(kv,page,limit);
    setAttr("code", 0);
    setAttr("msg", "成功！");
    setAttr("count", da.getTotalRow());
    setAttr("data", da.getList());
    setAttr("success",true);
}catch (Exception ex){
    setAttr("code", 0);
    setAttr("count", 0);
    setAttr("data", null);
    setAttr("msg",ex.getMessage()) ;
    setAttr("success",false);
}

        renderJson();
    }
    public void CompletedList()
    {
        try {
        String title = getPara("title");
        String user = getPara("user");
        String flowid = getPara("flowid");
        Date date1 = getDate("date1");
        Date date2 = getDate("date2");
        Integer page =  getParaToInt("page");
        Integer limit = getParaToInt("limit");
            page=  page==0?1:page;
            limit=limit==0?15:limit;
        //根据用户获取待办
        String receiveid="";  Claims claims = getAttr("claims");
        receiveid=claims.get("id").toString();
        Kv kv= Kv.by("title",title).set("flowid",flowid).set("user",user).set("date1",date1).set("date2",date2).set("receiveid",receiveid);
        Page<WorkFlowTask> da= workFlowTaskService.CompletedList(kv,page,limit);
        setAttr("code", 0);
        setAttr("msg", "成功！");
        setAttr("count", da.getTotalPage());
        setAttr("data", da.getList());
            setAttr("success",true);
    }catch (Exception ex){
        setAttr("code", 0);
        setAttr("count", 0);
        setAttr("data", null);
        setAttr("msg",ex.getMessage()) ;
        setAttr("success",false);

    }
        renderJson();
    }



    //初始流程
    //@Before({Authentication.class, POST.class})
    public void  FlowInit(){
        try
        {
            LoginUsers usersModel= Common.getLoginUser(this);
            Record formdata=null;
            String flowid = getPara("flowid");
            String stepid = getPara("stepid");
            String instanceid= getPara("instanceid");
            Workflow wfl=   CacheKit.get("flowcache",flowid.toLowerCase());
            if (wfl==null){
                wfl= WorkflowService.Get(flowid.toLowerCase());
                CacheKit.put("flowcache",flowid.toLowerCase(),wfl);
            }
           // Workflow wfl = WorkflowService.Get(flowid);
            String error = "";
            String wfInstalled = wfl.getRunJSON();
            RunModel wfInstalleds= JosnUtils.stringToBean(wfInstalled,RunModel.class);
            //WorkFlowBll.Instance.GetWorkFlowRunModel(wfl.RunJSON, out error);

            if (wfl == null)
            {
                //  Response.Write("未找到流程运行时!");

            }
            else if (wfl.getStatus().equals(3))
            {
               // return JSONhelper.ToJson(new { code = 0, msg = "该流程已被卸载,不能发起新的流程实例", count = 0, data = new { } }, false);
            }
            else if (wfl.getStatus().equals(4))
            {
              //  return JSONhelper.ToJson(new { code = 0, msg = "该流程已被删除,不能发起新的实例", count = 0, data = new { } }, false);

            }
            String stepID="";
            List<Steps> step=wfInstalleds.getSteps();
            if (stepid==null)
            {

                stepID=   step.get(0).getId();

            }else {
                if (stepid.equals(StringUtil.GuidEmpty())||stepid.equals("undefined")||stepid.equals("")){
                    stepID=   step.get(0).getId();
                }else {
                stepID=stepid;}
            }
            String finalStepID = stepID.toLowerCase();
            List<Steps>  da=step.stream().filter(Steps -> Steps.getId().equals(finalStepID)).collect(Collectors.toList());
           String currentStep =da.size()>0?da.get(0).getId().toLowerCase():null;
            Steps currentdata = null;
           if (currentStep!=null){
               currentdata=da.get(0);
           }
            List<Steps> nextSteps =new ArrayDeque<>();

            List<Lines> lines = wfInstalleds.getLines().stream().filter(Lines -> Lines.getFrom().equals( currentStep)).collect(Collectors.toList());
            for (Lines item:  lines)
            {
                List<Steps>  step1 = wfInstalleds.getSteps().stream().filter(Steps -> Steps.getId().equals( item.getTo())).collect(Collectors.toList());
                if (step1.size() > 0)
                {
                    nextSteps.add(step1.get(0));
                }
            }
             // stepList=stepList.stream().sorted(Steps  -> Steps.getPosition())
             //   String nextSteps = WorkFlowBll.Instance.GetNextSteps(wfInstalled.id.ToGuid(), currentStep.id.ToGuid());
             //获取表单
           String key= currentdata.getForms().get(0).getId();
            Record fmdata= Db.findById("sysformdesign","id",key);
            if (fmdata!=null){

                RunJson json= JSON.parseObject(fmdata.get("DesignHtml"), RunJson.class);
                JSONObject from= (JSONObject) json.getFrom().get("data");
                if (from!=null){
                   String table= from.get("table").toString();
                   if (instanceid!=null&&!instanceid.equals("undefined")&&!instanceid.equals("")){
                       formdata= Db.findById(table, "ID", instanceid);
                   }

                }
                List<Map<String,Object>>listdata=new ArrayDeque<>();
                    for (Map<String,Object> item:json.getData()  ) {
                        //赋值
                        try {
                            JSONObject fd = (JSONObject) item.get("data");
                            if (fd.get("name") != null && fd.get("name").toString() != null) {
                                if (formdata!=null) {
                                    fd.put("value", formdata.get(fd.get("name").toString()));
                                }else {
                                    switch (fd.get("value").toString())
                                    {

                                        case "@_SYS_DATETIME":
                                            fd.put("value", new Date());
                                           break;
                                        case "@_SYS_ORGNAME":
                                            fd.put("value", usersModel.getOrgname());
                                            break;
                                        case "@_SYS_GW":
                                            fd.put("value", "");
                                            break;
                                        case "@_SYS_GETUSERID":
                                            fd.put("value", usersModel.getId());
                                            break;
                                        case "@_SYS_ORGID":
                                            fd.put("value", usersModel.getOrgid());
                                            break;
                                        case "@_SYS_GETUSERNICKNAME":
                                            fd.put("value", usersModel.getName());
                                            break;
                                        case "@_SYS_GETUSERNAME":
                                            fd.put("value", usersModel.getAccount());
                                            break;

                                    }

                                }
                                //设置字段状态
                                List<FieldStatus> field = currentdata.getFieldStatus().stream().filter(fiele -> fiele.getField().equals(fd.get("name").toString())).collect(Collectors.toList());
                                if (field.size() > 0) {
                                    fd.put("showtext", field.get(0).getStatus());
                                    fd.put("required", field.get(0).getStatus().equals("0") ? "false" : "true");
                                }
                                item.put("data", fd);
                                listdata.add(item);
                            }
                        }catch (Exception ex){
                            System.out.print(ex.getMessage());
                        }


                    }
                json.setData(listdata);
                fmdata.set("DesignHtml",new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(json));
            }
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", nextSteps);
            setAttr("currentStep", currentStep);
            setAttr("currentdata", currentdata);
            setAttr("formdata", fmdata);
            setAttr("msg","成功") ;
            setAttr("success",true);
          //  return JSONhelper.ToJson(new { code = 0, msg = "成功", count = 1, data = nextSteps, currentStep = currentStep }, false);

        }
        catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg","未找到流程步骤") ;
            setAttr("success",false);
          //  return JSONhelper.ToJson(new { code = 0, msg = "未找到流程步骤", count = 0, data = "", currentStep = "" }, false);
        }
        renderJson();
    }
/*
流程处理
 */
    //@Before({Authentication.class, POST.class})
public  void sendTask() {
        try {
            String receiveid = "";
            Claims claims = getAttr("claims");
            receiveid = "EB03262C-AB60-4BC6-A4C0-96E66A4229FE";//claims.get("id").toString();
            String query = getPara("query");
            String table = getPara("table");
            String data = getPara("data");

            //data= Unity.getJsonSetData(data,claims);
            String params1 = getPara("params1");
            Result result = workFlowTaskService.sendTask(receiveid, query, table, data, params1);

            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", result.getNexttasks());
            setAttr("msg", result.getMessages());
            setAttr("success", result.getIssuccess());


        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);
        }
        renderJson();
    }
    /*
    获取业务表单数据
     */
    public void getcomment(){
        try {
        String query = getPara("query");
            List<WorkFlowTask> da= workFlowTaskService.getcomment(query);
            setAttr("code", 0);
            setAttr("count", 1);
            setAttr("data", da);
            setAttr("msg", "获取成功");
            setAttr("success", true);
    } catch (Exception ex) {
        setAttr("code", 0);
        setAttr("count", 0);
        setAttr("data", null);
        setAttr("msg", ex.getMessage());
        setAttr("success", false);
    }
    renderJson();
    }
/*
/流程退回
 */
public void FlowBack(){
        try {
            String receiveid = "";
            Claims claims = getAttr("claims");
            receiveid = claims.get("id").toString();
            String query = getPara("query");
            String table = getPara("table");
            String data = getPara("data");

           // data= Unity.getJsonSetData(data,claims);
            String params1 = getPara("params1");
            Result result = workFlowTaskService.sendTask(receiveid, query, table, data, params1);

            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", result.getNexttasks());
            setAttr("msg", result.getMessages());
            setAttr("success", result.getIssuccess());


        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);
        }
        renderJson();
    }
    /*
    /获取可退回的步骤
     */
    public void getBackStepList() {
        try {
            String query = getPara("query");
            Query querys = JSON.parseObject(query, Query.class);
            String flowid = querys.getFlowid();
            Integer type = getParaToInt("type");

            Workflow wfl=   CacheKit.get("flowcache",flowid.toLowerCase());
            if (wfl==null){
                wfl= WorkflowService.Get(flowid.toLowerCase());
                CacheKit.put("flowcache",flowid.toLowerCase(),wfl);
            }

            RunModel wfInstalled = JosnUtils.stringToBean(wfl.getRunJSON(), RunModel.class);
            List<Record> da = workFlowTaskService.GetBackSteps(querys.getTaskid(), type, querys.getStepid(), wfInstalled);
            if (da.size()>0) {
                //当第一步骤不能退回
                if (da.get(0).getStr("id").equals(StringUtil.GuidEmpty())) {
                    setAttr("code", 0);
                    setAttr("count", 0);
                    setAttr("data", da);
                    setAttr("msg", "第一个步骤不能退回");
                    setAttr("success", false);
                } else {
                    setAttr("code", 0);
                    setAttr("count", 0);
                    setAttr("data", da);
                    setAttr("msg", "成功");
                    setAttr("success", true);
                }
            }
            else{
                    setAttr("code", 0);
                    setAttr("count", 0);
                    setAttr("data", da);
                    setAttr("msg", "没有找到可退回的步骤");
                    setAttr("success", false);
                }


        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);
        }
        renderJson();
    }
    public void GetTaskList(){
        try {
            String flowid = getPara("flowid");
            String groupid = getPara("groupid");
           List<WorkFlowTask> da=workFlowTaskService.GetTaskList(flowid, groupid).stream().sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(Collectors.toList());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", da);
            setAttr("msg","成功");
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
