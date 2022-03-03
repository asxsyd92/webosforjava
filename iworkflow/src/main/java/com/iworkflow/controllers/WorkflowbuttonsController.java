package com.iworkflow.controllers;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Path;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.iworkflow.service.oa.WorkflowbuttonsService;
import com.security.Authorization;


@Path("/api/workflowbuttons")
@Before({ POST.class, Authorization.class})
public class WorkflowbuttonsController extends Controller {
    //获取按钮
    public  void  ButtonByoa(){

        try{

            List<Record> mm=  WorkflowbuttonsService.getAllList();
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", mm);
            setAttr("msg", "获取成功" );
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
