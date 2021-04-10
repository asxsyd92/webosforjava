package com.webos.controllers.portalsite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.webcore.annotation.Route;
import com.webcore.service.CommomService;
import com.webcore.service.TaoBaoService;
import com.webcore.utils.data.mysqlserver.FromData;


import java.util.Date;
import java.util.List;
import java.util.Map;

@Route(Key = "/api/portalsite")
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
        setAttr("count", da.getTotalRow());
        setAttr("data", da.getList());
        renderJson();
    }

    public void GetNewIndex() {
        String rewen = getPara("rewen");
        String tuijian = getPara("tuijian");
        String sql = "select * from ( SELECT   *,'cai'  type FROM a_article     order by rand() desc LIMIT 10) as t UNION (SELECT  *,'rewen' as type FROM a_article where Classid='"+rewen+ "'  LIMIT 10)UNION (SELECT *,'tuijian' as type FROM a_article where Classid='" + tuijian+ "'  LIMIT 10)";
      //  String sql = "select * from ( SELECT TOP 10 'cai' as type, *FROM A_Article    order by newid()) as t UNION (SELECT TOP 10 'rewen' as type,*FROM A_Article where Classid='"+rewen+ "')UNION (SELECT TOP 10 'tuijian' as type,*FROM A_Article where Classid='" + tuijian+ "')";
        setAttr("data",   Db.find(sql));
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

}
