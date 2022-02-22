package com.ctdisk.controllers;

import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.HttpHelper;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
@Path("/api/ctdisk")
public class CtdiskController extends Controller {

    public void  login() {
        try {
            Map<String,Object> user=new HashMap<>();
            String  token=   CacheKit.get("ctdisktokencache","ctdisktokencache");
            user.put("email","asxsyd92@foxmail.com");
            user.put("password","1111");
            user.put("device_id","");
            user.put("unique_id","");
            if (token==null){
             String data=   HttpHelper.sendPost("https://rest.ctfile.com/v1/user/auth/login",JSON.toJSONString(user));
                Map<String,Object> da=  JSON.parseObject(data,Map.class);
               if (da.get("code").equals(200)){
                   CacheKit.put("ctdisktokencache","ctdisktokencache",da.get("token").toString());
                   setAttr("code", 1);
                   setAttr("msg", da.get("message"));
                   setAttr("success", false);
                   setAttr("count", 0);
                   setAttr("data", da.get("token").toString());
               }else {
                   setAttr("code", 1);
                   setAttr("msg", da.get("message"));
                   setAttr("success", false);
                   setAttr("count", 0);
                   setAttr("data", null);

               }
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

}
