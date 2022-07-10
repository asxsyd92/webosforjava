package com.seoservice;


import com.asxsyd92.quartz.core.QuartzPlugin;
import com.asxsydutils.utils.HttpHelper;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import com.seoservice.taskjob.Task;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import kotlin.collections.ArrayDeque;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


public class SeoServiceConfig extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        UndertowServer.create(SeoServiceConfig.class)
                .configWeb( builder -> {
                    // 配置 Filter
//                    builder.addFilter("myFilter", "com.seoservice.SeoServiceConfig");
//                    builder.addFilterUrlMapping("myFilter", "/*");
//                    builder.addFilterInitParam("myFilter", "key", "value");

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

//        QuartzPlugin quartzPlugin = new QuartzPlugin();
//        quartzPlugin.addJob(new Task().cron("0 */2 * * * ?"));// 每两秒跑一次job
//        me.add(quartzPlugin);

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

//  java      java.util.   List<Record> record = Db.find("SELECT *FROM a_article");
//        for (Record item:record ) {
//            try{
//                String url="http://asxsyd92.com/news/newsdetail?id="+item.getStr("id");
//
//                System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
//                WebDriver driver = new ChromeDriver();
//                driver.get(url);
//                driver.manage().timeouts().pageLoadTimeout(1000 * 60, java.util.concurrent.TimeUnit.SECONDS);
//                //休眠10分钟后
//                Thread.currentThread().sleep(1000*60*2);
//                String html_source = driver.getPageSource();
//                System.out.print(html_source);
//                Record r = Db.findById("seo", "url", url);
//                if (r == null) {
//                    Record rr = new Record();
//                    rr.set("id", StringUtil.getPrimaryKey());
//                    rr.set("url", url);
//                    rr.set("html", html_source);
//                    //System.out.print(r.getStr("html"));
//                    Db.save("seo", rr);
//                } else {
//                    r.set("html", html_source);
//                    Db.update("seo", r);
//                }
//                driver.close();
//            }catch (Exception ex){
//                System.out.print("获取编译后资源错误\n");
//                System.out.print(ex.getMessage());
//            }
//        }
        execute();


    }
    int i=2358277;
    private  List<String> imtlist=new ArrayDeque<>();
    public void execute()  {
        try {
            System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            //休眠10分钟后
            //driver.findElement("d",);
            //for (int a=220001 ;a>520001;a++) {
            i++;
            driver.get("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=乳房&step_word=&hs=0&pn=0&spn=0&di=7060663421280190465&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=4223393491%2C3492650966&os=3681659939%2C1803564562&simid=4223393491%2C3492650966&adpicid=0&lpn=0&ln=1803&fr=&fmq=1648478185205_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fpics6.baidu.com%2Ffeed%2F1b4c510fd9f9d72a9ffa53a0c140ed32359bbb66.jpeg%3Ftoken%3Da441e991b18c90f91fe4d410708532a0&fromurl=ippr_z2C%24qAzdH3FAzdH3Fkwt3twiw5_z%26e3Bkwt17_z%26e3Bv54AzdH3Ff%3Ft1%3D8mmbcbaaadllmnd8b00%26ou6%3Dfrt1j6%26u56%3Drv&gsm=1&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined&dyTabStr=MCwzLDQsMiwxLDUsOCw3LDYsOQ%3D%3D");
            System.out.println(driver.getTitle());//得到网页标题
            //Thread.sleep(1000*10);
//下载当前图片
            //  imgView
            //getfile(driver);
            // }
            getfile(driver);
            for (int  a=0;a<8000;a++) {
                System.out.print(a);
                Thread.sleep(1000*10);
                driver.findElement(By.className("img-next")).click();//模拟点击搜索文本框动作
                getfile(driver);
            }


            //driver.findElement(By.id("kw")).sendKeys("asxsyd92.com");//模拟键盘输入要搜索的内容

            //driver.findElement(By.id("su")).click();//模拟点击搜索按钮

            // Thread.sleep(1000*10);
           // add(driver);
//
//            for (String a : imtlist) {
//                driver.get(a);
//
//                Thread.sleep(1000*10);
//                //添加新记录
//                //add(driver);
//                getfile(driver);
//                imtlist.remove(a);
//            }

            // Thread.sleep(1000*10);
            driver.quit();//退出

        }catch (Exception ex){
            System.out.print("获取编译后资源错误\n");
            System.out.print(ex.getMessage());
        }
    }
 void  add(WebDriver driver){
//     for (int a =0;a< 9000;a++) {
//         i++;
//     imtlist.add("https://www.tooopen.com/view/"+i+".html");
//     }

     List<WebElement> aList = driver.findElements(By.tagName("a"));
     try {

         for (WebElement a : aList) {
             String urlStr = a.getAttribute("src");
             System.out.print(urlStr);
             // a.click();
             //driver.findElement(By.id("pic")).click();

             imtlist.add(urlStr);

//                    System.out.println(a.getAttribute("src"));//获取a标签中的URL
//
//                    //获取a标签href属性值
//                    String urlStr = a.getAttribute("src");
//                    System.out.print(urlStr);
//
//                    String[] strs = urlStr.split("/");
//                    String  path= strs[strs.length - 1];
//                   HttpHelper. downloadFile(urlStr,"","d:/web/"+path);

         }


     } catch (Exception e) {
         e.printStackTrace();
     }
 }

    public  void getfile(WebDriver driver){
        try{
            WebElement d=  driver.findElement(By.id("currentImg"));
            String K= d.getAttribute("src");
            String[] strs = K.split("/");
            String  path= strs[strs.length - 1];
            System.out.print(K);
            HttpHelper. downloadFile(K,"","d:/web/"+ StringUtil.getPrimaryKey().replace("-","")+".png");
        }catch (Exception ex){}


    }
}
