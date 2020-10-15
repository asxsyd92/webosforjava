package com.asxsyd92.controllers;

import com.asxsyd92.service.NewsService;
import com.asxsyd92.utils.data.mysqlserver.FromData;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NewController  extends Controller {
//获取新闻列表
@Clear
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
    @Clear
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
    @Clear
    public void test() {
        try{

            Record o=new Record();
            o.set("ID", UUID.randomUUID().toString());
            o.set("title","测试");

            setAttr("msg",  FromData.save(o,"_new"));
        }catch (Exception ex){
            setAttr("msg", ex.getMessage());
        }
        renderJson();
    }
    @Clear
    public void tests() {
        try{
            String id = getPara("id");
            String title = getPara("title");
            Record o=new Record();
            o.set("ID", id);
            o.set("title",title);
            o.set("addtime", new java.sql.Timestamp((new Date().getTime())));
            setAttr("msg",  FromData.save(o,"_new"));
        }catch (Exception ex){
            setAttr("msg", ex.getMessage());
        }
        renderJson();
    }
}
