package com.webos.controllers.webos;

import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.JosnUtils;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.mailservice.impl.MailImpl;
import com.security.Authorization;
import com.webcore.modle.Dictionary;
import com.webcore.service.CommomService;
import com.webcore.service.DictionaryService;
import com.webcore.utils.Unity;
import com.webcore.utils.data.mysqlserver.FromData;
import io.jsonwebtoken.Claims;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.ehcache.xml.model.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Map;
@Before({Authorization.class})
@Path("/api/common")
public class CommomController extends Controller {
    @Inject
    MailImpl mailUtils;
    /// <summary>
    /// 公共单表保存
    /// </summary>
    /// <param name="tab"></param>
    /// <param name="data"></param>
    /// <returns></returns>
    @Before({Authorization.class, POST.class})
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
    @Before({ POST.class})
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
            setAttr("success", true);

    }
    renderJson();
    }

    @Before({ POST.class})
    public void GetDictionary(){


        Kv kv=Kv.by("parentid", StringUtil.GuidEmpty());
        try {
            setAttr("count",10);
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("success", true);

            setAttr("data", new JosnUtils<Dictionary>().toJson(DictionaryService.GetDictionary(kv)));
        }
        catch (Exception ex){
            setAttr("msg",ex.getLocalizedMessage());
            setAttr("success", false);
            setAttr("data",ex.getLocalizedMessage());
        }

        renderJson();

    }
@Clear
    public void test(){

    try{

      java.util.  List<Record> recordList= Db.find("select * from seo ");
        for (Record item:recordList
             ) {
            try {
                System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
                WebDriver driver = new ChromeDriver();
                driver.get(item.getStr("url").replace("asxsyd92.com", "127.0.0.1"));
                driver.manage().timeouts().pageLoadTimeout(1000 * 60, java.util.concurrent.TimeUnit.SECONDS);
                String html_source = driver.getPageSource();
                System.out.print(html_source);
                Record record = Db.findById("seo", "url", item.getStr("url"));
                if (record == null) {
                    Record r = new Record();
                    r.set("id", StringUtil.getPrimaryKey());
                    r.set("url", item.getStr("url"));
                    r.set("html", html_source.replace("127.0.0.1", "asxsyd92.com"));
                    System.out.print(r.getStr("html"));
                    Db.save("seo", r);
                } else {
                    record.set("html", html_source);
                    Db.update("seo", record);
                }
                driver.close();
            }catch (Exception ex){}
        }

        setAttr("mgs","操作成功");
    }catch (Exception ex){

        setAttr("erroe",ex.getMessage());
    }
    renderJson();
    }


}
