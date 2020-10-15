package com.asxsyd92.controllers;

import com.baidu.ueditor.ActionEnter;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import org.json.JSONException;

import java.io.File;


public class UeditorController  extends Controller {
    @Clear
    public void upload() throws JSONException {
        String users = getPara("asxsyd92user");
      //  String outText = ActionEnter.me().exec(getRequest());
        String rootPath = getRequest().getSession().getServletContext().getRealPath(File.separator+"asxsyd92upload"+File.separator+users.toLowerCase()+File.separator);
        String configtPath = getRequest().getSession().getServletContext().getRealPath(File.separator);

        String outText = new ActionEnter(getRequest(),rootPath).exec();
       // setAttr(outText);
        renderText(outText);
       // renderJson(outText);
      //  renderHtml();
       // request.setCharacterEncoding("utf-8");
     //   String rootPath = request.getRealPath("/"
      //  request
    //  render(   new ActionEnter( request, rootPath ).exec());
    }
}
