package com.service.oa;


import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.plugin.ehcache.CacheKit;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.service.modle.Workflow;
import com.service.oa.workflow.RunModel;

import java.util.Date;

public class WorkflowService {
    private static volatile Workflow instance = new Workflow().dao();

    public static Workflow Get(String flowid) {

       return instance.findById(flowid);
    }

    public static Page<Workflow> WorkFlowList(Kv kv, Integer page, Integer limit) {
        SqlPara sqlPara =  instance.getSqlPara("oa-workflow.WorkFlowList", kv);
        return   instance.paginate(page,limit,sqlPara);
    }

    public static String Seve(String json, String receiveid) {
        RunModel jsons= JosnUtils.stringToBean(json,RunModel.class);

        String id =jsons.getId();
        String name = jsons.getName();
        String type = jsons.getType();

        if (id.equals(StringUtil.GuidEmpty()))
        {
            id = StringUtil.getPrimaryKey();
            jsons.setId(id);
            // return "请先新建或打开流程!";
        }
        if (name==null)
        {
            return "流程名称不能为空!";
        }
        else
        {

            Workflow bwf = new Workflow();
            Workflow wf=   CacheKit.get("flowcache",id);
            if (wf==null){
                wf= WorkflowService.Get(id);
                CacheKit.put("flowcache",id,wf);
            }

            Boolean isAdd = false;
            if (wf == null)
            {
                wf = new Workflow();
                isAdd = true;
                wf.setID(id);
                wf.setCreateDate(new java.util.Date()) ;
                wf.setCreateUserID (receiveid);
                wf.setStatus (1);
            }
            wf.setDesignJSON ( JosnUtils.beanToString(jsons));
            wf.setInstanceManager(jsons.getInstanceManager());
            wf.setManager(jsons.getManager()) ;
            wf.setName(name) ;
            wf.setType(jsons.getType());
            // wf.Type = type.IsGuid() ? type.ToGuid() : new DictionaryDal.().GetIDByCode("FlowTypes");
            try
            {
                if (isAdd)
                {
                    if (wf.save()) return "保存成功";
                }
                else
                {
                    if (wf.update()) return "保存成功";
                }
                return "保存失败";
            }
            catch (Exception err)
            {
                return err.getMessage();
            }
        }
    }

    public static String Install(String jsonString, String receiveid, boolean b) {
        String saveInfo = Seve(jsonString, receiveid);
        if (!"保存成功".equals(saveInfo) )
        {
            return saveInfo;
        }
        String errMsg;
        RunModel  wfInstalled= JosnUtils.stringToBean(jsonString,RunModel.class);


            Workflow wf =instance.findById(wfInstalled.getId());
            if (wf == null)
            {
                return "流程实体为空";
            }
            else
            {

                wf.setInstallDate(new Date());
                wf.setInstallUserID(receiveid);
                wf.setRunJSON(JosnUtils.beanToString(jsonString));
                wf.setStatus (2);
                wf.update();





//                AppLibrary   bappLibrary = new Modle.AppLibrary();
//                WebOS.Modle.AppLibrary app = AppLibraryDal.Instance.Get(new { Code = wfInstalled.ID.ToString() });
//                bool isAdd = false;
//                if (app == null)
//                {
//                    isAdd = true;
//                    app = new WebOS.Modle.AppLibrary();
//                    app.ID = Guid.NewGuid();
//                }
//                app.Address = isMvc ? "WorkFlowRun/Index" : "Platform/WorkFlowRun/Default.aspx";
//                app.Code = wfInstalled.ID.ToString();
//                app.Note = "流程应用";
//                app.OpenMode = 0;
//                app.Params = "flowid=" + wfInstalled.ID.ToString();
//                app.Title = wfInstalled.Name;
//                app.Type = wfInstalled.Type.IsGuid() ? wfInstalled.Type.ToGuid() : DictionaryDal.Instance.Get(new { Code = "FlowTypes" }).ID;//.GetIDByCode("");
//                if (isAdd)
//                {
//                    AppLibraryDal.Instance.Insert(app);
//                }
//                else
//                {
//                    AppLibraryDal.Instance.Update(app);
//                }
//
//
//                        #endregion

                return "安装成功";

            }
    }

    public static boolean Del(String id) {
        Workflow wf=  instance.findById(id);
        wf.setStatus(4);
       return wf.update();
    }


}
