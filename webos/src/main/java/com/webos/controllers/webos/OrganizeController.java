package com.webos.controllers.webos;

import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.security.JwtInterceptor;
import com.webcore.modle.Organize;
import com.webcore.service.OrganizeService;

import java.util.List;
@Before({ POST.class, JwtInterceptor.class})
@Path( "/api/organiz")
public class OrganizeController extends Controller {
    @Inject
    OrganizeService  service;
    public void GetOrganizeById(){
        try{
    String id = getPara("id");
    if(id==null||id.equals("")){
        id= StringUtil.GuidEmpty();

    }
    List<Organize> data= service.GetOrganizeById(id);
    Object da=   new JosnUtils<Organize>().toJson(data);
    setAttr("data", da);
            setAttr("msg", "获取成功！");
    setAttr("success", true);
        }catch (Exception ex){
            setAttr("msg", "失败！");
            setAttr("success", false);
        }
    renderJson();
    }
}
