package com.asxsyd92;

import com.asxsyd92.Controllers.HomeController;
import com.asxsyd92.Controllers.NewController;
import com.asxsyd92.Controllers.UeditorController;
import com.asxsyd92.service.UserService;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import com.jwtTokenPlugin.interceptor.AuthInterceptor;
import com.jwtTokenPlugin.plugin.JwtTokenPlugin;


public class WebosConfig extends JFinalConfig {

    /**
     * 注意：用于启动的 main 方法可以在任意 java 类中创建，在此仅为方便演示
     *      才将 main 方法放在了 DemoConfig 中
     *
     *      开发项目时，建议新建一个 App.java 或者 Start.java 这样的专用
     *      启动入口类放置用于启动的 main 方法
     */
    public static void main(String[] args) {
        UndertowServer.start(WebosConfig.class, 8084, true);
    }

    public void configConstant(Constants me) {
        //在调用getPropertyToBoolean之前需要先调用loadPropertyFile
        //loadPropertyFile("config.properties");
        //设置jfinal的开发模式
       // me.setDevMode(getPropertyToBoolean("devMode",true));


      me.setDevMode(true);
        me.setError404View("/error.html");
        me.setError500View("/error.html");
    }

    public void configRoute(Routes me) {
        //me.setBaseViewPath("/webapp");
      me.add("/api/getnews", HomeController.class);
        me.add("/api/Article", NewController.class);
me.add("/api/taobao",NewController.class);
        me.add("/", HomeController.class);
        me.add("/api/ApiLogin", com.asxsyd92.Controllers.webos.LoginControllers.class);
me.add("/ueditor", UeditorController.class);
        me.add("/api/Users", com.asxsyd92.Controllers.webos.UsersControllers.class);
    }

    public void configEngine(Engine me) {
    //  me.setBaseTemplatePath("webapp");
        //me.setToClassPathSourceFactory();
    }
    public void configPlugin(Plugins me) {
        loadPropertyFile("config.properties");
        me.add(new JwtTokenPlugin(UserService.me));                                /**配置权限拦截插件*/
        int initialSize = getPropertyToInt("initialSize");
        int minIdle = getPropertyToInt("minIdle");
        int maxActive = getPropertyToInt("maxActive");
        //获取jdbc连接池
        String url=getProperty("jdbcUrl");
        String user=getProperty("user");
        String pw=getProperty("password");
        DruidPlugin druidPlugin = new DruidPlugin(url,user, pw);

        me.add(druidPlugin);


        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
       // arp.getEngine().setToClassPathSourceFactory();
        arp.setShowSql(true);
        arp.addSqlTemplate("/sql/webos.sql");
       // arp.setShowSql(getPropertyToBoolean("showSql",true));
        me.add(arp);


    }
    public void configInterceptor(Interceptors me) {
        me.add(new AuthInterceptor()); // 权限拦截器
    }
    public void configHandler(Handlers me) {

    }


}
