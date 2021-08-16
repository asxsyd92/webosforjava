package com.webcore.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.webcore.modle.Log;
import com.asxsydutils.utils.StringUtil;

import java.util.Date;

public  class LogService {
    private  volatile Log instance = new Log();

    /**
     * 插入日志
     * @param title 标题
     * @param msg 内容
     * @param ip IP地址
     * @param user 用户
     * @param userid 用户id
     * @param type 类型
     * @param newxml xml
     * @param oldxml 老的xml
     * @param other 其他
     * @return
     */
    public  boolean addLog(String title,String msg,String ip,String user,String userid,String type,String newxml,String oldxml,String other)
    {
        try
        {
        Log log=new Log();
        log.setID(StringUtil.getPrimaryKey());
        log.setIPAddress(ip);
        log.setTitle(title);
        log.setContents(msg);
        log.setWriteTime(new Date());
        log.setUserID(userid);
        log.setUserName(user);
        log.setType(type);
        log.setNewXml(newxml);
        log.setOldXml(oldxml);
        log.setOthers(other);
        return log.save();
        }catch (Exception ex){
            return false;
        }

    }

    /**
     * 审计日志
     * @param page
     * @param limit
     * @param kv
     * @return
     */
    public Page<Log> getLogList(int page, int limit, Kv kv) {
        SqlPara sqlPara =  Db.getSqlPara("webos-log.getLogList", kv);
        return   instance.paginate(page,limit,sqlPara);
    }
}
