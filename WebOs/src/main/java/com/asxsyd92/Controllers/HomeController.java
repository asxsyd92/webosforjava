package com.asxsyd92.Controllers;

import com.asxsyd92.Dal.RoleAppDal;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import sun.plugin.javascript.navig.LinkArray;

import java.util.List;

public class HomeController extends Controller {

    public void index() {
        render("index.html");

    }
    public void test() {
        List<RoleAppDal> da= RoleAppDal.dao.findAll();
        renderJson(da);
    }


}