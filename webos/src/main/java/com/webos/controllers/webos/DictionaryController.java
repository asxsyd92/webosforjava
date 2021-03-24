package com.webos.controllers.webos;

import com.asxsydutils.utils.JosnUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jwt.JwtInterceptor;
import com.webcore.annotation.Route;
import com.webcore.modle.Dictionary;
import com.webcore.service.DictionaryService;

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
