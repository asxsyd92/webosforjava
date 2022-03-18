package com.vueseo;

import com.asxsydutils.utils.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SeoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        String url = getDomain(request);
        System.out.print(url+"\n");

        System.out.print("进入查询\n");
        url = url.replace(":9090", "");
        System.out.print(url);
        Record record = Db.findFirst("SELECT *FROM seo WHERE url='"+url.toString().trim()+"' AND html !=''");
        if (record != null) {
            System.out.print("查询成功\n");
            response.setContentType("text/html;charset=utf-8");

            System.out.print(record.getStr("html"));
            response.getWriter().write(record.getStr("html"));


        } else {
            try {
                System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
                WebDriver driver = new ChromeDriver();
                driver.get(url);
                driver.manage().timeouts().pageLoadTimeout(1000 * 60, java.util.concurrent.TimeUnit.SECONDS);
                //休眠10分钟后
                Thread.currentThread().sleep(1000*20);
                String html_source = driver.getPageSource();
                System.out.print(html_source);
                Record r = Db.findById("seo", "url", url);
                if (r == null) {
                    Record rr = new Record();
                    rr.set("id", StringUtil.getPrimaryKey());
                    rr.set("url", url);
                    rr.set("html", html_source);
                    //System.out.print(r.getStr("html"));
                    Db.save("seo", rr);
                } else {
                    r.set("html", html_source);
                    Db.update("seo", r);
                }
                driver.close();
            }catch (Exception ex){
                System.out.print("获取编译后资源错误\n");
                System.out.print(ex.getMessage());
            }
            System.out.print("没有找到页面\n"+url);

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("请稍后再试");

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
    protected String getDomain(ServletRequest httpRequest) {
        HttpServletRequest request = (HttpServletRequest) httpRequest;

        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        return url.toString();


    }

}
