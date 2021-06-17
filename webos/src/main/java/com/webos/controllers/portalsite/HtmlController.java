package com.webos.controllers.portalsite;


import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.webcore.utils.data.mysqlserver.FromData;

import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
@Clear
@Path("News")
public class HtmlController extends Controller {
  public void   NewsDetail(){
      String id=getPara("id");

     String html= StringHTML("a_article", id);
      renderHtml(html);
  }
  @NotAction
    public String StringHTML(String table,String id)
    {
       Record da= Db.findById(table,"ID",id);
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
 String html = "";


            html = WriteFiles(da, Nextarticle, Lastarticle );


        return html;
    }
    public static String WriteFiles(Record torrent, Record Nextarticle, Record Lastarticle)
    {

        PropKit.use("config.properties");
        String temp= PropKit.get("fromurl");
        String path = temp +"/portalsite/newsemplate.html";
        //File file = new File(path);
       String str= read(path);


        // 替换内容
        // 这时,模板文件已经读入到名称为str的变量中了
        if (torrent == null)
        {

        }
        else
        {
            //getrandnews
          List<Record> sj= Db.find("select  *from a_article order by rand() desc LIMIT 50");
//            var sj = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.GetPage("A_Article", 1, 10, " NEWID()");
           if (sj != null)
            {
                String htmls = "";

                for (Record item : sj)
                {
                        htmls += "<li ><span class='layui-badge'>荐</span><a href='/News/NewsDetail?id=" + item.get("id") + "'>" + item.getStr("title") + "</a> </li>";
                }
                str = str.replace("$sj$", htmls);
            }
            else { str = str.replace("$sj$", ""); }
            str = str.replace("$Title$", torrent.getStr("title") );
            str = str.replace("$ID$", torrent.getStr("id"));
            str = str.replace("$Description$",torrent.getStr("description")); //模板页中的ShowArticle
            str = str.replace("$Adddate$", torrent.getStr("adddate"));
            str = str.replace("$views$", torrent.getStr("views"));
            str = str.replace("$Contents$", torrent.getStr("contents"));
            str = str.replace("$Author$", torrent.getStr("author"));
            str = str.replace("$Nextarticle.ID$", Nextarticle.getStr("id"));
            str = str.replace("$Lastarticle.ID$",  Lastarticle.getStr("id"));
            str = str.replace("$Lastarticle.Title$",  Lastarticle.getStr("title"));
            str = str.replace("$Nextarticle.Title$",  Nextarticle.getStr("title"));
            str = str.replace("$Nextarticle.views$",  Nextarticle.getStr("views"));
            str = str.replace("$Lastarticle.views$",  Lastarticle.getStr("views"));
        }


            return str;

    }

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
}
