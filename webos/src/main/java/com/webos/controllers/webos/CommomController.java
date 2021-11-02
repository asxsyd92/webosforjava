package com.webos.controllers.webos;

import com.alibaba.fastjson.JSON;

import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtInterceptor;
import com.webcore.modle.Dictionary;
import com.webcore.service.CommomService;
import com.webcore.service.DictionaryService;
import com.webcore.utils.Unity;
import com.webcore.utils.data.mysqlserver.FromData;
import io.jsonwebtoken.Claims;

import java.util.Map;

@Path("/api/common")
public class CommomController extends Controller {

    /// <summary>
    /// 公共单表保存
    /// </summary>
    /// <param name="tab"></param>
    /// <param name="data"></param>
    /// <returns></returns>
    @Before({JwtInterceptor.class, POST.class})
    public void save()
    {
        String tab= getPara("tab");
        String data= getPara("data");
        if (tab == null || tab == "") {
            setAttr("success", false);
            setAttr("msg", "标准符错误！");
        }else {
            Claims claims = getAttr("claims");
            Unity.getJsonSetData(data,claims);
            Record os=new Record();
            Map da = JSON.parseObject(data, Map.class);
            os.setColumns(da);
            String id="";
            try {
                id= FromData.save(os,tab);
            }catch (Exception ex){
                setAttr("msg", ex.getMessage());
                setAttr("success", false);
                renderJson();
            }
            if (id.equals(StringUtil.GuidEmpty())){
                setAttr("success", false);
                setAttr("msg", "添加失败！");
            }else {
                setAttr("success", true);
                setAttr("msg", "添加成功！");
            }
        }
      renderJson();
    }
    @Before({JwtInterceptor.class, POST.class})
    public void Del()
    {
        String key= getPara("key");
        String table= getPara("table");
        if (table.isEmpty())
          {
              setAttr("success", false);
              setAttr("msg", "标准符错误！");
          }
            else
        {
           if( Db.deleteById(table,"id",key)){
               setAttr("success", true);
               setAttr("msg", "删除成功！");
           }else {
               setAttr("success", false);
               setAttr("msg", "删除失败！");
           }

        }
      renderJson();
    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="tab">查询哪张表</param>
    /// <param name="title">查询条件</param>
    /// <param name="page"></param>
    /// <param name="limit"></param>
    /// <returns></returns>
    //获取公共list
    @Before({JwtInterceptor.class, GET.class})

    public void GetCommonList()
    {
        String tab= getPara("tab");
        String type= getPara("type");
        String title= getPara("title");
        String desc= getPara("desc");
        int page =  getParaToInt("page");
        int limit = getParaToInt("limit");

        if (tab == null || tab == "")
        {
            setAttr("success", false);
            setAttr("msg", "标准符错误！");

        }else {
            Kv kv=Kv.by("tab",tab).set("type",type).set("title",title).set("desc",desc);
            Page<Record> da= CommomService.Instance().GetCommonList(kv,page,limit);
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("count", da.getTotalRow());
            setAttr("data", da.getList());


    }
    renderJson();
    }

    @Before({JwtInterceptor.class, POST.class})
    public void GetDictionary(){


        Kv kv=Kv.by("parentid", StringUtil.GuidEmpty());
        try {
            setAttr("count",10);
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("data", new JosnUtils<Dictionary>().toJson(DictionaryService.GetDictionary(kv)));
        }
        catch (Exception ex){
            setAttr("data",ex.getLocalizedMessage());
        }

        renderJson();

    }
}
