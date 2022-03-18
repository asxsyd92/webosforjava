package com.vueseo;


import com.asxsydutils.utils.StringUtil;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;


public class SeoConfig extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        UndertowServer.create(SeoConfig.class)
                .configWeb( builder -> {
                    // 配置 Filter
                    builder.addFilter("myFilter", "com.vueseo.SeoFilter");
                    builder.addFilterUrlMapping("myFilter", "/*");
                    builder.addFilterInitParam("myFilter", "key", "value");

                    // 配置 Servlet
//                    builder.addServlet("myServlet", "com.abc.MyServlet");
//                    builder.addServletMapping("myServlet", "*.do");
//                    builder.addServletInitParam("myServlet", "key", "value");
//
//                    // 配置 Listener
//                    builder.addListener("com.abc.MyListener");
//
//                    // 配置 WebSocket，MyWebSocket 需使用 ServerEndpoint 注解
//                    builder.addWebSocketEndpoint("com.abc.MyWebSocket");
                })
                .start();
        //UndertowServer.start(WebosConfig.class, 88, true);
    }

    public void configConstant(Constants me) {
        //在调用getPropertyToBoolean之前需要先调用loadPropertyFile
        //loadPropertyFile("config.properties");
        //设置jfinal的开发模式
       // me.setDevMode(getPropertyToBoolean("devMode",true));


      me.setDevMode(true);
        //开启支持注解，支持 Controller、Interceptor 之中使用 @Inject 注入业务层，并且自动实现 AOP
        me.setInjectDependency(true);
        //me.setError404View("/error.html");
        //me.setError500View("/error.html");
     //   me.setJsonFactory(new FastJsonFactory());
    }

    public void configRoute(Routes me) {

      //  loadPropertyFile("config.properties");

  }

    public void configEngine(Engine me) {
    //  me.setBaseTemplatePath("webapp");
        //me.setToClassPathSourceFactory();
    }
    public void configPlugin(Plugins me) {
        loadPropertyFile("config.properties");
        //获取jdbc连接池
        String url=getProperty("jdbcUrl");
        String user=getProperty("user");
        String pw=getProperty("password");
        String driver=getProperty("driver");
        DruidPlugin druidPlugin = new DruidPlugin(url,user, pw,driver);
        me.add(druidPlugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        arp.setShowSql(true);

        arp.setShowSql(getPropertyToBoolean("showSql",false));



        me.add(arp);

    }
    public void configInterceptor(Interceptors me)
    {


    }

    public void configHandler(Handlers me) {

        //me.add(new ContextPathHandler("basePath"));
    }

    public void onStart() {
        String fozuStr="IC4uLi4uLi4uLi4uLi4uLi4uLi4uLi53ZWJvcy4uLi4uLi4uLi4uLi4uLi4uLi4uLi4KICAgICAgICAgICAgICAgICAgICAgICBfb28wb29fICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgb293ZWJvc29vICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICA4OCIgLiAiODggICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICh8IC1fLSB8KSAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgMFwgID0gIC8wICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgX19fL+KAmC0tLeKAmVxfX18gICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgIC4nIFx8ICAgICAgIHwvICcuICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAvIFxcfHx8ICA6ICB8fHwvLyBcICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgLyBffHx8fHwgLeWNjS18fHx8fF8gXCAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICB8ICAgfCBcXFwgIC0gIC8vLyB8ICAgfCAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgIHwgXF98ICAnJ1wtLS0vJycgIHxfLyB8ICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgXCAgLi1cX18gICctJyAgX19fLy0uIC8gICAgICAgICAgICAgIAogICAgICAgICAgICAgX19fJy4gLicgIC8tLS4tLVwgICcuIC4nX19fICAgICAgICAgICAgCiAgICAgICAgICAuIiIg4oCYPCAg4oCYLl9fX1xfPHw+Xy9fX18u4oCZID7igJkgIiIuICAgICAgICAgIAogICAgICAgICB8IHwgOiAg4oCYLSBc4oCYLjvigJhcIF8gL+KAmTsu4oCZLyAtIOKAmSA6IHwgfCAgICAgICAgCiAgICAgICAgIFwgIFwg4oCYXy4gICBcXyBfX1wgL19fIF8vICAgLi3igJkgLyAgLyAgICAgICAgCiAgICAgPT09PT3igJgtLl9fX1/igJguX19fIFxfX19fXy9fX18uLeKAmV9fXy4t4oCZPT09PT0gICAgIAogICAgICAgICAgICAgICAgICAgICAgIOKAmD0tLS094oCZICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLnYxLjUuLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4=";
        byte[] decode = Base64.decode(String.valueOf(fozuStr.toCharArray()));
        System.out.print("\n"+new String(decode));
        System.out.print("\n");
//     java.util.   List<Record> record = Db.find("SELECT *FROM a_article");
//        for (Record item:record ) {
//            try{
//            String url="http://asxsyd92.com/news/newsdetail?id="+item.getStr("id");
//
//            System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
//            WebDriver driver = new ChromeDriver();
//            driver.get(url);
//            driver.manage().timeouts().pageLoadTimeout(1000 * 60, java.util.concurrent.TimeUnit.SECONDS);
//            //休眠10分钟后
//            Thread.currentThread().sleep(1000*60*2);
//            String html_source = driver.getPageSource();
//            System.out.print(html_source);
//            Record r = Db.findById("seo", "url", url);
//            if (r == null) {
//                Record rr = new Record();
//                rr.set("id", StringUtil.getPrimaryKey());
//                rr.set("url", url);
//                rr.set("html", html_source);
//                //System.out.print(r.getStr("html"));
//                Db.save("seo", rr);
//            } else {
//                r.set("html", html_source);
//                Db.update("seo", r);
//            }
//            driver.close();
//        }catch (Exception ex){
//            System.out.print("获取编译后资源错误\n");
//            System.out.print(ex.getMessage());
//        }
//        }
    }


}
