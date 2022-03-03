package com.portalsite.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.webcore.service.CommomService;
import com.webcore.service.TaoBaoService;
import com.webcore.utils.data.mysqlserver.FromData;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Path ("/api/portalsite")
@Clear
public class PortalSiteController extends Controller {
    public static String appUrl = "http://gw.api.taobao.com/router/rest";
    public static String appUrlc = "https://eco.taobao.com/router/rest";
    public static String appKey = "25230632";
    public static String appSecret = "4de054e17b815f7d182cd7242548f0b2";
    public void GetNew() {
        String title = getPara("title");
        String type = getPara("type");
        int page = getInt("page");
        int limit = getInt("limit");
        Kv kv = Kv.by("tab", "a_article").set("type", type).set("title", title).set("desc", "Adddate");
        Page<Record> da = CommomService.Instance().GetCommonList(kv, page, limit);
        setAttr("code", 0);
        setAttr("msg", "成功！");
        setAttr("success", true);
        setAttr("count", da.getTotalRow());
        setAttr("data", da.getList());
        renderJson();
    }
   public void GetRandomNew(){
        String sql="select  *from a_article order by rand() desc LIMIT 20";
    List<Record>  index=  Db.find(sql);

    setAttr("data",   index);
    setAttr("success", true);
    renderJson();
    }
    public  void GetRandomDown(){
        String sql="select  *from download order by rand() desc LIMIT 20";
        List<Record>  index=  Db.find(sql);

        setAttr("data",   index);
        setAttr("success", true);
        renderJson();
    }
    public void GetNewIndex() {
        //获取缓存中数据

            String id = getPara("id");
            String sql ="SELECT * FROM `a_article` WHERE Classid LIKE'%"+id+"%' order by rand() LIMIT 10";// "select * from ( SELECT   *,'cai'  type FROM a_article     order by rand() desc LIMIT 10) as t UNION (SELECT  *,'rewen' as type FROM a_article where Classid='" + rewen + "'  LIMIT 10)UNION (SELECT *,'tuijian' as type FROM a_article where Classid='" + tuijian + "'  LIMIT 10)";
            List<Record>  index=  Db.findByCache(id,"getsiteindex",sql);

        setAttr("data",   index);
        setAttr("Success", true);
        renderJson();
    }

    public void GetNewDetails()
    {
        String id = getPara("id");
       Record da= Db.findById("a_article","id",id);
        if (da!=null){
            try {
             int views=   da.getInt("views")+1;
             da.set("views",views);
            }catch (Exception ex){
                da.set("views",1);
            }
            try {
                FromData.save(da,"a_article");
            }catch (Exception ex){
                System.out.println(ex);
            }
            Record Nextarticle= Db.findFirst("select   *from A_Article where id< '" + id + "'  ORDER BY ID DESC");

            Record Lastarticle= Db.findFirst("select   *from A_Article where id> '" + id + "'  ORDER BY ID DESC");

            setAttr("code",   0);
            setAttr("msg", "成功！");
            setAttr("count",  1);
            setAttr("data",da );
            setAttr("success", true);
            setAttr("nextarticle",  Nextarticle);
            setAttr("lastarticle", Lastarticle);
        }

    renderJson();
    }
public  void getTaoBao(){
        try
        {
    List<Record> da = Db.find("select  *from t_map_dataitem order by rand() desc LIMIT 10");
    setAttr("code", 0);
    setAttr("msg", "成功！");
    setAttr("count", 1);
    setAttr("data", da);
        } catch (Exception ex){
            setAttr("code", 0);
            setAttr("msg", ex.getMessage());
            setAttr("count", 1);
            setAttr("data", null);
        }
        renderJson();
}
    public void TaoBaoSearch(){
        try {

            String title = getPara("title");
            Integer page = getInt("page");
            Integer limit = getInt("limit");
if (title==null){
    Page<Record> da= TaoBaoService.GetDataitemList(Kv.by("title",title),page,limit);
    setAttr("code", 0);
    setAttr("msg","成功");
    setAttr("count", da.getTotalRow());
    setAttr("data", da.getList());
}else {
    TaobaoClient client = new DefaultTaobaoClient(appUrl, appKey, appSecret,"json");
  //  TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
    TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
    Long l=Long.parseLong(limit.toString());
    Long p=Long.parseLong(page.toString());
    req.setPageSize(l);
    req.setPageNo(p);

    req.setAdzoneId(71272250295L);
    req.setQ(title);
    TbkDgMaterialOptionalResponse rsp = client.execute(req);
    Record os=new Record();
    Map<String,Object> mt= JSON.parseObject(rsp.getBody(), Map.class);
    os.setColumns(mt);
   String res= os.getStr("tbk_dg_material_optional_response");
   Integer total_results=0;
   if (res!=null){
       Map<String,Object> sd= JSON.parseObject(res, Map.class);

       os.setColumns(sd);
       total_results=os.getInt("total_results");
      String data= os.getStr("result_list");
       Map<String,Object> sds= JSON.parseObject(data, Map.class);
       os.setColumns(sds);

       List dsa= JSONArray.parseObject(os.getStr("map_data"), List.class);
       for (Object t : dsa) {
           Record sss=new Record();
           Map<String,Object> map = JSONObject.parseObject(JSON.toJSONString(t));
           sss.setColumns(map);
         Record da=  Db.findById("t_map_dataitem", "num_iid", sss.getStr("num_iid"));
         if (da== null){
             try {
                 sss.set("datatime",new Date());
                 sss.set("id", StringUtil.getPrimaryKey());
                 FromData.save(sss, "t_map_dataitem");
             } catch (Exception ex) {
                 System.out.println(ex.getMessage());
             }
         }
       }
       setAttr("code", 0);
       setAttr("msg","获取成功");
       setAttr("count", total_results);
       setAttr("data", dsa);
   }else {
       setAttr("code", 0);
       setAttr("msg","无结果");
       setAttr("count", 1);
       setAttr("data", null);
   }

}
        }catch (Exception ex){
            setAttr("code", 0);
            setAttr("msg", ex.getMessage());
            setAttr("count", 1);
            setAttr("data", null);
        }
        renderJson();
    }
public  void  getDownload(){
    try
    {
        String title = getPara("title");
        int page = getInt("page");
        String type = getPara("type");
        int limit = getInt("limit");
        Kv kv = Kv.by("title", title).set("type",type);
        Page<Record> da=  CommomService.Instance().getDownload(page,limit,kv);

        setAttr("code", 0);
        setAttr("msg", "成功！");
        setAttr("success", true);
        setAttr("count", da.getTotalRow());
        setAttr("data", da.getList());
    } catch (Exception ex){
        setAttr("code", 0);
        setAttr("msg", ex.getMessage());
        setAttr("count", 1);
        setAttr("data", null);
    }
    renderJson();

}
    public  void  getgroup(){
        try
        {
            String group = getPara("group");
            List<Record> da=Db.find("SELECT * FROM download WHERE groupname= '"+group+"' ORDER BY Title");
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("success", true);
            setAttr("count", 0);
            setAttr("data", da);
        } catch (Exception ex){
            setAttr("code", 0);
            setAttr("msg", ex.getMessage());
            setAttr("count", 1);
            setAttr("data", null);
        }
        renderJson();

    }

public  void  setHot(){
    try
    {
        String id = getPara("id");
     Record record= Db.findById("download","ID",id);
  if (record!=null){
     Integer views= record.getInt("views");
     if (views==null){
         views=1;
     }else {
         views=views+1;
     }
      record.set("views",views);
      Db.update("download",record);
  }

        setAttr("code", 0);
        setAttr("msg", "成功！");
        setAttr("success", true);

    } catch (Exception ex){
        setAttr("code", 0);
        setAttr("msg", ex.getMessage());
        setAttr("count", 1);
        setAttr("data", null);
    }
    renderJson();
}
}
