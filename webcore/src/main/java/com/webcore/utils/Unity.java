package com.webcore.utils;

import com.alibaba.fastjson.JSON;

import com.asxsydutils.utils.StringUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import io.jsonwebtoken.Claims;

import java.lang.reflect.Type;
import java.util.Map;

public class Unity<T> {
    public enum SysOperation
    {//填加系统时间
        _SYS_DATETIME,
        //当前用户ID
        _SYS_GETUSERID,
        //当前用户名
        _SYS_GETUSERNAME,
        //当前昵称
        _SYS_GETUSERNICKNAME,
        //组织id
        _SYS_ORGID,
        //组织名称
        _SYS_ORGNAME,
        //岗位
        _SYS_GW
    }
    public static String getJsonSetData(String data, Claims claims){
        data= data.replace(SysOperation._SYS_DATETIME.name(), StringUtil.getDatatime().toString());
        data= data.replace(SysOperation._SYS_GETUSERID.name(), claims.get("id").toString());
        data= data.replace(SysOperation._SYS_GETUSERNAME.name(),claims.get("name").toString());
        data= data.replace(SysOperation._SYS_GETUSERNICKNAME.name(), claims.get("account").toString());
        data= data.replace(SysOperation._SYS_ORGID.name(), claims.get("orgid").toString() );
        data= data.replace(SysOperation._SYS_ORGNAME.name(), claims.get("orname").toString() );

        return  data;

    }

    public static Record getArgsRecord(Controller _this) {
        try{
            String jsonStr = HttpKit.readData(_this.getRequest());
            if (StrKit.notBlank(jsonStr)) {
                Map ls = JSON.parseObject(jsonStr, Map.class);
                Record r = new Record().setColumns(ls);
                return r;
            } else {
                return new Record();

            }
        }catch (Exception ex){
            System.out.println("接收的JSON参数：" + ex.getMessage());
            return new Record();
        }

    }
    public T getArgsRecord(Controller _this, Class<T> clazz) {
        try{
            String jsonStr = HttpKit.readData(_this.getRequest());
            if (StrKit.notBlank(jsonStr)) {
                T ls = JSON.parseObject(jsonStr, (Type) clazz.getClass());

                return ls;
            } else {
                return null;

            }
        }catch (Exception ex){
            System.out.println("接收的JSON参数：" + ex.getMessage());
            return null;
        }

    }
}

