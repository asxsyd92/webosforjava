package com.asxsyd92.Controllers;

import com.asxsyd92.service.NewsService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class NewController  extends Controller {
//获取新闻列表
    public void GetNew() {
        try {
            int page =  getParaToInt("page");
            int limit = getParaToInt("limit");
            String title = getPara("title");
            String type = getPara("type");
            if (page==0) page=1;
            if (limit==0) limit=15;
            Page<Record> news=    NewsService.GetNew(title,type,page,limit);
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", news.getTotalRow());
            setAttr("data",news.getList());
            setAttr("success", true);
        }catch (Exception x){
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count",0);
            setAttr("data",null);
            setAttr("success", false);

        }renderJson();
    }

 public void  TaoBaoSearch(){

     try {
         int page =  getParaToInt("page");
         int limit = getParaToInt("limit");
         String title = getPara("title");
         String type = getPara("type");
         if (page==0) page=1;
         if (limit==0) limit=15;
         List<Record> news=    NewsService.TaoBaoSearch(title,page,limit);
         setAttr("msg", "");
         setAttr("code", 0);
         setAttr("count", 10);
         setAttr("data",news.toArray());
         setAttr("success", true);
     }catch (Exception x){
         setAttr("msg", x.getMessage());
         setAttr("code", 0);
         setAttr("count",0);
         setAttr("data",null);
         setAttr("success", false);

     }renderJson();

 }
}
