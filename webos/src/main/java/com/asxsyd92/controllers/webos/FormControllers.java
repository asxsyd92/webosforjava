package com.asxsyd92.controllers.webos;


import com.alibaba.fastjson.JSON;
import com.asxsyd92.bll.DictionaryBll;
import com.asxsyd92.utils.Unity;
import com.asxsyd92.annotation.Route;
import com.asxsyd92.utils.StringUtil;
import com.asxsyd92.utils.data.MySql;
import com.asxsyd92.utils.data.mysqlserver.FromData;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtInterceptor;
import io.jsonwebtoken.Claims;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Route(Key = "/api/Form")
public class FormControllers extends Controller {

     @NotAction
    public static String read(String path) {
        StringBuffer res = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(path), StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                res.append(line + "\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    @Before({JwtInterceptor.class, POST.class})
    public  void FormDesignYL()
    {
        try {
            String html = getPara("html");
            ServletContext s1 = this.getRequest().getServletContext();
            String temp = s1.getRealPath("/"); //(关键)
            String m_localPath = temp + "/webos/page/from/FDsubmit.html";
            String path = m_localPath;

            File file = new File(path);
            if (!file.exists()) {
                // file.createNewFile();
            } else {

                String str = read(path);
                // 替换内容
                // 这时,模板文件已经读入到名称为str的变量中了
                str = str.replace("$FDsubmit$", html);
                // 写文件
                try {
                    setAttr("str", str);

                } catch (Exception ex) {
                    setAttr("str", ex.getMessage());

                } finally {
                    // sw.Close();
                }

            }

        }catch (Exception ex){
            setAttr("str", ex.getMessage());
        }
        renderJson();
    }

    @Before({JwtInterceptor.class, POST.class})

    public void FormDesignHtml()
    {
        String html = getPara("html");
        String name = getPara("name");
        String title = getPara("title");
        Boolean type = getParaToBoolean("type");
        String data = getPara("data");
        String flow = getPara("flow");

        name = name.toLowerCase();
        var r = name;
        if (type) { name = "Debug/" + name; } else { name = "Release/" + name; }
        var tl = FormDesign(html);
        ServletContext s1 = getRequest().getServletContext();
        String m_localPath = s1.getRealPath("/"); //(关键)
     //   String m_localPath = AppDomain.CurrentDomain.BaseDirectory;
        String path = m_localPath + "/webos/page/from/" + name + ".html";

        // 判断文件是否存在，不存在则创建，否则读取值显示到窗体
        Claims claims = getAttr("claims");
        data= Unity.getJsonSetData(data,claims);
        data= data.replace("_sys_url", "/webos/page/from/" + name + ".html");


        Record fmdata= Db.findById("sysformdesign","url","/webos/page/from/" + name + ".html");

     //   String id= FromData.save(os,"sysformdesign");
        //var fmdata= SqlFromData.GetFromData("SysFormDesign",new { Url = "/webos/page/from/" + name + ".html" }).FirstOrDefault();
        String ulr = "";
        if (flow.equals(StringUtil.GuidEmpty())) {
            ulr = "?flowid=" + flow;
        }
        if (fmdata != null)
        {
            Record os=new Record();

            Map h5 = JSON.parseObject(data, Map.class);
            os.set("id",fmdata.getStr("id"));
          //  data = Models.Unity.OperationJson(data,User);
          //  var os = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<object, object>>(data);
          //  var obj = fmdata as Dictionary<Object, Object>;
          //  os["ID"] = obj["ID"];
          //  SqlFromData.SaveFromData("SysFormDesign", os.ToJson());
            String id= FromData.save(os,"sysformdesign");

            Record rp= Db.findById("roleapp","tag","/page/from/" + name + ".html" + ulr);
        if (rp!=null){
            Record rps=new Record();
            rps.set("id",StringUtil.getPrimaryKey());
            rps.set("title",title);
            rps.set("tag","/page/from/" + name + ".html" + ulr);
            rps.set("ParentID","1ca18548-e361-4c42-bfa1-f086f14e0669");
            FromData.save(os,"roleapp");
            }

        }
        else {
            String keyid =StringUtil.GuidEmpty();
            data =data.replace("_sys_id", keyid);

            Record os=new Record();
            Map h5 = JSON.parseObject(data, Map.class);
            os.setColumns(h5);
            os.set("id",keyid);
            String id= FromData.save(os,"sysformdesign");
            Record rps=new Record();
            rps.set("id",StringUtil.getPrimaryKey());
            rps.set("title",title);
            rps.set("tag","/page/from/" + name + ".html" + ulr);
            rps.set("ParentID","1ca18548-e361-4c42-bfa1-f086f14e0669");
            FromData.save(os,"roleapp");
          //  SqlFromData.SaveFromData("RoleApp", new { ID = keyid, Title= title, ParentID ="1ca18548-e361-4c42-bfa1-f086f14e0669".ToGuid(), tag= "/page/from/" + name + ".html"+ ulr }.ToJson());
        }
        File file = new File(path);
        BufferedWriter out = null;
        if (!file.exists())
        {
            try {

                file.createNewFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(tl,0,tl.length());
            }catch (Exception ex){
                System.out.print(ex.getMessage());
            }  finally {
        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        }

    }
        }

        else
        {
            try {
                file.delete();
                file.createNewFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(tl,0,tl.length());

            }catch (Exception ex){
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

            }

        }

        //renderJson("/webos/page/from/" + name + ".html");
       setAttr("url","/webos/page/from/" + name + ".html");
        renderJson();
    }

    @NotAction
    public    String FormDesign(String html)
    {
        try {

//            ServletContext s1 = this.getRequest().getServletContext();
////            String temp = s1.getRealPath("/"); //(关键)
            ServletContext s1 = getRequest().getServletContext();
            String temp = s1.getRealPath("/"); //(关键)
            String m_localPath = temp + "/webos/page/from/FDsubmit.html";
            String path = m_localPath;

            File file = new File(path);
            if (!file.exists()) {
                // file.createNewFile();
            } else {

                String str = read(path);
                // 替换内容
                // 这时,模板文件已经读入到名称为str的变量中了
                str = str.replace("$FDsubmit$", html);
                // 写文件
                try {
            return str;

                } catch (Exception ex) {
                    return  ex.getMessage();


                } finally {
                    // sw.Close();
                }

            }

        }catch (Exception ex){
            return  ex.getMessage();
        }

            return  "";
//        string m_localPath = AppDomain.CurrentDomain.BaseDirectory;
//        string path = m_localPath + "wwwroot/webos/page/from/FDsubmit.html";
//        Encoding code = Encoding.GetEncoding("utf-8");
//        // 读取模板文件
//        // string temp = HttpContext.Current.Server.MapPath("Text.htm");
//        StreamReader sr = null;
//        string str = "";
//        try
//        {
//            sr = new StreamReader(path, code);
//            str = sr.ReadToEnd(); // 读取文件
//            sr.Close();
//        }
//        catch (Exception exp)
//        {
//            //sr.Close();
//            return exp.Message; ;
//
//        }
//
//        // 替换内容
//        // 这时,模板文件已经读入到名称为str的变量中了
//        str = str.Replace("$FDsubmit$", html);
//        // 写文件
//        try
//        {
//
//            return str;
//        }
//        catch (Exception ex)
//        {
//            return ex.Message;
//        }
//        finally
//        {
//            // sw.Close();
     //  }
    }

    public  void  FormTable(){
        String id = getPara("id");
        String table = getPara("table");
        String title = getPara("title");
        if (id != null && id != "") {
           Record da= Db.findById("SysFormDesign",id);
           if(da!=null){
               setAttr("code", 0);
               setAttr("msg", "获取成功");
               setAttr("data", da);
           }else{
               List<Record> list=  MySql.getMySqlableStructure(table);
               setAttr("code", 0);
               setAttr("msg", "-1");
               setAttr("data", list);
               //var list = SqlFromData.GetTableStructure(table);
               //return JSONhelper.ToJson(new { code = 0, msg = "-1", data = list });
           }

    }else {
        //第一步查询SysFormDesign是否已经存在已有表单
        Record ta= Db.findById("SysFormDesign","Tab",table);
        if (ta != null)
        {
            setAttr("code", 0);
            setAttr("msg", "获取成功");
            setAttr("data", ta);
        }
        else {
            // var list = SqlFromData.GetTableStructure(table);
            //return JSONhelper.ToJson(new { code = 0, msg = "-1", data = list });
        }
        }

    renderJson();
    }
    @Before({JwtInterceptor.class, POST.class})
    public void  FormCommonTaskSave(){
        String table= getPara("table");
        String data= getPara("data");

        Claims claims = getAttr("claims");
        Unity.getJsonSetData(data,claims);
      //  boolean istask= getParaToBoolean("istask");;
        int tag = 0;
        Record os=new Record();

        Map user = JSON.parseObject(data, Map.class);

        os.setColumns(user);

        if (os.getStr("ID").equals(StringUtil.GuidEmpty()))
        {
            os.set("Adddate",StringUtil.getDatatime());

        }
      Object cls=  os.get("Classid");
        //var cls=  os.FirstOrDefault(c => c.Key.ToString() == "");
       String id= FromData.save(os,table);
//        var id = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.SaveFromData(table, data);

        if (id.equals(StringUtil.GuidEmpty()) )
        {   setAttr("msg", "添加失败！");
            setAttr("Success", false);
            renderJson();
        }else {
            setAttr("Success", true);
            setAttr("msg", "添加成功");
            renderJson();


//            if (istask) {
//
//                var DAS = CommonTaskDal.Instance.Get(new {
//                    InstanceID = id
//                });
//                var jsonData = LitJSONCore.JsonMapper.ToObject(data);
//                var user = User.Claims.FirstOrDefault(c = > c.Type == JwtClaimTypes.Id).Value;
//                var username = User.Claims.FirstOrDefault(c = > c.Type == JwtClaimTypes.Name).Value;
//                if (DAS == null) {
//                    WebOS.Modle.CommonTask task = new WebOS.Modle.CommonTask();
//                    task.ID = Guid.NewGuid();
//                    task.InstanceID = id;
//                    task.t_Table = table;
//                    task.SenderName = username;
//                    task.SenderID = user.ToGuid();
//                    task.AddTime = System.DateTime.Now;
//                    task.Classid = cls.Key == null ? Guid.Empty : cls.Value.ToString().ToGuid();
//                    try {
//                        task.Title = jsonData["Title"].ToString();
//                    } catch
//                    {
//                        task.Title = "标题不错在！";
//                    }
//                    tag = CommonTaskDal.Instance.Insert(task);
//
//                } else {
//                    try {
//                        DAS.Title = jsonData["Title"].ToString();
//                    } catch
//                    {
//                        DAS.Title = "标题不错在！";
//                    }
//                    tag = CommonTaskDal.Instance.Update(DAS);
//
//                }
//                if (tag > 0) {
//                    return JSONhelper.ToJson(new {
//                        msg = "添加成功！", Success = true
//                    });
//                } else return JSONhelper.ToJson(new {
//                    msg = "添加失败！", Success = false
//                });
//            }


        }
        renderJson();
    }


    @Before({JwtInterceptor.class, GET.class})
    public void  GetCheckName() {
        String Name = getPara("Name");
        ServletContext s1 = this.getRequest().getServletContext();
        String m_localPath = s1.getRealPath("/"); //(关键)
        //   string m_localPath = AppDomain.CurrentDomain.BaseDirectory;
        String path = m_localPath + "/webos/page/from/Debug/" + Name.toLowerCase() + ".html";
        File file = new File(path);
        if (!file.exists()) {
            setAttr("Success", true);
            setAttr("msg", "路径可用");
        } else {
            setAttr("Success", false);
            setAttr("msg", "路径不可用");


        }
        renderJson();
    }
    public void GetDictionaryByID( )
    {
        String id = getPara("id");
        setAttr("data", DictionaryBll.Instance().GetDictionaryByID(id));
        renderJson();
    }
}

