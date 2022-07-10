package com.seoservice.taskjob;


import com.alibaba.fastjson.JSON;
import com.asxsyd92.quartz.core.JFinalJob;
import com.asxsydutils.utils.HttpHelper;
import com.asxsydutils.utils.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import kotlin.collections.ArrayDeque;
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
    private  List<String> imtlist=new ArrayDeque<>();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
      //休眠10分钟后
            //driver.findElement("d",);
            //for (int a=220001 ;a>520001;a++) {
                driver.get("https://www.tooopen.com/view/2358332.html");//要打开的网址
                System.out.println(driver.getTitle());//得到网页标题
                //Thread.sleep(1000*10);
//下载当前图片
                //  imgView
                //getfile(driver);
           // }


               // driver.findElement(By.id("kw")).clear();//模拟点击搜索文本框动作

    //driver.findElement(By.id("kw")).sendKeys("asxsyd92.com");//模拟键盘输入要搜索的内容

    //driver.findElement(By.id("su")).click();//模拟点击搜索按钮

   // Thread.sleep(1000*10);



            List<WebElement> aList = driver.findElements(By.className("pic"));
            try {

                for (WebElement a : aList) {
                    String urlStr = a.getAttribute("href");
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
                for (String a : imtlist) {
                    driver.get(a);
                    Thread.sleep(1000*10);
                    getfile(driver);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
           // Thread.sleep(1000*10);
 driver.quit();//退出

        }catch (Exception ex){
            System.out.print("获取编译后资源错误\n");
            System.out.print(ex.getMessage());
        }
    }

    public  void getfile(WebDriver driver){
        try{
            WebElement d=  driver.findElement(By.id("imgView"));
            String K= d.getAttribute("src");
            String[] strs = K.split("/");
            String  path= strs[strs.length - 1];
            System.out.print(K);
            HttpHelper. downloadFile(K,"","d:/web/"+path);
        }catch (Exception ex){}


    }

}