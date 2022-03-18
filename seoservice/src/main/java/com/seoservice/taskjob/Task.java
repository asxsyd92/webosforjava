package com.seoservice.taskjob;


import com.alibaba.fastjson.JSON;
import com.asxsyd92.quartz.core.JFinalJob;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.List;
import java.text.SimpleDateFormat;

import java.util.Date;
public class Task extends JFinalJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
      //休眠10分钟后
            //driver.findElement("d",);
     driver.get("https://www.baidu.com/");//要打开的网址

                driver.findElement(By.id("kw")).clear();//模拟点击搜索文本框动作

    driver.findElement(By.id("kw")).sendKeys("asxsyd92.com");//模拟键盘输入要搜索的内容

    driver.findElement(By.id("su")).click();//模拟点击搜索按钮

    Thread.sleep(1000*10);

    System.out.println(driver.getTitle());//得到网页标题
            Thread.sleep(1000*10);

            List<WebElement> aList = driver.findElements(By.tagName("a"));
            try {

                for (WebElement a : aList) {
                    System.out.println(a.getAttribute("href"));//获取a标签中的URL

                    //获取a标签href属性值
                    String urlStr = a.getAttribute("linkText");
                    if(urlStr.contains("爱上歆随")||urlStr.contains("asxsyd92.com")) {
                        System.out.println(urlStr);//获取a标签中的URL
                        Thread.sleep(1000*10);
                        a.click();
                        Thread.sleep(1000*10);
                        return;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000*10);
 driver.quit();//退出

        }catch (Exception ex){
            System.out.print("获取编译后资源错误\n");
            System.out.print(ex.getMessage());
        }
    }

}