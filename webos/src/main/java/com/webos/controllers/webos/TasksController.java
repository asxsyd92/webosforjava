package com.webos.controllers.webos;


import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.PropKit;
import com.webcore.service.LogService;
import com.webcore.service.TaskService;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.jwt.JwtInterceptor;
import com.webos.Common;
import io.jsonwebtoken.Claims;
import kotlin.collections.ArrayDeque;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Map;
@Before({JwtInterceptor.class})
@Path("/api/tasks")
public class TasksController  extends Controller {
    @Inject
    LogService logService;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void WaitList() {
        try {
            //根据用户获取待办
            String title = getPara("title");
            String type = getPara("type");
            Date date1 = getDate("date1");
            Date date2 = getDate("date2");
            String desc = getPara("desc");
            int page = getInt("page");
            int limit = getInt("limit");
            Kv kv = Kv.by("title", title).set("type", type).set("date1", date1).set("date2", date2).set("desc", desc);

            Page<Record> da = TaskService.GetPage(page, limit, kv);
            setAttr("msg", "");
            setAttr("code", 0);
            setAttr("count", da.getTotalRow());
            setAttr("data", da.getList());
            setAttr("success", true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }

    public void DelFormData() {
        Claims claims = getAttr("claims");
        String key = getPara("key");
        String table = getPara("table");
        if (table.isEmpty()) {
            setAttr("msg", "表数据为空不能删除");
            setAttr("success", false);
        } else {
            //删除主表数据成功之后删除业务表中数据，附件在附件管理中删除
            boolean tag = Db.deleteById("commontask", "InstanceID", key);

            logService.addLog("删除业务数据", "commontask中数据删除！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "删除", "", "", "");

            if (tag) {

                boolean tag0 = Db.deleteById(table, "id", key);// SqlFromData.DelFromData(table, key);
                if (tag0) {
                    setAttr("msg", "删除成功");
                    setAttr("success", true);
                } else {
                    setAttr("msg", "删除成功,但未能彻底删除！");
                    setAttr("success", true);

                }
            }
            setAttr("msg", "删除失败！");
            setAttr("success", false);


        }
        renderJson();
    }

    public void GetFormData() {

        String wheres = getPara("where");
        Record where = new Record();

        Map h5 = JSON.parseObject(wheres.replace("[", "").replace("]", ""), Map.class);
        where.setColumns(h5);
        String table = getPara("table");

//where: [{"TableName":"A_Article","ColumnsName":"ID","ColumnType":"CB6A1DBB-29B1-44C6-922F-378D09A2629A"}]
        Record da = Db.findById(where.getStr("TableName"), where.getStr("ColumnsName"), where.getStr("ColumnType"));
        List<Record> list = new ArrayList<>();
        list.add(da);
        renderJson(list);
        //return JSONhelper.ToJson(WebOS.Dal.CommonTaskDal.Instance.GetNoMoldeWhere(table, where));
    }

    public void generateMap() {
        try{
        PropKit.use("config.properties");
        String m_localPath = PropKit.get("fromurl");
        String path = m_localPath + "/map.xml";
        File file = new File(path);
        BufferedWriter out = null;
        if (!file.exists()) {
            try {

                String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "   <!-- XML文件需以utf-8编码-->\n" +
                        "   <urlset>";
                String xmls ="";
                        List<Record> da = Db.find("SELECT *FROM a_article ORDER BY Adddate DESC LIMIT 50000");
                for (Record itme : da) {

                    try {
                        String url = "http://asxsyd92.com/news/newsdetail?id=" + itme.get("id");


                        xmls += " <url><loc>" + url + "</loc> <lastmod>" + sdf.format(itme.get("Adddate")) + "</lastmod> <changefreq>daily</changefreq><priority>0.8</priority></url>";

                    } catch (Exception ex) {
                    }
                }
                xml =xml+  xmls+"</urlset>" ;

                file.createNewFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(xml, 0, xml.length());
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        System.out.print(e.getMessage());
                        e.printStackTrace();
                    }
                }
                //map.xml
            }
        }else {

            try {

                String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "   <!-- XML文件需以utf-8编码-->\n" +
                        "   <urlset>";
                String xmls ="";
                List<Record> da = Db.findAll("a_article");
                for (Record itme : da) {

                    try {
                        String url = "http://asxsyd92.com/news/newsdetail?id=" + itme.get("id");


                        xmls += " <url><loc>" + url + "</loc> <lastmod>" + sdf.format(itme.get("Adddate")) + "</lastmod> <changefreq>daily</changefreq><priority>0.8</priority></url>";

                    } catch (Exception ex) {
                    }
                }
                xml =xml+  xmls+" </urlset>" ;

                file.delete();
                file.createNewFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(xml, 0, xml.length());
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        System.out.print(e.getMessage());
                        e.printStackTrace();
                    }
                }
                //map.xml
            }
        }
            setAttr("msg","生成成功，请在百度平台抓取结果获取访问map.xml");
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", true);
    }catch (Exception ex){
            System.out.println(ex.getMessage());
            setAttr("msg", ex.getLocalizedMessage());
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }

}
