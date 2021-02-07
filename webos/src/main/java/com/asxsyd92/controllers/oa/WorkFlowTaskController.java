package com.asxsyd92.controllers.oa;

import com.asxsyd92.annotation.Route;
import com.asxsyd92.service.WorkFlowTaskService;
import com.asxsyd92.service.WorkflowService;
import com.asxsyd92.modle.WorkFlowTask;
import com.asxsyd92.modle.Workflow;
import com.asxsyd92.oa.task.Result;
import com.asxsyd92.oa.workflow.Lines;
import com.asxsyd92.oa.workflow.RunModel;
import com.asxsyd92.oa.workflow.Steps;
import com.asxsyd92.utils.JosnUtils;
import com.asxsyd92.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jwt.JwtInterceptor;
import com.spire.ms.System.Collections.ArrayList;
import io.jsonwebtoken.Claims;

import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;


@Route(Key="/api/workflowtasks")
public class WorkFlowTaskController extends Controller {
    //获取待办事项
    @Before({JwtInterceptor.class})
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
        Page<WorkFlowTask> da= WorkFlowTaskService.CompletedList(kv,page,limit);
        setAttr("code", 0);
        setAttr("msg", "成功！");
        setAttr("count", da.getTotalPage());
        setAttr("data", da.getList());
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
    @Before({JwtInterceptor.class, POST.class})
    public void  FlowInit(){
        try
        {
            String flowid = getPara("flowid");
            String stepid = getPara("stepid");
            Workflow wfl = WorkflowService.Get(flowid);
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
                if (stepid.equals(StringUtil.GuidEmpty())){
                    stepID=   step.get(0).getId();
                }else {
                stepID=stepid;}
            }
            String finalStepID = stepID.toLowerCase();
            List<Steps>  da=step.stream().filter(Steps -> Steps.getId().equals(finalStepID)).collect(Collectors.toList());
           String currentStep =da.size()>0?da.get(0).getId().toLowerCase():null;
            List<Steps> nextSteps =new ArrayList();

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

            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", nextSteps);
            setAttr("currentStep", currentStep);
            setAttr("msg","成功") ;
            setAttr("success",false);
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

    @Before({JwtInterceptor.class, POST.class})
    public  void sendTask(){
        try {
            String receiveid="";  Claims claims = getAttr("claims");
            receiveid=claims.get("id").toString();
            String query = getPara("query");
            String table = getPara("table");
            String data = getPara("data");
            String params1 = getPara("params1");
            Result result= WorkFlowTaskService.sendTask(receiveid,query,table,data,params1);

            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", result.getNexttasks());
            setAttr("msg",result.getMessages()) ;
            setAttr("success",result.getIssuccess());


        }catch (Exception ex){
        setAttr("code", 0);
        setAttr("count", 0);
        setAttr("data", null);
        setAttr("msg",ex.getMessage()) ;
        setAttr("success",false);
    }


        renderJson();
}
}
