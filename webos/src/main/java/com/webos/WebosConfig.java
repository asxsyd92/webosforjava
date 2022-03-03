package com.webos;


import com.asxsyd92.swagger.config.routes.SwaggerRoutes;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.asxsydutils.config.TableConfig;
import com.security.Authorization;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.webos.controllers.UeditorController;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
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
        //me.setError404View("/error.html");
        //me.setError500View("/error.html");
     //   me.setJsonFactory(new FastJsonFactory());
    }

    public void configRoute(Routes me) {
        loadPropertyFile("config.properties");
       me.add("/api/ueditor", UeditorController.class);
       // me.add("/api/Users", UsersControllers.class);
        me.add(new SwaggerRoutes());
        String controllers=getProperty("controllers");
        if (!StringUtil.isBlank(controllers)){
          String a[]=  controllers.split(",");
            for (String item:a) {
                me.scan(item);
            }
        }
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
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        arp.setShowSql(true);
        String sqltemplate=getProperty("sqltemplate");
        if (!StringUtil.isBlank(sqltemplate)){
            String a[]=  sqltemplate.split(",");
            for (String item:a
            ) {
                arp.addSqlTemplate(item);
            }
        }
        arp.setShowSql(getPropertyToBoolean("showSql",false));
        String modle=getProperty("modle");
        TableConfig.mapping(modle,arp);
        me.add(new EhCachePlugin());
        me.add(arp);

    }
    public void configInterceptor(Interceptors me)
    {

        me.add(new Authorization()); // 权限拦截器
    }

    public void configHandler(Handlers me) {
        me.add(new UrlSkipHandler("^/websocket.ws", false));
        me.add(new ContextPathHandler("basePath"));

    }

    public void onStart() {
        String fozuStr="IC4uLi4uLi4uLi4uLi4uLi4uLi4uLi53ZWJvcy4uLi4uLi4uLi4uLi4uLi4uLi4uLi4KICAgICAgICAgICAgICAgICAgICAgICBfb28wb29fICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgb293ZWJvc29vICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICA4OCIgLiAiODggICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICh8IC1fLSB8KSAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgMFwgID0gIC8wICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgX19fL+KAmC0tLeKAmVxfX18gICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgIC4nIFx8ICAgICAgIHwvICcuICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAvIFxcfHx8ICA6ICB8fHwvLyBcICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgLyBffHx8fHwgLeWNjS18fHx8fF8gXCAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICB8ICAgfCBcXFwgIC0gIC8vLyB8ICAgfCAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgIHwgXF98ICAnJ1wtLS0vJycgIHxfLyB8ICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgXCAgLi1cX18gICctJyAgX19fLy0uIC8gICAgICAgICAgICAgIAogICAgICAgICAgICAgX19fJy4gLicgIC8tLS4tLVwgICcuIC4nX19fICAgICAgICAgICAgCiAgICAgICAgICAuIiIg4oCYPCAg4oCYLl9fX1xfPHw+Xy9fX18u4oCZID7igJkgIiIuICAgICAgICAgIAogICAgICAgICB8IHwgOiAg4oCYLSBc4oCYLjvigJhcIF8gL+KAmTsu4oCZLyAtIOKAmSA6IHwgfCAgICAgICAgCiAgICAgICAgIFwgIFwg4oCYXy4gICBcXyBfX1wgL19fIF8vICAgLi3igJkgLyAgLyAgICAgICAgCiAgICAgPT09PT3igJgtLl9fX1/igJguX19fIFxfX19fXy9fX18uLeKAmV9fXy4t4oCZPT09PT0gICAgIAogICAgICAgICAgICAgICAgICAgICAgIOKAmD0tLS094oCZICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLnYxLjUuLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4=";
        byte[] decode = Base64.decode(String.valueOf(fozuStr.toCharArray()));
        System.out.print("\n"+new String(decode));
        System.out.print("\n");

    }


}
