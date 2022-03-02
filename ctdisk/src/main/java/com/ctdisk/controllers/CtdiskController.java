package com.ctdisk.controllers;

import com.alibaba.fastjson.JSON;

import com.asxsydutils.utils.HttpHelper;
import com.asxsydutils.utils.StringUtil;
import com.asxsydutils.utils.WebosResponse;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.security.JwtInterceptor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
@Path("/api/ctdisk")
@Before({ POST.class, JwtInterceptor.class})
public class CtdiskController extends Controller {

    @NotAction
    public WebosResponse login() {
        WebosResponse response=new WebosResponse();
        try {

            Map<String,Object> user=new HashMap<>();
            user.put("email","asxsyd92@foxmail.com");
            user.put("password","..642135..");
            user.put("device_id","");
            user.put("unique_id","");
             String data=   HttpHelper.sendPost("https://rest.ctfile.com/v1/user/auth/login",JSON.toJSONString(user));
                Map<String,Object> da=  JSON.parseObject(data,Map.class);
               if (da.get("code").equals(200)){
                   CacheKit.put("ctdisktokencache","ctdisktokencache",da.get("token").toString());
                   response.setData(da.get("token").toString());
                   response.setSuccess(true);
                   return response;
               }else {
                   response.setMessage(da.get("message").toString());
                   response.setSuccess(false);
                   return response;
               }

        }catch (Exception ex){
            response.setMessage("错误："+ex.getMessage());
            response.setSuccess(false);
            return response;
           }

    }

    public void filelist(){
        try{
            String folder_id = getPara("folder_id");
            Integer start=getInt("start");
            if (start==null){
                start=0;
            }
            if (folder_id==null){
                folder_id="d0";
            }
            int total=0;
            Map<String,Object> da=new HashMap<>();
            da.put("folder_id",folder_id);
            da.put("start",0);
            String token=  CacheKit.get("ctdisktokencache","ctdisktokencache");
            if (token==null){
                WebosResponse response= login();
                if (response.getSuccess()){
                    token=  CacheKit.get("ctdisktokencache","ctdisktokencache");

                }else {
                    setAttr("code", 1);
                    setAttr("msg", response.getMessage());
                    setAttr("success", false);
                    setAttr("count", 0);
                    setAttr("data", null);
                    renderJson();
                    return;
                }

            }
            da.put("session",token);
            String data=   HttpHelper.sendPost("https://rest.ctfile.com/v1/public/file/list",JSON.toJSONString(da));
            Map<String,Object> list=  JSON.parseObject(data, Map.class);
            total=Integer.parseInt(list.get("num").toString());
            if (list.get("code").equals(200)){
                List<Map<String,Object>> lists=  JSON.parseObject(list.get("results").toString(), List.class);

                setAttr("code", 0);
                setAttr("msg", "成功！");
                setAttr("success", true);
                setAttr("count", total);
                setAttr("data", lists);
            }else {
                setAttr("code", 1);
                setAttr("msg", list.get("message"));
                setAttr("success", false);
                setAttr("count", 0);
                setAttr("data", null);

            }


        }catch (Exception ex){
            setAttr("code", 1);
            setAttr("msg", "错误："+ex.getMessage());
            setAttr("success", false);
            setAttr("count", 0);
            setAttr("data", null);

        }
        renderJson();
    }
public  void  syndown(){
    try{
        String folder_id = getPara("folder_id");
        String group= getPara("group");
        Integer start=getInt("start");
        String classid=getPara("classid");
        if (start==null){
            start=0;
        }
        if (folder_id==null){
            folder_id="d0";
        }
        int total=0;
        Map<String,Object> da=new HashMap<>();
        da.put("folder_id",folder_id);
        da.put("start",0);
        String token=  CacheKit.get("ctdisktokencache","ctdisktokencache");
        if (token==null){
            WebosResponse response= login();
            if (response.getSuccess()){
                token=  CacheKit.get("ctdisktokencache","ctdisktokencache");

            }else {
                setAttr("code", 1);
                setAttr("msg", response.getMessage());
                setAttr("success", false);
                setAttr("count", 0);
                setAttr("data", null);
                renderJson();
                return;
            }

        }
        da.put("session",token);
        String data=   HttpHelper.sendPost("https://rest.ctfile.com/v1/public/file/list",JSON.toJSONString(da));
        Map<String,Object> list=  JSON.parseObject(data, Map.class);
        total=Integer.parseInt(list.get("num").toString());
        if (list.get("code").equals(200)){
            List<Map<String,Object>> lists=  JSON.parseObject(list.get("results").toString(), List.class);
            for (Map<String,Object> item:lists) {
               Record e= Db.findById("download","Url",item.get("weblink").toString());
               if (e==null){
                   Record record=new Record();
                   record.set("id", StringUtil.getPrimaryKey());
                   record.set("Title",item.get("name").toString());
                   record.set("Url",item.get("weblink").toString());
                   record.set("Description","8077");
                   record.set("views",0);
                   record.set("classid",classid);
                   record.set("Adddate",new java.util.Date());
                   record.set("size",item.get("size").toString());
                   record.set("key",item.get("key").toString());
                   record.set("status",item.get("status").toString());
                   record.set("groupname",group);
                   record.set("imgsrc",item.get("imgsrc").toString());
                   record.set("icon",item.get("icon").toString());
                   Db.save("download",record);
               }

            }
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("success", true);
            setAttr("count", total);
            setAttr("data", lists);
        }else {
            setAttr("code", 1);
            setAttr("msg", list.get("message"));
            setAttr("success", false);
            setAttr("count", 0);
            setAttr("data", null);

        }


    }catch (Exception ex){
        setAttr("code", 1);
        setAttr("msg", "错误："+ex.getMessage());
        setAttr("success", false);
        setAttr("count", 0);
        setAttr("data", null);

    }
    renderJson();
}
}


