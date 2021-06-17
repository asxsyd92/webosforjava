package com.webos.controllers.webos;

import com.asxsydutils.utils.JosnUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.webcore.modle.Organize;
import com.webcore.service.OrganizeService;

@Path( "/api/organiz")
public class OrganizeController extends Controller {
    @Inject
    OrganizeService  service;
    public void GetOrganizeById(){
    String id = getPara("id");
    Object da=   new JosnUtils<Organize>().toJson(service.GetOrganizeById(id));
    setAttr("data", da);
    renderJson();
    }
}
