package com.webos.controllers.webos;


import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.webcore.service.DictionaryService;
import com.webcore.service.LogService;
import com.webcore.service.TaskService;
import com.webcore.modle.CommonTask;
import com.webcore.modle.Dictionary;

import com.webcore.utils.data.MySql;
import com.webcore.utils.data.mysqlserver.FromData;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jwt.JwtInterceptor;
import com.webos.Common;
import io.jsonwebtoken.Claims;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Before({JwtInterceptor.class, POST.class})
@Path("/api/form")
public class FormControllers extends Controller {
    @Inject
    LogService logService;
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
public  void getFormJson(){
         try{
             String key = getPara("key");
             Record fmdata= Db.findById("sysformdesign","id",key);
             if (fmdata!=null){
                 setAttr("success", true);
                 setAttr("data", fmdata);
                 setAttr("msg", "表单不存在");
             }else {
                 setAttr("success", false);
                 setAttr("msg", "表单不存在");
             }
         }catch (Exception ex){
             setAttr("success", false);
             setAttr("msg", "表单查询异常");
         }
    renderJson();
}
public  void  saveFormJson(){
    try{
        String key = getPara("key");
        String data = getPara("data");
        String title = getPara("title");
        String tab=getPara("tab");
        if (key==null){

            Record os=new Record();
            os.set("datetime",new java.util.Date());
            os.set("designhtml",data);
            os.set("id",StringUtil.GuidEmpty());
            os.set("tab",tab);
            os.set("title",title);
            String fromid="";
            try {
                fromid= FromData.save(os,"sysformdesign");
                setAttr("success", true);
                setAttr("data", os);
                setAttr("msg", "保存成功");
            }catch (Exception ex){
                setAttr("success", false);

                setAttr("msg", ex.getMessage());
            }
            Record rps = new Record();
            rps.set("id", StringUtil.getPrimaryKey());
            rps.set("title", title);
            rps.set("params", fromid);
            rps.set("tag", "showfrom");
            rps.set("ParentID", "1ca18548-e361-4c42-bfa1-f086f14e0669");
            try {
                FromData.save(rps, "roleapp");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }else {
            Record fmdata= Db.findById("sysformdesign","id",key);
            fmdata.set("designhtml",data);
            fmdata.set("title",title);
            fmdata.set("tab",tab);
            try {
                FromData.save(fmdata,"sysformdesign");
                setAttr("success", true);
                setAttr("data", fmdata);
                setAttr("msg", "保存成功");
            }catch (Exception ex){
                setAttr("success", false);

                setAttr("msg", ex.getMessage());
            }

        }


    }catch (Exception ex){
        setAttr("success", false);
        setAttr("msg", "表单保存异常");
    }
    renderJson();
}
    public  void FormDesignYL()
    {
        try {
            PropKit.use("config.properties");
            String html = getPara("html");
            String temp= PropKit.get("fromurl");
                   // PropKit.get("fromurl");

            String path = temp + "/webos/page/from/FDsubmit.html";

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
        PropKit.use("config.properties");
       // loadPropertyFile("config.properties");
        String html = getPara("html");
        String name = getPara("name");
        String title = getPara("title");
        Boolean type = getParaToBoolean("type");
        String data = getPara("data");
        String flow = getPara("flow");

        name = name.toLowerCase();
        //Record r = name;
        if (type) { name = "Debug/" + name; } else { name = "Release/" + name; }
        String tl = FormDesign(html);

        String m_localPath=PropKit.get("fromurl");
        String path = m_localPath + "/webos/page/from/" + name + ".html";

        // 判断文件是否存在，不存在则创建，否则读取值显示到窗体
        Claims claims = getAttr("claims");
        //data= Unity.getJsonSetData(data,claims);
        data= data.replace("_sys_url", "/webos/page/from/" + name + ".html");


        Record fmdata= Db.findById("sysformdesign","url","/webos/page/from/" + name + ".html");

     //   String id= FromData.save(os,"sysformdesign");
        //var fmdata= SqlFromData.GetFromData("SysFormDesign",new { Url = "/webos/page/from/" + name + ".html" }).FirstOrDefault();
        String ulr = "";
        if (flow.equals(StringUtil.GuidEmpty())) {
            ulr = "flowid=" + flow+"&istask=true";
        }
        if (fmdata != null)
        {
            Record os=new Record();

            Map h5 = JSON.parseObject(data, Map.class);
            os.setColumns(h5);
          //  data = Models.Unity.OperationJson(data,User);
          //  var os = Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<object, object>>(data);
          //  var obj = fmdata as Dictionary<Object, Object>;
          //  os["ID"] = obj["ID"];
          //  SqlFromData.SaveFromData("SysFormDesign", os.ToJson());
            try {
                FromData.save(os,"sysformdesign");
            }catch (Exception ex){
                System.out.println(ex);
            }


            Record rp= Db.findById("roleapp","tag","/page/from/" + name + ".html" + ulr);
        if (rp!=null) {
            Record rps = new Record();
            rps.set("id", rp.get("id"));
            rps.set("title", title);
            rps.set("tag", "/page/from/" + name + ".html" + ulr);
            rps.set("ParentID", "1ca18548-e361-4c42-bfa1-f086f14e0669");
            try {
                FromData.save(rps, "roleapp");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }else
        {
            Record rps=new Record();
            rps.set("id",StringUtil.getPrimaryKey());
            rps.set("title",title);
            rps.set("tag","/page/from/" + name + ".html" + ulr);
            rps.set("ParentID","1ca18548-e361-4c42-bfa1-f086f14e0669");
            try {
                FromData.save(rps,"roleapp");
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        }
        else {
            String keyid =StringUtil.GuidEmpty();
            data =data.replace("_sys_id", keyid);

            Record os=new Record();
            Map h5 = JSON.parseObject(data, Map.class);
            os.setColumns(h5);
            os.set("id",keyid);
            try {
                String id= FromData.save(os,"sysformdesign");
            }catch (Exception ex){
                System.out.println(ex);
            }

            Record rps=new Record();
            rps.set("id",StringUtil.getPrimaryKey());
            rps.set("title",title);
            rps.set("tag","/page/from/" + name + ".html" + ulr);
            rps.set("ParentID","1ca18548-e361-4c42-bfa1-f086f14e0669");
            try {
                FromData.save(rps,"roleapp");
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }

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
       setAttr("url","/webos/page/from/" + name + ".html?istask="+true);
        logService.addLog("发布表单", "发布表单！", Common.getRemoteLoginUserIp(this.getRequest()), claims.get("name").toString(), claims.get("id").toString(), "发布", "", "", "");

        renderJson();
    }

    @NotAction
    public    String FormDesign(String html)
    {
        try {
            PropKit.use("config.properties");
            String temp=PropKit.get("fromurl");

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

    }

    public  void  FormTable(){
        String id = getPara("id");
        String table = getPara("table");
        String title = getPara("title");
        if (id != null && id != "") {
           Record da= Db.findById("sysformdesign",id);
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
        Record ta= Db.findById("sysformdesign","Tab",table);
        if (ta != null)
        {
            setAttr("code", 0);
            setAttr("msg", "获取成功");
            setAttr("data", ta);
        }
        else {
            List<Record> list=  MySql.getMySqlableStructure(table);
            setAttr("code", 0);
            setAttr("msg", "-1");
            setAttr("data", list);
        //    var list = SqlFromData.GetTableStructure(table);
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
       // Unity.getJsonSetData(data,claims);
        boolean istask= getParaToBoolean("istask");;
        String tag = "";
        Record os=new Record();

        Map user = JSON.parseObject(data, Map.class);

        os.setColumns(user);

        if (os.getStr("ID").equals(StringUtil.GuidEmpty()))
        {
            os.set("Adddate",StringUtil.getDatatime());

        }
      Object cls=  os.get("Classid");
        //var cls=  os.FirstOrDefault(c => c.Key.ToString() == "");
        String id=StringUtil.GuidEmpty();
        try {
            id= FromData.save(os,table);
        }catch (Exception ex){
            setAttr("msg", ex.getMessage());
            setAttr("Success", false);
            renderJson();
        }
        if (id.equals(StringUtil.GuidEmpty()) )
        {   setAttr("msg", "添加失败！");
            setAttr("Success", false);
            renderJson();
        }else {
            //是否存导公共task表中统一管理
            if (istask) {

                CommonTask DAS = TaskService.Get(id);
                Object _user =  claims.get("id");
                Object username =claims.get("name");
                if (DAS == null) {
                    CommonTask task = new CommonTask();
                    task.setID(StringUtil.getPrimaryKey());
                    task.setInstanceID(id);
                    task.setTTable(table);
                    task.setSenderName(username.toString());
                    task.setSenderID(_user.toString()) ;
                    task.setAddTime(new Date());
                    if (cls!=null){
                        task.setClassid(cls.toString());
                    }else {
                        task.setClassid(StringUtil.GuidEmpty());
                    }

                    try {
                        if (os.get("title")!=null){
                            task.setTitle(os.get("title"));
                        }

                    } catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                        task.setTitle(os.get("标题不错在"));

                    }
                    try {
                        tag= FromData.save(task.toRecord(),"commontask");
                    }catch (Exception ex){
                        setAttr("msg", ex.getMessage());
                        setAttr("Success", false);
                        renderJson();
                    }


                } else {
                    try {
                        if (os.get("title")!=null){
                            DAS.setTitle(os.get("title"));
                        }

                    } catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                        DAS.setTitle(os.get("标题不错在"));

                    }
                    try {
                        tag= FromData.save(DAS.toRecord(),"commontask");
                    }catch (Exception ex){
                        setAttr("msg", ex.getMessage());
                        setAttr("Success", false);
                        renderJson();
                    }

                }
                if (!tag.equals(StringUtil.GuidEmpty())) {
                    setAttr("Success", true);
                    setAttr("msg", "成功");
                    renderJson();
                } else {
                    setAttr("Success", false);
                    setAttr("msg", "失败");
                    renderJson();
                };
            }
else {
                setAttr("Success", true);
                setAttr("msg", "成功");
            }

        }
        renderJson();
    }


    @Before({JwtInterceptor.class, GET.class})
    public void  GetCheckName() {
        PropKit.use("config.properties");
        String Name = getPara("Name");
        String m_localPath=PropKit.get("fromurl");

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
    @Clear
    public void GetDictionaryByID() throws Exception {
        String id = getPara("id");
        Object da=   new JosnUtils<Dictionary>().toJson(DictionaryService.GetDictionaryByID(id));
        setAttr("data", da);
        renderJson();
    }
    @Before({JwtInterceptor.class, POST.class})
    public  void  FormDel(){
        PropKit.use("config.properties");
        String key = getPara("key");
        String url = getPara("url");
        boolean tag0 = Db.deleteById("SysFormDesign","ID",key);
        Db.deleteById("RoleApp","ID",key);

        if (tag0)
        {

            String temp =   PropKit.get("fromurl");
            String m_localPath = temp + url;
            String path = m_localPath;

            File file = new File(path);

            try {
                if (!file.exists()){}else {
                    file.delete();
                }

            } catch (Exception ex) {
                setAttr("msg", "删除成功！但是文件处理失败！"+ex.getMessage());
        }
            setAttr("msg", "删除成功！");
            setAttr("success", true);

        }else {
            setAttr("msg", "删除失败！");
            setAttr("success", false);
        }

     renderJson();
    }
}

