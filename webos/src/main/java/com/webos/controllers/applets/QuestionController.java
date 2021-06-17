package com.webos.controllers.applets;

import com.jfinal.core.Path;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;

@Path( "/api/question")
@Clear
public class QuestionController extends Controller {
    @Before({POST.class})
    public void getQuestion() {
        String tab = getPara("tab");
        Integer limit=getParaToInt("limit");
        if (tab==null) {
            tab = "A_Question";
        }

        String sql=" SELECT   * FROM "+tab.toLowerCase()+"     order by rand() desc LIMIT "+limit;
       // String sql = "select top "+ limit + " *, NewID() as random from  " + tab + " where 1=1 " + name + " order by random";

        setAttr("data",   Db.find(sql));
        setAttr("code", 0);
        setAttr("msg", "成功");
        setAttr("count", limit);
        renderJson();
    }




}
