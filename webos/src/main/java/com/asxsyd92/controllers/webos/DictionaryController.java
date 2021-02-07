package com.asxsyd92.controllers.webos;

import com.asxsyd92.annotation.Route;
import com.asxsyd92.service.DictionaryService;
import com.asxsyd92.modle.Dictionary;
import com.asxsyd92.utils.JosnUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jwt.JwtInterceptor;

@Route(Key = "/api/dictionary")
public class DictionaryController extends Controller {
    @Before({JwtInterceptor.class,POST.class})
   public void GetByCode() throws Exception {
       String code=getPara("code");
       ;
       Object da =  new JosnUtils<Dictionary>().toJson(DictionaryService.GetByCode(code));

        setAttr("data", da);
        setAttr("success", true);

    renderJson();
    }
}
