package com.asxsyd92.controllers;


import com.asxsyd92.dal.RoleAppDal;
import com.asxsyd92.utils.data.mysqlserver.FromData;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.UUID;

public class HomeController extends Controller {
    @Clear
    public void index() {
        render("index.html");

    }


}