package com.webcore.service;

import com.webcore.modle.CommonTask;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

public class TaskService {
    private static volatile CommonTask instance = new CommonTask().dao();
    public static Page<Record> GetPage(int page, int limit,Kv kv) {

        SqlPara sqlPara =  Db.getSqlPara("webos-task.gettask", kv);
      return   Db.paginate(page,limit,sqlPara);
    }

    public static CommonTask Get(String id) {
      return  instance.findById(id);
    }
}
