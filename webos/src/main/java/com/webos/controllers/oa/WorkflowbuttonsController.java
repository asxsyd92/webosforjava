package com.webos.controllers.oa;

import java.util.List;

import com.webcore.annotation.Route;
import com.webcore.service.WorkflowbuttonsService;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtInterceptor;

@Route(Key="/api/workflowbuttons")
@Before({JwtInterceptor.class})
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