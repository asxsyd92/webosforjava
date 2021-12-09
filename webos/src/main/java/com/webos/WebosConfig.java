package com.webos;


import com.asxsyd92.swagger.config.routes.SwaggerRoutes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.webcore.config.TableConfig;
import com.webos.controllers.UeditorController;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.webos.jwt.JwtInterceptor;
import com.webos.controllers.websocket.WebSocket;


public class WebosConfig extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        UndertowServer.create(WebosConfig.class)
                .configWeb(builder -> {
                    // 配置WebSocket需使用ServerEndpoint注解
                    builder.addWebSocketEndpoint(WebSocket.class);
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
        me.setError404View("/error.html");
        me.setError500View("/error.html");
     //   me.setJsonFactory(new FastJsonFactory());
    }

    public void configRoute(Routes me) {

       me.add("/api/ueditor", UeditorController.class);
       // me.add("/api/Users", UsersControllers.class);
        me.add(new SwaggerRoutes());
        me.scan("com.webos.controllers");
    }

    public void configEngine(Engine me) {
    //  me.setBaseTemplatePath("webapp");
        //me.setToClassPathSourceFactory();
    }
    public void configPlugin(Plugins me) {
   loadPropertyFile("config.properties");
      //  me.add(new JwtTokenPlugin(UserService.me));                                /**配置权限拦截插件*/
        int initialSize = getPropertyToInt("initialSize");
        int minIdle = getPropertyToInt("minIdle");
        int maxActive = getPropertyToInt("maxActive");
        //获取jdbc连接池
        String url=getProperty("jdbcUrl");
        String user=getProperty("user");
        String pw=getProperty("password");
        String driver=getProperty("driver");
        DruidPlugin druidPlugin = new DruidPlugin(url,user, pw,driver);


        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
       arp. setContainerFactory(new CaseInsensitiveContainerFactory(true));
       // arp.getEngine().setToClassPathSourceFactory();
        arp.setShowSql(true);
        arp.addSqlTemplate("/sql/webos.sql");
        arp.addSqlTemplate("/sql/oa.sql");
       // arp.setDialect(new SqlServerDialect());
       // arp.addMapping("RoleApp", RoleAppDal.class);
       // arp.setShowSql(getPropertyToBoolean("showSql",true));
        TableConfig.mapping("com.webcore.modle",arp);
        me.add(new EhCachePlugin());
        me.add(arp);
        System.out.println(arp.getConfig());
//        me.add(new SwaggerPlugin(new SwaggerDoc().setBasePath("/").setHost("127.0.0.1:99").setSwagger("2.0")
//                .setInfo(new SwaggerApiInfo("jfinal swagger demo", "1.0", "jfinal swagger", ""))));


 

    }
    public void configInterceptor(Interceptors me)
    {
        me.add(new JwtInterceptor()); // 权限拦截器
    }

    public void configHandler(Handlers me) {
        me.add(new UrlSkipHandler("^/websocket.ws", false));
        me.add(new ContextPathHandler("basePath"));

    }
    public void onStart() {
        System.out.println("系统启动完成后回调");
    }


}
