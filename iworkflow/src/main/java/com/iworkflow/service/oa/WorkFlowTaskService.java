package com.iworkflow.service.oa;
import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.iworkflow.service.modle.WorkFlowTask;
import com.iworkflow.service.modle.Workflow;
import com.iworkflow.service.oa.task.*;
import com.iworkflow.service.oa.workflow.*;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.ehcache.CacheKit;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.iworkflow.service.modle.WorkFlowArchives;

import com.webcore.modle.Users;
import com.webcore.service.OrganizeService;
import com.webcore.service.UsersService;
import com.webcore.utils.data.mysqlserver.FromData;
import kotlin.collections.ArrayDeque;

import java.util.*;
import java.util.stream.Collectors;


public class WorkFlowTaskService {
    private static volatile WorkFlowTask instance = new WorkFlowTask().dao();

    @Inject
    UsersService usersService;
    @Inject
    OrganizeService organizeService;
    public static Page<WorkFlowTask> getWaitList(Kv kv, Integer page, Integer limit) {

        SqlPara sqlPara =  instance.getSqlPara("oa-workflowtask.getwaitlist", kv);
        return   instance.paginate(page,limit,sqlPara);
    }

    public static Page<WorkFlowTask> CompletedList(Kv kv, Integer page, Integer limit) {
        SqlPara sqlPara =  instance.getSqlPara("oa-workflowtask.completedlist", kv);
        return   instance.paginate(page,limit,sqlPara);
    }

    public Result sendTask(String toUser, String query, String table, String data, String params1) {

        Query querys = JSON.parseObject(query, Query.class);

if (table==null){
    Result reslut=new    Result ();
    reslut.setIssuccess(false);
    reslut.setMessages("无法获取table");
    reslut.setDebugmessages("无法获取table");
    return reslut;
}
        if (querys == null) { return null; }

        String issign = querys.getIssign();
        String comment = querys.getComment();
        String flowid = querys.getFlowid();
        String instanceid = querys.getInstanceid();

        String taskid = querys.getTaskid();
        String stepid = querys.getStepid();
        String groupid = querys.getGroupid();
        Params opationJSON =null;   String opation="";
        if (params1!=null){
            opationJSON= JSON.parseObject(params1, Params.class);

            opation=opationJSON.getType();
        }
        Workflow wfl=   CacheKit.get("flowcache",flowid.toLowerCase());
        if (wfl==null){
              wfl= WorkflowService.Get(flowid.toLowerCase());
            CacheKit.put("flowcache",flowid.toLowerCase(),wfl);
        }


        String error = "";

        RunModel wfInstalled= JosnUtils.stringToBean(wfl.getRunJSON(),RunModel.class);
        if (wfInstalled == null)
        {
            return null;

        }

        //流程标题
        String titleField = querys.getForm_TitleField();
        String title = querys.getTitleField();

        Execute execute = new Execute();
        execute.setComment( comment==null ? "" : comment.toString().trim());
        switch (opation)
        {
            case "submit":
                execute.setExecuteType(EnumType.ExecuteType.Submit);
                break;
            case "save":
                execute.setExecuteType(EnumType.ExecuteType.Save);

                break;
            case "back":  execute.setExecuteType(EnumType.ExecuteType.Back);

                break;
            case "completed":execute.setExecuteType(EnumType.ExecuteType.Completed);

                break;
            case "redirect":execute.setExecuteType(EnumType.ExecuteType.Redirect);

                break;
        }

        execute.setFlowid(flowid);
        execute.setGroupid(groupid);
        execute.setInstanceid(instanceid);
        execute.setIssign("1".equals( issign));
        execute.setNote("");
        execute.setSender(usersService.Get(toUser)) ;
        String st= "";
        if (stepid==null){
            st= wfInstalled.getSteps().get(0).getId();
        }else {
            st= stepid;
        }
        execute.setStepid(st.toLowerCase());
        execute.setTaskid(taskid);
        execute.setTitle(title);
        execute.setUrls(table);
       List<Steps>  stepsjson = opationJSON.getSteps();
        if (stepsjson.size()>0)
        {
            for (Steps step : stepsjson)
            {

                String id = step.getId().toLowerCase();
                String member = step.getMember().toLowerCase();

                {
                    switch (execute.getExecuteType())
                    {
                        case Submit:
                            java.util.Map<String, List<Users>> steps=new HashMap();
                            steps.put(id, organizeService.GetAllUsers(member));
                            execute.setSteps(steps);
                           break;
                        case Back:
                            java.util.Map<String, List<Users>> steps1=new HashMap();
                            steps1.put(id, new ArrayList<Users>());
                            execute.setSteps(steps1);
                            break;
                        case Save:
                            break;
                        case Completed:
                            break;
                        case Redirect:
                            break;
                    }
                }
                if (execute.getExecuteType() == EnumType.ExecuteType.Redirect)
                {

                   // execute.setSteps(StringUtil.GuidEmpty(),).Add(Guid.Empty, OrganizeService.Instance.GetAllUsers(member));
                }
            }
        }

        WorkFlowCustomEventParams eventParams = new WorkFlowCustomEventParams();
        eventParams.setFlowid(execute.getFlowid());
        eventParams.setGroupid(execute.getGroupid());
        eventParams.setStepid(execute.getStepid());

        eventParams.setTaskid(execute.getTaskid());
        eventParams.setInstanceid(execute.getInstanceid());

        //保存业务数据 "1" != Request.QueryString["isSystemDetermine"]:当前步骤流转类型如果是系统判断，则先保存数据，在这里就不需要保存数据了。
        if (execute.getExecuteType() == EnumType.ExecuteType.Save ||
                execute.getExecuteType() == EnumType.ExecuteType.Completed ||   ! "1".equals( querys.getIsSystemDetermine())
        )
        {
            try {
                if (data!=null){
                Record os=new Record();

                Map h5 = JSON.parseObject(data, Map.class);
                os.setColumns(h5);

                instanceid=  FromData.save(os,table);
                }
            }catch (Exception ex){
                Result reslut=new    Result ();
                reslut.setIssuccess(false);
                reslut.setMessages("保持失败"+ex.getMessage());
                reslut.setDebugmessages("保持失败"+ex.getMessage());
                return reslut;

            }
            if (execute.getInstanceid()==null)
            {
                execute.setInstanceid(instanceid);
            }
        }

        //  Response.Write("执行参数：" + params1 + "<br/>");

      List<Steps>   steps = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(execute.getStepid().toLowerCase())).collect(Collectors.toList());
        for (Steps step : steps)
        {
            //步骤提交前事件
            if (!step.getEvent().getSubmitBefore().equals(null) &&
                    (execute.getExecuteType() == EnumType.ExecuteType.Submit
                            || execute.getExecuteType() == EnumType.ExecuteType.Completed))
            {
                //object obj = WorkFlowTaskService.Instance.ExecuteFlowCustomEvent(step.Event.SubmitBefore.Trim(), eventParams);
         }
            //步骤退回前事件
            if (!(step.getEvent().getSubmitBefore()==null) && execute.getExecuteType() == EnumType.ExecuteType.Back)
            {
                //Record obj = WorkFlowTaskService.ExecuteFlowCustomEvent(step.getEvent().getBackBefore().toString().trim(), eventParams);
                // Response.Write(String.Format("执行步骤退回前事件：({0}) 返回值：{1}<br/>", step.Event.BackBefore.Trim(), obj.ToString()));
            }
        }

        ////处理委托
        //foreach (var executeStep in execute.Steps)
        //{
        //    for (int i = 0; i < executeStep.Value.Count; i++)
        //    {
        //        Guid newUserID = bworkFlowDelegation.GetFlowDelegationByUserID(execute.FlowID, executeStep.Value[i].ID);
        //        if (newUserID != Guid.Empty && newUserID != executeStep.Value[i].ID)
        //        {
        //            executeStep.Value[i] = busers.Get(newUserID);
        //        }
        //    }
        //}

        Result reslut =  Execute(execute,wfInstalled);
        //  Response.Write(String.Format("处理流程步骤结果：{0}<br/>", reslut.IsSuccess ? "成功" : "失败"));
        // Response.Write(String.Format("调试信息：{0}", reslut.DebugMessages));
        String msg = reslut.getMessages();
        String logContent = String.format("处理参数：{0}<br/>处理结果：{1}<br/>调试信息：{2}<br/>返回信息：{3}",
                params1,
                reslut.getIssuccess() ? "成功" : "失败",
                reslut.getDebugmessages(),
                reslut.getMessages()
        );

        //  RoadFlow.Platform.Log.Add(String.Format("处理了流程({0})", wfInstalled.Name), logContent, RoadFlow.Platform.Log.Types.流程相关);

        for (Steps step : steps)
        {
            //步骤提交后事件
            if (!(step.getEvent().getSubmitAfter()==null) &&
                    (execute.getExecuteType() == EnumType.ExecuteType.Submit
                            || execute.getExecuteType() == EnumType.ExecuteType.Completed))
            {
               // object obj = WorkFlowTaskService.Instance.ExecuteFlowCustomEvent(step.Event.SubmitAfter.Trim(), eventParams);
                //Response.Write(String.Format("执行步骤提交后事件：({0}) 返回值：{1}<br/>", step.Event.SubmitAfter.Trim(), obj.ToString()));
            }
            //步骤退回后事件
//            if (!step.Event.BackAfter.IsNullOrEmpty() && execute.ExecuteType == Back)
//            {
//                object obj = WorkFlowTaskService.Instance.ExecuteFlowCustomEvent(step.Event.BackAfter.Trim(), eventParams);
//                //  Response.Write(String.Format("执行步骤退回后事件：({0}) 返回值：{1}<br/>", step.Event.BackAfter.Trim(), obj.ToString()));
//            }
        }

        //归档
        if (execute.getExecuteType() == EnumType.ExecuteType.Submit
                || execute.getExecuteType() == EnumType.ExecuteType.Completed)
        {
            List<Steps> currentsteps = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(execute.getStepid())).collect(Collectors.toList());
            if (currentsteps.size() > 0 && currentsteps.get(0).getArchives().equals(1))
            {
                String flowName, stepName;
                String formHtml =querys.getForm_body_div_textarea();
                String commentHtml = querys.getForm_commentlist_div_textarea();
                //stepName = bworkFlow.GetStepName(execute.StepID, execute.FlowID, out flowName, true);
                //String archivesContents = new RoadFlow.Platform.WorkFlowForm().GetArchivesString(formHtml, commentHtml);
                WorkFlowArchives wfr = new WorkFlowArchives();
                wfr.setComments(commentHtml);
                //wfr.Contents = archivesContents;
                //wfr.FlowID = execute.FlowID;
                //wfr.FlowName = flowName;
                wfr.setGroupID(execute.getGroupid());
                wfr.setID(StringUtil.getPrimaryKey());
                wfr.setInstanceID(execute.getInstanceid());
                wfr.setStepID(execute.getStepid());
                //  wfr.StepName = stepName;
                //  wfr.Title = title.IsNullOrEmpty() ? flowName + "-" + stepName : title;
                wfr.setTaskID(execute.getTaskid());
                wfr.setWriteTime(new Date());
                // WorkFlowArchivesDal..Add(wfr);
            }
        }

        //  Response.Write("<script type=\"text/javascript\">alert('" + msg + "');top.mainDialog.close();</script>");

        //<editor-fold desc="dadfaf">
        if (reslut.getIssuccess())
        {
            //判断是打开任务还是关闭窗口

        }
        //</editor-fold>
        return reslut;
        
    }



    /// <summary>
    /// 处理流程
    /// </summary>
    /// <param name="executeModel">处理实体</param>
    /// <returns></returns>
    public static Result Execute(Execute executeModel, RunModel wfInstalled)
    {
        Result result = new Result();
        List<WorkFlowTask> nextTasks = new ArrayList<WorkFlowTask>();
        if (executeModel.getFlowid().equals(StringUtil.GuidEmpty()))
        {
            result.setDebugmessages("流程ID错误");
            result.setIssuccess(false);
            result.setMessages("执行参数错误");
            return result;
        }


        if (wfInstalled == null)
        {
            result.setDebugmessages( "未找到流程运行时实体");
            result.setIssuccess(false);
            result.setMessages("流程运行时为空");
            return result;
        }

        //lock (executeModel.getGroupid())
        {
            switch (executeModel.getExecuteType())
            {
                case Back:
                    result=    executeBack(executeModel,wfInstalled);
                    break;

                case Save:
                    result=  executeSave(executeModel,wfInstalled);
                    break;
                case Submit:
                case Completed:
                    result=  executeSubmit(executeModel,wfInstalled);
                    break;
                case Redirect:
                    result=  executeRedirect(executeModel,wfInstalled);
                    break;
                default:
                    result.setDebugmessages("流程处理类型为空") ;
                    result.setIssuccess(false) ;
                    result.setMessages("流程处理类型为空");
                    return result;
            }

            result.setNexttasks(nextTasks) ;
            return result;
        }
    }

    private static Result executeRedirect(Execute executeModel, RunModel wfInstalled) {
        Result result=new Result();
        WorkFlowTask currentTask = instance.findById(executeModel.getTaskid());
        if (currentTask == null)
        {
            result.setDebugmessages("未能创建或找到当前任务");
            result.setIssuccess(false);
            result.setMessages("未能创建或找到当前任务");
            return result;
        }
        else if (currentTask.getStatus().equals(2)||currentTask.getStatus().equals(2)||currentTask.getStatus().equals(3)||currentTask.getStatus().equals(4)||currentTask.getStatus().equals(5))
        {
            result.setDebugmessages("当前任务已处理");
            result.setIssuccess(false);
            result.setMessages("当前任务已处理");
            return result;
        }
        else if (currentTask.getStatus().equals(-1))
        {
            result.setDebugmessages("当前任务正在等待他人处理") ;
            result.setIssuccess(false);
            result.setMessages("当前任务正在等待他人处理");
            return result;
        }
        if (executeModel.getSteps().size() == 0)
        {
            result.setDebugmessages("未设置转交人员");
            result.setIssuccess(false);
            result.setMessages("未设置转交人员");
            return result;
        }
        String receiveName = currentTask.getReceiveName();

            for (Users user : executeModel.getSteps().get(0))
            {
                currentTask.setID(StringUtil.getPrimaryKey());
                currentTask.setReceiveID(user.getID());
                currentTask.setReceiveName(user.getName());

                currentTask.setStatus(0);
                currentTask.setIsSign(0);
                currentTask.setType(3);
                currentTask.setNote(String.format("该任务由{0}转交", receiveName));
                currentTask.setUrls(executeModel.getUrls());
                if (!HasNoCompletedTasks(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID(), user.getID()))
                {
                    currentTask.save();
                }
                currentTask.save();
                //  nextTasks.Insert(currentTask);
            }
            Completed(executeModel.getTaskid(), executeModel.getComment(), executeModel.getIssign(), 2, "已转交他人处理");


        List<String> nextStepName = new ArrayList<String>();
        for (Users user : executeModel.getSteps().get(0))
        {
            nextStepName.add(user.getName());
        }
        String userName =  nextStepName.stream().distinct().toArray().toString();
        result.setDebugmessages( "已转交给:"+ userName);
        result.setIssuccess(true);
        result.setMessages("已转交给:"+userName);
        return result;
    }

    private static Result executeSubmit(Execute executeModel, RunModel wfInstalled) {
        List<WorkFlowTask> nextTasks = new ArrayList<WorkFlowTask>();
        Result   result=new  Result();
            //如果是第一步提交并且没有实例则先创建实例
            WorkFlowTask currentTask = null;
        Boolean isFirst = executeModel.getStepid().toLowerCase() == wfInstalled.getSteps().get(0).getId() && (executeModel.getTaskid()==null )&& (executeModel.getGroupid()==null) ;
        if (isFirst)
            {
                currentTask = createFirstTask(executeModel,wfInstalled,false);
            }
            else
            {
                currentTask = instance.findById(executeModel.getTaskid());
            }
        if (currentTask == null)
        {
            result.setDebugmessages("未能创建或找到当前任务");
            result.setIssuccess(false) ;
            result.setMessages("未能创建或找到当前任务");
            return result;
        }
        else if (currentTask.getStatus().equals(2)||currentTask.getStatus().equals(3)||currentTask.getStatus().equals(4))
        {
            result.setDebugmessages( "当前任务已处理");
            result.setIssuccess(false);
            result.setMessages("当前任务已处理");
            return result;
        }

       List <Steps> currentSteps = wfInstalled.getSteps().stream().filter(Steps-> Steps.getId().equals(executeModel.getStepid())).collect(Collectors.toList());
        Steps currentStep = currentSteps.size() > 0 ? currentSteps.get(0) : null;
            if (currentStep == null)
            {
                result.setDebugmessages( "未找到当前步骤");
                result.setIssuccess(false);
                result.setMessages("未找到当前步骤");
                return result;

            }

//            //如果当前步骤是子流程步骤，并且策略是 子流程完成后才能提交 则要判断子流程是否已完成
//            if (currentStep.getType() == "subflow"
//                    && currentStep..IsGuid()
//                    && currentStep.behavior.subflowstrategy == "0"
//                    && currentTask.SubFlowGroupID.HasValue
//                    && !currentTask.SubFlowGroupID.Value.IsEmptyGuid()
//                    && !GetInstanceIsCompleted(currentStep.subflow.ToGuid(), currentTask.SubFlowGroupID.Value))
//            {
//                result.DebugMessages = "当前步骤的子流程实例未完成,子流程：" + currentStep.subflow + ",实例组：" + currentTask.SubFlowGroupID.ToString();
//                result.IsSuccess = false;
//                result.Messages = "当前步骤的子流程未完成,不能提交!";
//                return;
//            }

            //如果是完成任务或者没有后续处理步骤，则完成任务
            if (executeModel.getExecuteType() == EnumType.ExecuteType.Completed
                    || executeModel.getSteps() == null || executeModel.getSteps().size() == 0)
            {
                executeComplete(executeModel,wfInstalled,true);
                result.setDebugmessages( "完成");
                result.setIssuccess(true);
                result.setMessages("完成");
                return result;

            }

        //region 处理策略判断
        int status = 0;


            if (!currentTask.getStepID().equals( wfInstalled.getSteps().get(0).getId()))//第一步不判断策略
            {
                switch (Integer.parseInt(currentStep.getBehavior().getHanlderModel()))
                {
                    case 0://所有人必须处理
                        WorkFlowTask finalCurrentTask2 = currentTask;
                        List<WorkFlowTask> taskList = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(WorkFlowTask -> WorkFlowTask.getSort().equals(finalCurrentTask2.getSort())  && !(WorkFlowTask.getType().equals(5))).collect(Collectors.toList());
                        if (taskList.size() > 1)
                        {
                            List<WorkFlowTask> noCompleted = taskList.stream().filter(WorkFlowTask -> !(WorkFlowTask.getStatus().equals(2))).collect(Collectors.toList());
                            if (noCompleted.size() - 1 > 0)
                            {
                                status = -1;
                            }
                        }
                        Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
                        break;
                    case 1://一人同意即可
                        WorkFlowTask finalCurrentTask1 = currentTask;
                        List<WorkFlowTask> taskList1 = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(WorkFlowTask -> WorkFlowTask.getSort().equals(finalCurrentTask1.getSort())  && !(WorkFlowTask.getType().equals(5))).collect(Collectors.toList());
                        for (WorkFlowTask task : taskList1) {
                            if (!task.getID().equals(currentTask.getID())){
                                if (task.getStatus().equals(0) || task.getStatus().equals(1)) {
                                    Completed(task.getID(), "", false, 4, null);
                                }
                        }
                        else
                        {
                            Completed(task.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
                        }
                    }
                    break;
                    case 2://依据人数比例
                        WorkFlowTask finalCurrentTask = currentTask;
                        List<WorkFlowTask> taskList2 = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(p -> p.getSort().equals(finalCurrentTask.getSort())  && !(p.getType().equals(5))).collect(Collectors.toList());
                        if (taskList2.size() > 1)
                        {
//                            Double percentage =Double.parseDouble(currentStep.getBehavior().getPercentage())  <= 0 ? 100 :Double.parseDouble( currentStep.getBehavior().getPercentage());//比例
//                            if ((((decimal)(taskList2.Where(p => p.Status == 2).Count() + 1) / (decimal)taskList2.Count) * 100).Round() < percentage)
//                            {
//                                status = -1;
//                            }
//                                else
//                            {
//                                foreach (var task in taskList2)
//                                {
//                                    if (task.ID != currentTask.ID && task.Status.In(0, 1))
//                                    {
//                                        Completed(task.ID, "", false, 4);
//                                    }
//                                }
//                            }
                        }
                        Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
                        break;
                    case 3://独立处理
                        Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
                        break;
                }
            }
            else
            {
                Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
            }
        //endregion

            //如果条件不满足则创建一个状态为-1的后续任务，等条件满足后才修改状态，待办人员才能看到任务。
            if (status == -1)
            {
                List <WorkFlowTask>tempTasks = createTempTasks(executeModel, currentTask,  wfInstalled);
                List<String> nextStepName = new ArrayList<String>();
                for (WorkFlowTask nstep : tempTasks)
                {
                    nextStepName.add(nstep.getStepName());
                }
                nextTasks.addAll(tempTasks);
                String stepName =String.join(",",  nextStepName.stream().distinct().collect(Collectors.toList())) ;
                result.setDebugmessages( String.format("已发送到:{0},其他人未处理,不创建后续任务", stepName));
                result.setIssuccess(true);
                result.setMessages(String.format("已发送到:{0},等待他人处理!", stepName));
                result.setNexttasks(nextTasks);

                return result;
            }
             java.util.Map<String, List<Users>> step= executeModel.getSteps();
           Set<Map.Entry<String, List<Users>>> entry =  step.entrySet();
        for (Map.Entry<String, List<Users>> m : entry) {
                for (Users user : step.get(m.getKey())) {

                    List<Steps> nextSteps = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(m.getKey())).collect(Collectors.toList());
                    if (nextSteps.size() == 0) {
                        continue;
                    }
                    Steps nextStep = nextSteps.get(0);
                    Integer s = Integer.parseInt(nextStep.getBehavior().getCountersignature());
                    Boolean isPassing = 0 == s;


                    if (0 != s) {
                        List<Steps> prevSteps = GetPrevSteps(executeModel.getFlowid(), nextStep.getId(), wfInstalled);
                        switch (s) {
                            case 1://所有步骤同意
                                isPassing = true;
                                for (Steps prevStep : prevSteps) {
                                    if (!IsPassing(prevStep, executeModel.getFlowid(), executeModel.getGroupid(), currentTask.getPrevID(), currentTask.getSort())) {
                                        isPassing = false;
                                        break;
                                    }
                                }
                                break;
                            case 2://一个步骤同意即可
                                isPassing = false;
                                for (Steps prevStep : prevSteps) {
                                    if (IsPassing(prevStep, executeModel.getFlowid(), executeModel.getGroupid(), currentTask.getPrevID(), currentTask.getSort())) {
                                        isPassing = true;
                                        break;
                                    }
                                }
                                break;
                            case 3://依据比例
                                int passCount = 0;
                                for (Steps prevStep : prevSteps) {
                                    if (IsPassing(prevStep, executeModel.getFlowid(), executeModel.getGroupid(), currentTask.getPrevID(), currentTask.getSort())) {
                                        passCount++;
                                    }
                                }
                                isPassing = (((double) passCount / (double) prevSteps.size()) * 100) >= (s <= 0 ? 100 : Double.parseDouble(nextStep.getBehavior().getCountersignature()));
                                break;
                        }
                        if (isPassing) {
                            List<WorkFlowTask> tjTasks = GetTaskList(currentTask.getID(), false);
                            for (WorkFlowTask tjTask : tjTasks) {
                                if (tjTask.getID() == currentTask.getID() || tjTask.getStatus().equals(2) || tjTask.getStatus().equals(3) || tjTask.getStatus().equals(4) || tjTask.getStatus().equals(5)) {
                                    continue;
                                }
                                Completed(tjTask.getID(), "", false, 4, null);
                            }
                        }
                    }
                    if (isPassing)
                    {
                        WorkFlowTask task = new WorkFlowTask();
                        if (!nextStep.getWorkTime().equals(""))
                        {
                            int gs= Integer.parseInt(nextStep.getWorkTime());
                            Date date =new   Date(); //取时间
                            Calendar   calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            calendar.add(calendar.HOUR,gs); //把日期往后增加一天,整数  往后推,负数往前移动
                            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
                            task.setCompletedTime(new Date());
                        }

                        task.setFlowID(executeModel.getFlowid());
                        task.setGroupID(currentTask != null ? currentTask.getGroupID() : executeModel.getGroupid());
                        task.setID(StringUtil.getPrimaryKey());
                        task.setType(0);
                        task.setInstanceID(executeModel.getInstanceid());
                        if (!(executeModel.getNote()==null))
                        {
                            task.setNote(executeModel.getNote());
                        }
                        task.setPrevID(currentTask.getID());
                        task.setPrevStepID(currentTask.getStepID()) ;
                        task.setReceiveID( user.getID());
                        task.setReceiveName(user.getName());
                        task.setReceiveTime(new Date());
                        task.setSenderID(executeModel.getSender().getID());
                        task.setSenderName(executeModel.getSender().getName());
                        task.setSenderTime(task.getReceiveTime());
                        task.setStatus(status);
                        task.setStepID(m.getKey());
                        task.setStepName(nextStep.getName());
                        task.setSort(currentTask.getSort() + 1);
                        task.setTitle(executeModel.getTitle()==null ? currentTask.getTitle() : executeModel.getTitle());
                        task.setUrls(executeModel.getUrls());
//                            #region 如果当前步骤是子流程步骤，则要发起子流程实例
//                        if (nextStep.type == "subflow" && nextStep.subflow.IsGuid())
//                        {
//                            Execute subflowExecuteModel = new Execute();
//                            if (!nextStep.events.submitBefore.IsNullOrEmpty())
//                            {
//                                object obj = ExecuteFlowCustomEvent(nextStep.events.submitBefore.Trim(),
//                                        new WorkFlowCustomEventParams()
//                                        {
//                                            FlowID = executeModel.FlowID,
//                                            GroupID = currentTask.GroupID,
//                                            InstanceID = currentTask.InstanceID,
//                                            StepID = executeModel.StepID,
//                                            TaskID = currentTask.ID
//                                        });
//                                if (obj is Execute)
//                                {
//                                    subflowExecuteModel = obj as Execute;
//                                }
//                            }
//                            subflowExecuteModel.ExecuteType = EnumType.ExecuteType.Save;
//                            subflowExecuteModel.FlowID = nextStep.subflow.ToGuid();
//                            subflowExecuteModel.Sender = user;
//                            if (subflowExecuteModel.Title.IsNullOrEmpty())
//                            {
//                                subflowExecuteModel.Title = "未命名的任务";
//                            }
//                            if (subflowExecuteModel.InstanceID.IsEmptyGuid())
//                            {
//                                subflowExecuteModel.InstanceID = Guid.Empty;
//                            }
//                            var subflowTask = createFirstTask(subflowExecuteModel, true);
//                            task.SubFlowGroupID = subflowTask.GroupID;
//                        }
//                            #endregion

                        if (!HasNoCompletedTasks(executeModel.getFlowid(), m.getKey(), currentTask.getGroupID(), user.getID()))
                        {
                            task.save();
                           // instance.save(task);
                        }else {
                            task.update();
                        }
                       // nextTasks.add(task);
                      //  task.save();
                       // WorkFlowTaskDal.Instance.Insert(task);
                    }

/*
                    if (isPassing)
                    {
                        WorkFlowTask task = new WorkFlowTask();
                        if (!nextStep.getWorkTime().equals(""))
                        {
                            int gs= Integer.parseInt(nextStep.getWorkTime());
                            Date date =new   Date(); //取时间
                            Calendar   calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            calendar.add(calendar.HOUR,gs); //把日期往后增加一天,整数  往后推,负数往前移动
                            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
                            task.setCompletedTime(new Date());
                        }


//                        task.setGroupID(currentTask != null ? currentTask.getGroupID() : executeModel.getGroupid());
//                        task.setID(StringUtil.getPrimaryKey());
//                        task.setType(0);
//                        task.setInstanceID(executeModel.getInstanceid());
//                        if (!executeModel.getNote().equals(null))
//                        {
//                            task.setNote(executeModel.getNote());
//                        }
//                        task.setPrevID(StringUtil.GuidEmpty());
//                        task.setPrevStepID(StringUtil.GuidEmpty());
//                       // task.ReceiveID = user.ID;
//                       // task.ReceiveName = user.Name;
//                        task.setReceiveID(user.getID());
//                        task.setReceiveName(user.getName());
//                        task.setReceiveTime(new Date());
//                        task.setSenderID(executeModel.getSender().getID());
//                        task.setSenderName(executeModel.getSender().getName());
//                        task.setSenderTime(task.getReceiveTime());
//                        task.setStatus(0);
//                        task.setStepID(wfInstalled.getSteps().get(0).getId());
//                        task.setStepName(nextSteps.get(0).getName());
//                        task.setSort(currentTask.getSort()+1);
//                        task.setTitle(executeModel.getTitle()==null?wfInstalled.getName():executeModel.getTitle()) ;
//                        task.setUrls(executeModel.getUrls());
                       // task.save();


//                        if (nextStep.type == "subflow" && nextStep.subflow.IsGuid())
//                        {
//                            Execute subflowExecuteModel = new Execute();
//                            if (!nextStep.events.submitBefore.IsNullOrEmpty())
//                            {
//                                object obj = ExecuteFlowCustomEvent(nextStep.events.submitBefore.Trim(),
//                                        new WorkFlowCustomEventParams()
//                                        {
//                                            FlowID = executeModel.FlowID,
//                                            GroupID = currentTask.GroupID,
//                                            InstanceID = currentTask.InstanceID,
//                                            StepID = executeModel.StepID,
//                                            TaskID = currentTask.ID
//                                        });
//                                if (obj is Execute)
//                                {
//                                    subflowExecuteModel = obj as Execute;
//                                }
//                            }
//                            subflowExecuteModel.ExecuteType = EnumType.ExecuteType.Save;
//                            subflowExecuteModel.FlowID = nextStep.subflow.ToGuid();
//                            subflowExecuteModel.Sender = user;
//                            if (subflowExecuteModel.Title.IsNullOrEmpty())
//                            {
//                                subflowExecuteModel.Title = "未命名的任务";
//                            }
//                            if (subflowExecuteModel.InstanceID.IsEmptyGuid())
//                            {
//                                subflowExecuteModel.InstanceID = Guid.Empty;
//                            }
//                            var subflowTask = createFirstTask(subflowExecuteModel, true);
//                            task.SubFlowGroupID = subflowTask.GroupID;
//                        }
//
                        task.save();
                        if (!HasNoCompletedTasks(executeModel.getFlowid(), m.getKey(), currentTask.getGroupID(), user.getID()))
                        {
                            task.save();
                        }
                      nextTasks.add(task);
//                        WorkFlowTaskDal.Instance.Insert(task);
                    }


 */
                }
            }

            if (nextTasks.size() > 0)
            {

                //激活临时任务
                UpdateTempTasks(nextTasks.get(0).getFlowID(), nextTasks.get(0).getStepID(), nextTasks.get(0).getGroupID(),
                        nextTasks.get(0).getCompletedTime(), nextTasks.get(0).getReceiveTime());
/*
                for (Steps step : executeModel.getSteps())
                {
                    List<Steps> nextSteps = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(step)).collect(Collectors.toList());
                    if (nextSteps.size() > 0)
                    {
                        Steps nextStep = nextSteps.get(0);
                        if (!nextStep.getBehavior().getCopyFor().equals(null))
                        {
                            var users = OrganizeService.Instance.GetAllUsers(nextStep.getBehavior().getCopyFor());
                            foreach (var user in users)
                            {
                                WorkFlowTask task = new WorkFlowTask();
                                if (nextStep.workTime != "")
                                {
                                    double gs; double.TryParse(nextStep.workTime, out gs);
                                    task.CompletedTime = DateTime.Now.AddHours(gs);
                                }
                                task.setFlowID(executeModel.getFlowid());
                                task.setGroupID(currentTask != null ? currentTask.getGroupID() : executeModel.getGroupid());
                                task.setID(StringUtil.getPrimaryKey());
                                task.setType(0);
                                task.setInstanceID(executeModel.getInstanceid());
                                if (!executeModel.getNote().equals(null))
                                {
                                    task.setNote(executeModel.getNote());
                                }
                                task.setPrevID(StringUtil.GuidEmpty());
                                task.setPrevStepID(StringUtil.GuidEmpty());
                                task.setReceiveID(user.getID());
                                task.setReceiveName( user.Name);
                                task.setReceiveTime(new Date());
                                task.setSenderID(executeModel.getSender().getID());
                                task.setSenderName(executeModel.getSender().getName());
                                task.setSenderTime(task.getReceiveTime());
                                task.setStatus(0);
                                task.setStepID(wfInstalled.getSteps().get(0).getId());
                                task.setStepName(nextSteps.get(0).getName());
                                task.setSort(currentTask.getSort() + 1);
                                task.setTitle(executeModel.getTitle()==null?wfInstalled.getName():executeModel.getTitle()) ;
                                task.setUrls(executeModel.getUrls());

                                if (!HasNoCompletedTasks(executeModel.getFlowid(), step, currentTask.getGroupID(), user.getID()))
                                {
                                    task.save();

                                }
                            }
                        }
                    }
                }

*/
                List<String> nextStepName =  new ArrayList<String>();
                for (WorkFlowTask nstep : nextTasks)
                {
                    nextStepName.add(nstep.getStepName());
                }
                String stepName = String.join(",", nextStepName.stream().distinct().collect(Collectors.toList()));

                result.setDebugmessages( String.format("已发送到:{0},", stepName));
                result.setIssuccess(true);
                result.setMessages(String.format("已发送到:{0},", stepName));
                result.setNexttasks(nextTasks);
            }
            else
            {
                List<WorkFlowTask> tempTasks = createTempTasks(executeModel, currentTask,wfInstalled);
                List<String> nextStepName = new ArrayList<String>();
                for (WorkFlowTask nstep : tempTasks)
                {
                    nextStepName.add(nstep.getStepName());
                }
               // nextTasks.(tempTasks);
                String stepName = String.join(",", nextStepName.stream().distinct().collect(Collectors.toList()));

                result.setDebugmessages( String.format("已发到:"+stepName+",等待其它步骤处理", stepName));
                result.setIssuccess(true);
                result.setMessages(String.format("已发送:"+stepName+",等待其它步骤处理", stepName));
                result.setNexttasks(nextTasks);
             return result;
            }
            return null;
    }




    private static Result  executeSave(Execute executeModel, RunModel wfInstalled) {
        List<WorkFlowTask> nextTasks = new ArrayList<WorkFlowTask>();
        Result result=new Result();
        //如果是第一步提交并且没有实例则先创建实例
        WorkFlowTask currentTask = null;
        Boolean isFirst = executeModel.getStepid().toLowerCase() == wfInstalled.getSteps().get(0).getId() && (executeModel.getTaskid()==null && executeModel.getGroupid()==null) ;
        if (isFirst)
        {
            currentTask = createFirstTask(executeModel,wfInstalled,false);
        }
        else
        {
            currentTask = instance.findById(executeModel.getTaskid());
        }
        if (currentTask == null)
        {
            result.setDebugmessages("未能创建或找到当前任务");
            result.setIssuccess(false) ;
            result.setMessages("未能创建或找到当前任务");
            return result;
        }
        else if (currentTask.getStatus().equals(2)&&currentTask.getStatus().equals(3)&&currentTask.getStatus().equals(4))
        {
            result.setDebugmessages( "当前任务已处理");
            result.setIssuccess(false);
            result.setMessages("当前任务已处理");
            return result;
        }
        else
        {
            currentTask.setInstanceID(executeModel.getInstanceid());
            nextTasks.add(currentTask);
            if (isFirst)
            {
                currentTask.setTitle(executeModel.getTitle()==null ? "未命名任务" : executeModel.getTitle());
                currentTask.update();

            }
            else
            {
                if (!(executeModel.getTitle()==null))
                {
                    currentTask.setTitle(executeModel.getTitle()) ;
                    currentTask.update();
                  //  WorkFlowTaskDal.Instance.Update(currentTask);
                }
            }
        }

        result.setDebugmessages("保存成功");
        result.setIssuccess(true);
        result.setMessages("保存成功");
        return result;
    }

    /// <summary>
    /// 创建第一个任务
    /// </summary>
    /// <param name="executeModel"></param>
    /// <param name="isSubFlow">是否是创建子流程任务</param>
    /// <returns></returns>
    private static WorkFlowTask createFirstTask(Execute executeModel, RunModel wfInstalled, Boolean isSubFlow)
    {


        RunModel finalWfInstalled = wfInstalled;
       List <Steps> nextSteps = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(finalWfInstalled.getSteps().get(0).getId())).collect(Collectors.toList());
        if (nextSteps.size() == 0)
        {
            return null;
        }
        WorkFlowTask task = new WorkFlowTask();

        //if (nextSteps.First().workTime > 0)
        //{

        if (!nextSteps.get(0).getWorkTime().equals(""))
        {
            int gs= Integer.parseInt(nextSteps.get(0).getWorkTime());
            Date date =new   Date(); //取时间
            Calendar   calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.HOUR,gs); //把日期往后增加一天,整数  往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            task.setCompletedTime(new Date());
        }
        task.setFlowID(executeModel.getFlowid());
        task.setGroupID(StringUtil.getPrimaryKey());
        task.setID(StringUtil.getPrimaryKey());;
        task.setType(0);
        task.setInstanceID(executeModel.getInstanceid());
        if (!(executeModel.getNote()!=null))
        {
            task.setNote(executeModel.getNote());
        }
        task.setPrevID(StringUtil.GuidEmpty());
        task.setPrevStepID(StringUtil.GuidEmpty());
        task.setReceiveID(executeModel.getSender().getID());
        task.setReceiveName(executeModel.getSender().getName());
        task.setReceiveTime(new Date());
        task.setSenderID(executeModel.getSender().getID());
        task.setSenderName(executeModel.getSender().getName());
        task.setSenderTime(task.getReceiveTime());
        task.setStatus(0);
        task.setStepID(wfInstalled.getSteps().get(0).getId());
        task.setStepName(nextSteps.get(0).getName());
        task.setSort(1);
        task.setTitle(executeModel.getTitle()==null?wfInstalled.getName():executeModel.getTitle()) ;
        task.setUrls(executeModel.getUrls());
        task.save();
   
        if (isSubFlow)
        {
            wfInstalled = null;
        }
        return task;
    }

    private static Result executeBack(Execute executeModel, RunModel wfInstalled) {
        Result result= new Result();
        WorkFlowTask currentTask = Get(executeModel.getTaskid());
        if (currentTask == null)
        {
            result.setDebugmessages("未能找到当前任务");
            result.setIssuccess(false);
            result.setMessages( "未能找到当前任务");
            return result;
        }
        else if (currentTask.getStatus().equals(2)||currentTask.getStatus().equals(3)||currentTask.getStatus().equals(4)||currentTask.getStatus().equals(5))
        {
            result.setDebugmessages( "当前任务已处理");
            result.setIssuccess(false);//IsSuccess = false;
            result.setMessages( "当前任务已处理");
            return result;
        }

        //var currentSteps = wfInstalled.getSteps().stream().filter(p -> p.getId() == currentTask.StepID.ToString());\
        List<Steps> currentSteps = wfInstalled.getSteps().stream().filter(p -> p.getId().equals( currentTask.getStepID())).collect(Collectors.toList());;
        Steps currentStep = currentSteps.size() > 0 ? currentSteps.get(0) : null;

        if (currentStep == null)
        {
            result.setDebugmessages("未能找到当前步骤");
            result.setIssuccess(false);
            result.setMessages( "未能找到当前步骤");
            return result;
        }
        if (currentTask.getStepID().equals( wfInstalled.getSteps().get(0).getId()))
        {
            result.setDebugmessages("当前任务是流程第一步,不能退回");
            result.setIssuccess(false);
            result.setMessages( "当前任务是流程第一步,不能退回");

            return result;
        }
        if (executeModel.getSteps().size()== 0)
        {
            result.setDebugmessages("没有选择要退回的步骤");
            result.setIssuccess(false);
            result.setMessages( "没有选择要退回的步骤");

            return result;

        }


            List<WorkFlowTask> backTasks = new ArrayList<WorkFlowTask>();
        List<WorkFlowTask> nextTasks = new ArrayList<WorkFlowTask>();
            int status = 0;
            switch (currentStep.getBehavior().getBackModel())
            {
                case "0"://不能退回
                    result.setDebugmessages("当前步骤设置为不能退回");
                    result.setIssuccess(false);
                    result.setMessages( "当前步骤设置为不能退回");

                    return result;


                case "1":
                    switch (currentStep.getBehavior().getHanlderModel())
                    {
                        case "0"://所有人必须同意,如果一人不同意则全部退回
                            List<WorkFlowTask> taskList = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(p -> p.getSort().equals( currentTask.getSort()) &&! p.getType().equals(5)).collect(Collectors.toList());
                            for (WorkFlowTask task : taskList)
                        {
                            if (!task.getID().equals(currentTask.getID()))
                            {
                                if (task.getStatus().equals(0)||task.getStatus().equals(1))
                                {
                                    Completed(task.getID(), "", false, 5,"");
                                    //backTasks.Add(task);
                                }
                            }
                            else
                            {
                                Completed(task.getID(), executeModel.getComment(), executeModel.getIssign(), 3,"");
                            }
                        }
                        break;
                        case "1"://一人同意即可
                            List<WorkFlowTask> taskList1 = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(p -> p.getSort().equals( currentTask.getSort()) &&! p.getType().equals(5)).collect(Collectors.toList());
                            //var taskList1 = GetTaskList(currentTask.FlowID, currentTask.StepID, currentTask.GroupID).FindAll(p => p.Sort == currentTask.Sort && p.Type != 5);
                            if (taskList1.size() > 1)
                            {
                                List<WorkFlowTask> noCompleted = taskList1.stream().filter(p -> p.getStatus().equals(3)).collect(Collectors.toList());
                                if (noCompleted.size() - 1 > 0)
                                {
                                    status = -1;
                                }
                            }
                            Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(), 3,"");
                            break;
                        case "2"://依据人数比例
                            List<WorkFlowTask> taskList2 = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(p -> p.getSort().equals( currentTask.getSort()) &&! p.getType().equals(5)).collect(Collectors.toList());

                            if (taskList2.size() > 1)
                            {
//                                Double percentage =Double.parseDouble( currentStep.getBehavior().getPercentage()) <= 0 ? 100 :Double.parseDouble( currentStep.getBehavior().getPercentage());//比例
//                                if (taskList2.stream().filter(p -> p.getStatus().equals(3)).collect(Collectors.toList()).size() + 1 / taskList2.size() * 100)< 100 - percentage)
//                                {
//                                    status = -1;
//                                }
//                                    else
//                                {
//                                    foreach (var task in taskList2)
//                                    {
//                                        if (task.ID != currentTask.ID && task.Status.In(0, 1))
//                                        {
//                                            Completed(task.ID, "", false, 5);
//                                            //backTasks.Add(task);
//                                        }
//                                    }
//                                }
                            }
                            Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(), 3,"");
                            break;
                        case "3"://独立处理
                            Completed(currentTask.getID(), executeModel.getComment(), executeModel.getIssign(), 3,"");
                            break;
                    }
                   // currentTask.save();
                    //instance.save(currentTask);
                    break;

            }

            if (status == -1)
            {
                result.setDebugmessages("已退回,等待他人处理");
                result.setIssuccess(false);
                result.setMessages( "已退回,等待他人处理");

                return result;


            }

            for (WorkFlowTask backTask : backTasks)
            {
                if (backTask.getStatus().equals(2)||backTask.getStatus().equals(3))//已完成的任务不能退回
                {
                    continue;
                }
                if (backTask.getID().equals( currentTask.getID()))
                {
                    Completed(backTask.getID(), executeModel.getComment(), executeModel.getIssign(), 3,"");
                }
                else
                {
                    Completed(backTask.getID(), "", false, 3, "他人已退回");
                }
            }

            List<WorkFlowTask> tasks = new ArrayList<WorkFlowTask>();
            if (currentStep.getBehavior().getHanlderModel().equals(0)||currentStep.getBehavior().getHanlderModel().equals(1)||currentStep.getBehavior().getHanlderModel().equals(2))//退回时要退回其它步骤发来的同级任务。
            {
                List<WorkFlowTask> tjTasks = GetTaskList(currentTask.getFlowID(), currentTask.getStepID(), currentTask.getGroupID()).stream().filter(p -> p.getSort().equals(currentTask.getSort())).collect(Collectors.toList());
                for (WorkFlowTask tjTask : tjTasks)
                {
                    if (!executeModel.getSteps().containsKey(tjTask.getPrevStepID()))
                    {
                        executeModel.getSteps().put(tjTask.getPrevStepID(), new ArrayList<Users>());
                    }
                }
            }
        java.util.Map<String, List<Users>> step= executeModel.getSteps();
        Set<Map.Entry<String, List<Users>>> entry =  step.entrySet();
//            for (int i=0; step.size()>i;i++)

            for (Map.Entry<String, List<Users>> m : entry) {
               // for (Users user : step.get(m.getKey())) {

                    List<WorkFlowTask> tasks1 = GetTaskList(executeModel.getFlowid(), m.getKey(), executeModel.getGroupid());
                if (tasks1.size() > 0)
                {
                     tasks1 = tasks1.stream().sorted(Comparator.comparingInt(WorkFlowTask::getSort).reversed()).collect(Collectors.toList());
                    //tasks1 = tasks1.stream().filter()..OrderByDescending(p => p.Sort).ToList();
                    int maxSort = tasks1.get(0).getSort();
                    tasks.add(tasks1.stream().filter(p -> p.getSort().equals( maxSort)).collect(Collectors.toList()).get(0));
                }
           // }
            }


            //当前步骤是否是会签步骤
            List<Steps> countersignatureStep = GetNextSteps(executeModel.getFlowid(), executeModel.getStepid(),wfInstalled).stream().filter(p -> p.getBehavior().getCountersignature().equals("0")).collect(Collectors.toList());
            Boolean IsCountersignature = countersignatureStep != null;
        Boolean isBack = true;
            if (IsCountersignature)
            {
                List<Steps> steps = GetPrevSteps(executeModel.getFlowid(), countersignatureStep.get(0).getId(),wfInstalled);
                switch (countersignatureStep.get(0).getBehavior().getCountersignature())
                {
                    case "1"://所有步骤处理，如果一个步骤退回则退回
                        isBack = false;
                        for (Steps steps1 : steps)
                    {
                        if (IsBack(steps1, executeModel.getFlowid(), currentTask.getGroupID(), currentTask.getPrevID(), currentTask.getSort()))
                        {
                            isBack = true;
                            break;
                        }
                    }
                    break;
                    case "2"://一个步骤退回,如果有一个步骤同意，则不退回
                        isBack = true;
                        for (Steps step2 : steps)
                    {
                        if (!IsBack(step2, executeModel.getFlowid(), currentTask.getGroupID(), currentTask.getPrevID(), currentTask.getSort()))
                        {
                            isBack = false;
                            break;
                        }
                    }
                    break;
                    case "3"://依据比例退回
                        int backCount = 0;
                        for (Steps step3 : steps)
                    {
                        if (IsBack(step3, executeModel.getFlowid(), currentTask.getGroupID(), currentTask.getPrevID(), currentTask.getSort()))
                        {
                            backCount++;
                        }
                    }
                    //isBack = (((decimal)backCount / (decimal)steps.Count) * 100).Round() >= (countersignatureStep.Behavior.CountersignaturePercentage <= 0 ? 100 : countersignatureStep.Behavior.CountersignaturePercentage);
                    break;
                }

                if (isBack)
                {
                    List<WorkFlowTask> tjTasks = GetTaskList(currentTask.getID(), false);
                    for (WorkFlowTask tjTask : tjTasks)
                    {
                        if (tjTask.getID().equals(currentTask.getID())  ||  tjTask.getStatus().equals(2)|| tjTask.getStatus().equals(3)|| tjTask.getStatus().equals(4)|| tjTask.getStatus().equals(5))
                        {
                            continue;
                        }
                        Completed(tjTask.getID(), "", false, 5,"");
                    }
                }
            }


            //如果退回步骤是子流程步骤，则要作废子流程实例
//            if (currentStep.type == "subflow" && currentStep.subflow.IsGuid() && currentTask.SubFlowGroupID.HasValue)
//            {
//                DeleteInstance(currentStep.subflow.ToGuid(), currentTask.SubFlowGroupID.Value, true);
//            }

            if (isBack)
            {
                List<WorkFlowTask> backTaskList = tasks.stream().distinct().collect(Collectors.toList());
                if (backTaskList.size() == 0)
                {
                    Completed(currentTask.getID(), "", false, 0, "");
                    result.setDebugmessages("没有接收人,退回失败!");
                    result.setIssuccess(false);
                    result.setMessages( "没有接收人,退回失败!");

                    return result;
                }

                for (WorkFlowTask task : backTaskList)
                {
                    if (task != null)
                    {
                        //删除抄送
                        if (task.getType().equals(5))
                        {
                           task.delete();// .Instance.Delete(task.ID);
                            continue;
                        }

                        WorkFlowTask newTask = task;
                        newTask.setID(StringUtil.getPrimaryKey());// = Guid.NewGuid();
                        newTask.setPrevID ( currentTask.getID());
                        newTask.setNote ("退回任务");
                        newTask.setReceiveTime(new Date()) ;//= DateTime.Now;
                        newTask.setSenderID ( currentTask.getReceiveID());
                        newTask.setSenderName ( currentTask.getReceiveName());
                        newTask.setSenderTime ( new Date());//DateTime.Now;
                        newTask.setSort ( currentTask.getSort() + 1);
                        newTask.setStatus(0);// = 0;
                        newTask.setType (4);//= 4;
                        newTask.setComment ("");// "";
                        newTask.setOpenTime(null);// = null;
                        //newTask.PrevStepID = currentTask.StepID;
                        newTask.setUrls ( currentTask.getUrls());
                        if (!currentStep.getWorkTime().equals(""))
                        {
                            Long gs= Long.parseLong(currentStep.getWorkTime());
                          Long s=  new Date().getTime()+gs;
                            Date date = new Date(s);
                            newTask.setCompletedTime(date);// = DateTime.Now.AddHours(gs);
                            // = DateTime.Now.AddHours((double)currentStep.workTime);
                        }
                        else
                        {
                            newTask.setCompletedTime(null);// = null;
                        }
                        newTask.setCompletedTime1(null);// = null;
                        newTask.save();
                       // WorkFlowTaskDal.Instance.Insert(newTask);
                       //nextTasks.Add(newTask);
                    }
                }

                //删除临时任务
                List<Steps> nextSteps = GetNextSteps(executeModel.getFlowid(), executeModel.getStepid(),wfInstalled);
                for (Steps step5 : nextSteps)
                {
                    DeleteTempTasks(currentTask.getFlowID(), step5.getId(), currentTask.getGroupID(),
                            IsCountersignature ? StringUtil.GuidEmpty() : currentStep.getId()
                    );
                }
            }




        if (nextTasks.size() > 0)
        {
            List<String> nextStepName = new ArrayList<String>();
            for (WorkFlowTask nstep : nextTasks)
            {
                nextStepName.add(nstep.getStepName());
            }
            String msg = String.format("已退回到:{0}",String.join(",",nextStepName));
            result.setDebugmessages(msg);
            result.setIssuccess(true);
            result.setMessages( msg);

        }
        else
        {
            result.setDebugmessages("已退回,等待其它步骤处理");
            result.setIssuccess(false);
            result.setMessages( "已退回,等待其它步骤处理");

        }
        return result;
    }

    private static WorkFlowTask Get(String taskid) {
        return instance.findById(taskid);
    }

    /// <summary>
    /// 删除临时任务
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="stepID"></param>
    /// <param name="groupID"></param>
    /// <param name="prevStepID"></param>
    /// <returns></returns>
    public static boolean DeleteTempTasks(String flowID, String stepID, String groupID, String prevStepID)
    {
        Record del=new Record();
        del.set("FlowID",flowID);
        del.set("StepID",stepID);
        del.set("GroupID",groupID);
        del.set("Status",-1);
      //  String sql = "DELETE WorkFlowTask WHERE FlowID='"+flowID+"' AND StepID='"+flowID+"' AND GroupID='"+groupID+"' AND Status=-1";

        if (!prevStepID.equals(StringUtil.GuidEmpty()))
        {   del.set("PrevStepID",prevStepID);
          //  sql =sql+ " AND PrevStepID='"+prevStepID+"'";

        }
        return Db.delete("WorkFlowTask",del);

    }
    /// <summary>
    /// 完成任务
    /// </summary>
    /// <param name="executeModel"></param>
    /// <param name="isCompleteTask">是否需要调用Completed方法完成当前任务</param>
    private static Result executeComplete(Execute executeModel, RunModel wfInstalled, Boolean isCompleteTask)
    { Result result= new Result();
        isCompleteTask=(executeModel.getExecuteType()== EnumType.ExecuteType.Completed?true:false);
        if (executeModel.getStepid().toLowerCase().equals(StringUtil.GuidEmpty()) || executeModel.getFlowid().equals(StringUtil.GuidEmpty()))
        {
            result.setDebugmessages("完成流程参数错误");
            result.setIssuccess(false);
            result.setMessages("完成流程参数错误");
            return result;
        }
        WorkFlowTask task = instance.findById(executeModel.getTaskid());
        if (task == null)
        {
            result.setDebugmessages("未找到当前任务");
            result.setIssuccess(false);
            result.setMessages("未找到当前任务");
            return result;

        }
        else if (isCompleteTask && task.getStatus().equals(2)||task.getStatus().equals(3)||task.getStatus().equals(4))
        {    result.setDebugmessages("当前任务已处理");
            result.setIssuccess(false);
            result.setMessages("当前任务已处理");
            return result;

        }
        if (isCompleteTask)
        {
            Completed(task.getID(), executeModel.getComment(), executeModel.getIssign(),null,null);
        }
try
{
    Databases databases= wfInstalled.getDatabases().get(0);
    Record s=new Record();
    s.set(databases.getPrimaryKey(),task.getInstanceID());
    s.set(wfInstalled.getTitleField().getField(),"1");
    Db.update( databases.getTable(),"id",s);
}catch (Exception ex){}


//        var parentTasks = GetBySubFlowGroupID(task.GroupID);
//        if (parentTasks.Count > 0)
//        {
//            var parentTask = parentTasks.First();
//            var wfl = WebOS.Dal.WorkFlowDal.Instance.Get(executeModel.FlowID);
//            string error = "";
//            var flowRunModel = WorkFlowBll.Instance.GetWorkFlowRunModel(wfl.RunJSON, out error);
//            //   var flowRunModel = GetWorkFlowRunModel(parentTask.FlowID);
//            if (flowRunModel != null)
//            {
//                var steps = flowRunModel.Steps.Where(p => p.ID == parentTask.StepID);
//                if (steps.Count() > 0 && !steps.First().Event.SubFlowCompletedBefore.IsNullOrEmpty())
//                {
//                    ExecuteFlowCustomEvent(steps.First().Event.SubFlowCompletedBefore.Trim(), new WorkFlowCustomEventParams()
//                    {
//                        FlowID = parentTask.FlowID,
//                        GroupID = parentTask.GroupID,
//                        InstanceID = parentTask.InstanceID,
//                        StepID = parentTask.StepID,
//                        TaskID = parentTask.ID
//                    });
//                }
//            }
//        }
//
        result.setDebugmessages("已完成");
        result.setIssuccess(false);
        result.setMessages("已完成");
        return result;

    }
    /// <summary>
    /// 完成一个任务
    /// </summary>
    /// <param name="taskID"></param>
    /// <param name="comment"></param>
    /// <param name="isSign"></param>
    /// <returns></returns>
    public static boolean Completed(String taskID, String comment, Boolean isSign, Integer status, String note)
    {

        isSign=isSign==null?false:isSign;
        status=status==null?2:status;
        note=note==null?"":note;
        WorkFlowTask task = instance.findById(taskID);
        if (task != null)
        {
            task.setCompletedTime1(new Date());
            if (comment!=null||!comment.equals("")){
                task.setComment(comment);
            }

            task.setIsSign(isSign ? 0 : 1);
            task.setStatus(status);
            task.setNote(note);
            return task.update();
        }
        return false;
    }
    /// <summary>
    /// 得到一个流程实例一个步骤的任务
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
    public static List<WorkFlowTask> GetTaskList(String flowID, String stepID, String groupID)
    {
      SqlPara sqlPara=  Db.getSqlPara("oa-workflowtask.gettasklist",new Kv().by("flowID",flowID).set("stepID",stepID).set("groupID",groupID));
        return instance.find(sqlPara);
    }

    /// <summary>
    /// 得到和一个任务同级的任务
    /// </summary>
    /// <param name="taskID">任务ID</param>
    /// <param name="isStepID">是否区分步骤ID，多步骤会签区分的是上一步骤ID</param>
    /// <returns></returns>
    public static List<WorkFlowTask> GetTaskList(String taskID, Boolean isStepID)
    {
        isStepID=isStepID==null?true:isStepID;
        WorkFlowTask task = instance.findById(taskID);
        if (task == null)
        {
            return null;
        }
        String sql = String.format("SELECT * FROM workflowtask WHERE PrevID='"+task.getPrevID()+"' AND %s", isStepID ? "StepID='"+task.getStepID()+"'" : "PrevStepID='"+task.getPrevStepID() +"'");

        return instance.find(sql);

    }

    public static List<WorkFlowTask> getcomment(String query) {
        Query querys = JSON.parseObject(query, Query.class);
       List<WorkFlowTask> d= instance.find("SELECT * FROM workflowtask WHERE InstanceID='"+ querys.getInstanceid()+"' and FlowID='"+querys.getFlowid()+"' and GroupID='"+querys.getGroupid()+"' AND comment  IS NOT NULL ORDER BY sort ");
       return  d;
    }



    /// <summary>
    /// 得到一个实例的任务
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
    public static List<WorkFlowTask> GetTaskList(String flowID, String groupID)
    {
        if (flowID != null || flowID.equals(StringUtil.GuidEmpty()))
        {
            return instance.find("select * from WorkFlowTask where groupID='"+groupID+"'");
        }
        else
        {
            return instance.find("select * from WorkFlowTask where groupID='"+groupID+"' and flowID='"+flowID+"'");
        }

    }

    /// <summary>
    /// 创建临时任务（待办人员看不到）
    /// </summary>
    /// <param name="executeModel"></param>
    private static List<WorkFlowTask> createTempTasks(Execute executeModel, WorkFlowTask currentTask,RunModel wfInstalled)
    {
       List<WorkFlowTask> tasks = new ArrayList<WorkFlowTask>();
//       for (com.jfinal.template.expr.ast.Map step : executeModel.getSteps())
//        {
//            for (var user in step.Value)
        java.util.Map<String, List<Users>> step= executeModel.getSteps();
        Set<Map.Entry<String, List<Users>>> entry =  step.entrySet();
//            for (int i=0; step.size()>i;i++)
        for (Map.Entry<String, List<Users>> m : entry) {
            for (Users user : step.get(m.getKey())) {
            {
                List<Steps> nextSteps = wfInstalled.getSteps().stream().filter(p -> p.getId().equals( m.getKey().toLowerCase())).collect(Collectors.toList());
                if (nextSteps.size() == 0)
                {
                    continue;
                }
                Steps nextStep = nextSteps.get(0);
                WorkFlowTask task = new WorkFlowTask();

                if (!nextStep.getWorkTime().equals(""))
                {
                    int gs= Integer.parseInt(nextStep.getWorkTime());
                    Date date =new   Date(); //取时间
                    Calendar   calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.HOUR,gs); //把日期往后增加一天,整数  往后推,负数往前移动
                    date=calendar.getTime(); //这个时间就是日期往后推一天的结果
                    task.setCompletedTime(new Date());
                }

                task.setFlowID( executeModel.getFlowid());
                task.setGroupID( currentTask != null ? currentTask.getGroupID() : executeModel.getGroupid());
                task.setID ( StringUtil.getPrimaryKey());
                task.setType ( 0);
                task.setInstanceID ( executeModel.getInstanceid());
                if (!executeModel.getNote().equals(null))
                {
                    task.setNote ( executeModel.getNote());
                }
                task.setPrevID ( currentTask.getID());
                task.setPrevStepID ( currentTask.getStepID());
                task.setReceiveID ( user.getID());
                task.setReceiveName ( user.getName());
                task.setReceiveTime ( new Date());
                task.setSenderID ( executeModel.getSender().getID());
                task.setSenderName ( executeModel.getSender().getName());
                task.setSenderTime ( task.getReceiveTime());
                task.setStatus ( -1);
                task.setStepID (m.getKey());
                task.setStepName ( nextStep.getName());
                task.setSort ( currentTask.getSort()+ 1);
                task.setTitle ( executeModel.getTitle()==null ? currentTask.getTitle() : executeModel.getTitle());
                task.setUrls ( executeModel.getUrls());
                if (!HasNoCompletedTasks(executeModel.getFlowid(), m.getKey(), currentTask.getGroupID(), user.getID()))
                {
                    task.save();

                }
                tasks.add(task);
            }
        }
        }
        return tasks;
    }


    /// <summary>
    /// 查询一个用户在一个步骤是否有未完成任务
    /// </summary>
    /// <param name="flowID"></param>
    /// <returns></returns>
    public static Boolean HasNoCompletedTasks(String flowID, String stepID, String groupID, String userID)
    {
       List< WorkFlowTask> tasks = instance.find("select *from WorkFlowTask where Status In(0, 1) and flowID='"+flowID+"' and groupID='"+groupID+"' and stepID='"+stepID+"' and ReceiveID='"+userID+"'");
        return tasks.size()>0 ?true:false;
    }
    /// <summary>
    /// 判断一个步骤是否通过
    /// </summary>
    /// <param name="step"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
//    private bool IsPassing(Steps step, String flowID, String groupID, String taskID, int sort)
//    {
//        var tasks = GetTaskList(flowID, step.ID, groupID).FindAll(p => p.Sort == sort && p.Type != 5);
//        if (tasks.Count == 0)
//        {
//            return false;
//        }
//        bool isPassing = true;
//        switch (step.Behavior.HanlderModel)
//        {
//            case 0://所有人必须处理
//            case 3://独立处理
//                isPassing = tasks.Where(p => p.Status != 2).Count() == 0;
//                break;
//            case 1://一人同意即可
//                isPassing = tasks.Where(p => p.Status == 2).Count() > 0;
//                break;
//            case 2://依据人数比例
//                isPassing = (((decimal)(tasks.Where(p => p.Status == 2).Count() + 1) / (decimal)tasks.Count) * 100).Round() >= (step.Behavior.Percentage <= 0 ? 100 : step.Behavior.Percentage);
//                break;
//        }
//        return isPassing;
//    }

    /// <summary>
    /// 判断一个步骤是否退回
    /// </summary>
    /// <param name="step"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
    private static boolean IsBack(Steps step, String flowID, String groupID,String taskID, int sort)
    {
        List<WorkFlowTask> tasks = GetTaskList(flowID, step.getId(), groupID).stream().filter(WorkFlowTask -> WorkFlowTask.getSort() == sort && WorkFlowTask.getType() != 5).collect(Collectors.toList());
        if (tasks.size() == 0)
        {
            return false;
        }
        boolean isBack = true;
        switch (step.getBehavior().getHanlderModel())
        {
            case "0"://所有人必须处理
            case "3"://独立处理
                isBack = tasks.stream().filter(WorkFlowTask -> WorkFlowTask.getStatus().equals(3)&&WorkFlowTask.getStatus().equals(5)).collect(Collectors.toList()).size() > 0;
                break;
            case "1"://一人同意即可
                isBack = tasks.stream().filter(WorkFlowTask -> WorkFlowTask.getStatus().equals(2)&&WorkFlowTask.getStatus().equals(4)).collect(Collectors.toList()).size() == 0;
                break;
           // case 2://依据人数比例
              //  isBack = (((Decimal)(tasks.Where(p => p.Status.In(3, 5)).Count() + 1) / (decimal)tasks.Count) * 100).Round() >= 100 - (step.Behavior.Percentage <= 0 ? 100 : step.Behavior.Percentage);
              //  break;
        }
        return isBack;
    }
    static boolean UpdateTempTasks(String flowID, String stepID, String groupID, Date completedTime, Date receiveTime)
    {

        WorkFlowTask da = instance.findFirst("select *from WorkFlowTask where FlowID = '"+flowID+"' and StepID='"+stepID+"' and GroupID='"+groupID+"'");

        if (da != null)
        {
            da.setCompletedTime(null);
            da.setReceiveTime(new Date());
            da.setSenderTime(receiveTime);;
            return da.update();
        }
        return false;

    }

    /// <summary>
    /// 删除流程实例
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
    public boolean DeleteInstance(String flowID, String groupID, Boolean hasInstanceData,RunModel wfInstalleds)
    {
        hasInstanceData=hasInstanceData==null?false:hasInstanceData;
        if (hasInstanceData)
        {
           List<WorkFlowTask> tasks = GetTaskList(flowID, groupID);
            if (tasks.size() > 0 && !tasks.get(0).getInstanceID().equals(StringUtil.GuidEmpty()))
            {

                Workflow wfl=   CacheKit.get("flowcache",flowID);
                if (wfl==null){
                     wfl = WorkflowService.Get(flowID);
                    CacheKit.put("flowcache",flowID,wfl);
                }

                String error = "";
                String wfInstalled = wfl.getRunJSON();

                if (wfInstalleds != null && wfInstalleds.getDatabases().size() > 0)
                {
                    Databases dataBase = wfInstalleds.getDatabases().get(0);
                    DeleteTabel(dataBase.getTable(), dataBase.getPrimaryKey(), tasks.get(0).getInstanceID());
                }
            }
        }


        return DeleteInstance(flowID, groupID);
    }

    /// <summary>
    /// 判断一个步骤是否通过
    /// </summary>
    /// <param name="step"></param>
    /// <param name="groupID"></param>
    /// <returns></returns>
    private static Boolean IsPassing(Steps step, String flowID, String groupID, String taskID, int sort)
    {
        List<WorkFlowTask> tasks =  GetTaskList(flowID, step.getId(), groupID).stream().filter(p ->  p.getSort().equals(sort) && !p.getType().equals(5) ).collect(Collectors.toList());
        if (tasks.size() == 0)
        {
            return false;
        }
        Boolean isPassing = true;
        switch (Integer.parseInt( step.getBehavior().getHanlderModel()))
        {
            case 0://所有人必须处理
            case 3://独立处理
                isPassing = tasks.stream().filter(p -> !(p.getStatus().equals(2))).collect(Collectors.toList()).size() == 0;
                break;
            case 1://一人同意即可
                isPassing = tasks.stream().filter(p -> p.getStatus().equals(2)).collect(Collectors.toList()).size() > 0;
                break;
            case 2://依据人数比例
                isPassing = (((tasks.stream().filter(p -> p.getStatus().equals(2)).collect(Collectors.toList()).size() + 1) / tasks.size()) * 100) >= (Double.parseDouble( step.getBehavior().getPercentage() )<= 0 ? 100 : Double.parseDouble( step.getBehavior().getPercentage() ));
                break;
        }
        return isPassing;
    }
    private boolean DeleteInstance(String flowID, String groupID) {
        Record del=new Record();
        del.set("GroupID",groupID);

        if (flowID!=null){

            del.set("FlowID",flowID);
        }
       return Db.delete("workflowtask",del);
    }

    private void DeleteTabel(String table, String primaryKey, String instanceID) {
        Db.deleteById(table,primaryKey,instanceID);
    }

    /// <summary>
    /// 根据subflow得到一个任务
    /// </summary>
    /// <param name="subflowGroupID"></param>
    /// <returns></returns>
    public List<WorkFlowTask> GetBySubFlowGroupID(String subflowGroupID)
    {
        String sql = "SELECT * FROM workflowtask WHERE SubFlowGroupID='"+subflowGroupID+"'";
        return instance.find(sql);
    }
    /// <summary>
    /// 得到一个流程当前步骤的后续步骤集合
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="stepID"></param>
    /// <returns></returns>
    public static List<Steps> GetNextSteps(String flowID, String stepID, RunModel wfInstalled)
    {
        List<Steps> stepList = new ArrayList<Steps>();
        List<Lines> lines = wfInstalled.getLines().stream().filter(Lines -> Lines.getFrom().equals( stepID)).collect(Collectors.toList());
        for (Lines line : lines)
        {
           List <Steps> step = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(line.getTo())).collect(Collectors.toList());
            if (step.size() > 0)
            {
                stepList.add(step.get(0));
            }
        }
        return stepList.stream().distinct().collect(Collectors.toList());
    }
    /// <summary>
    /// 得到一个流程步骤的前面步骤集合
    /// </summary>
    /// <param name="flowID"></param>
    /// <param name="stepID"></param>
    /// <returns></returns>
    public static List<Steps> GetPrevSteps(String flowID, String stepID, RunModel wfInstalled)
    {
        List<Steps> stepList = new ArrayList<Steps>();

        List<Lines> lines = wfInstalled.getLines().stream().filter(Steps -> Steps.getTo().equals( stepID)).collect(Collectors.toList());
        for (Lines line : lines)
        {
            List<Steps> step = wfInstalled.getSteps().stream().filter(Steps -> Steps.getId().equals(line.getFrom())).collect(Collectors.toList());
            if (step.size() > 0)
            {
                stepList.add(step.get(0));
            }
        }
        return stepList;
    }
    /// <summary>
    /// 得到一个任务可以退回的步骤
    /// </summary>
    /// <param name="taskID">当前任务ID</param>
    /// <param name="backType">退回类型</param>
    /// <param name="stepID"></param>
    /// <returns></returns>
    public static List<Record> GetBackSteps(String taskID, int backType, String stepID, RunModel wfInstalled)
    {
        List<Record> dict = new ArrayDeque<>();
        List<Steps> steps = wfInstalled.getSteps().stream().filter(p -> p.getId().equals( stepID)).collect(Collectors.toList());
        if (steps.size() == 0)
        {
            return null;
        }
        Steps step = steps.get(0);
        WorkFlowTask task = Get(taskID);
        //加签退回给发送人
        if (task != null && task.getType().equals(7) )
        {
            Record record=new
                    Record();
            record.set("id",StringUtil.GuidEmpty());
            record.set("name",task.getSenderName());
            record.set("list",false);
            dict.add(record);
            return dict;
        }
        //独立退回退回给发送人
        if (task != null &&  step.getBehavior().getBackModel().equals("4"))
        {
            List<Steps> backSte = wfInstalled.getSteps().stream().filter(p -> p.getId().equals(task.getPrevStepID())).collect(Collectors.toList());
            if (backSte.size()>0){
                Steps backStep=backSte.get(0);
                Record record=new
                        Record();
                record.set("id",StringUtil.GuidEmpty());
                record.set("name",backStep.getName());
                dict.add(record);
                return dict;
              //  dict.put(StringUtil.GuidEmpty(),  backStep.getName() + "(" + task.getSenderName() + ")" );

            }
        return dict;
        }
        switch (backType)
        {
            case 0://退回前一步
                if (task != null)
                {
                    if (!step.getBehavior().getCountersignature().equals("0"))//如果是会签步骤，则要退回到前面所有步骤
                    {
                        List<Steps> backSteps = GetPrevSteps(task.getFlowID(), step.getId(),wfInstalled);
                      int i=0;
                        for (Steps backStep : backSteps)
                        {   Record record=new  Record();
                            record.set("id",backStep.getId());
                            record.set("name",backStep.getName());
                            dict.add(record);
                        }

                    }
                    else
                    {
                        Record record=new  Record();
                        record.set("id",task.getPrevStepID());
                        record.set("name",GetStepName(task.getPrevStepID(), wfInstalled));
                        dict.add(record);
                       // dict.put(task.getPrevStepID(), GetStepName(task.getPrevStepID(), wfInstalled));
                    }
                }
                break;
            case 1://退回第一步
                Record record=new  Record();
                record.set("id",wfInstalled.getSteps().get(0).getId());
                record.set("name", GetStepName(wfInstalled.getSteps().get(0).getId(), wfInstalled));
                dict.add(record);
              //  dict.put(wfInstalled.getSteps().get(0).getId(), GetStepName(wfInstalled.getSteps().get(0).getId(), wfInstalled));
                break;
            case 2://退回某一步
                if (step.getBehavior().getBackModel().equals("2") && !step.getBehavior().getBackStep().equals(StringUtil.GuidEmpty()))
                {
                    Record record1=new  Record();
                    record1.set("id",step.getBehavior().getBackStep());
                    record1.set("name", GetStepName(step.getBehavior().getBackStep(), wfInstalled));
                    dict.add(record1);
                 //   dict.put(step.getBehavior().getBackStep(), GetStepName(step.getBehavior().getBackStep(), wfInstalled));
                }
                else
                {
                    if (task != null)
                    {
                        List<WorkFlowTask> taskList = GetTaskList(task.getFlowID(), task.getGroupID()).stream().filter(p -> p.getStatus().equals(2)|| p.getStatus().equals(3)|| p.getStatus().equals(4)).sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(Collectors.toList());
                        for (WorkFlowTask task1 : taskList)
                        {
                            if (dict!=null){
                            if (dict.stream().filter(p->p.getStr("id").equals(task1.getStepID())).collect(Collectors.toList()).size()>0 && task1.getStepID().equals( stepID))
                            {
                                Record record1=new  Record();
                                record1.set("id",task1.getStepID());
                                record1.set("name", GetStepName(task1.getStepID(), wfInstalled));
                                dict.add(record1);
                              //  dict.put(task1.getStepID(), GetStepName(task1.getStepID(), wfInstalled));
                            }
                        }
                        }
                    }
                }
                break;
        }
        return dict;
    }
    /// <summary>
    /// 根据步骤ID得到步骤名称
    /// </summary>
    /// <param name="stepID"></param>
    /// <param name="flowID"></param>
    /// <param name="defautFirstStepName">如果步骤ID为空是否默认为第一步</param>
    /// <returns></returns>
    public static String GetStepName(String stepID,RunModel wfinstalled)
    {
        if (wfinstalled == null) return "";

        String finalStepID = stepID;
        List<Steps> steps = wfinstalled.getSteps().stream().filter(p -> p.getId().equals(finalStepID)).collect(Collectors.toList());
        return steps.size() > 0 ? steps.get(0).getName() : "";
    }

    /// <summary>
    /// 根据步骤ID得到步骤名称
    /// </summary>
    /// <param name="stepID"></param>
    /// <param name="flowID"></param>
    /// <param name="defautFirstStepName">如果步骤ID为空是否默认为第一步</param>
    /// <returns></returns>
    public static String GetStepName(String stepID, RunModel wfinstalled, Boolean defautFirstStepName)
    {
        if (wfinstalled == null) return "";
        if (stepID.equals( StringUtil.GuidEmpty()) && defautFirstStepName)
        {
            stepID = wfinstalled.getSteps().get(0).getId();
        }
        String finalStepID = stepID;
        List<Steps> steps = wfinstalled.getSteps().stream().filter(p -> p.getId().equals(finalStepID)).collect(Collectors.toList());
        return steps.size() > 0 ? steps.get(0).getName() : "";
    }
}
