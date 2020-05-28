package com.asxsyd92.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;


import java.util.List;

public class NewsService {
    public static Page<Record> GetNew(String title, String type, int page, int limit) {
        Kv kv=null;
        kv=Kv.by("1",1);
        if (title!=null&&title!=""){
            kv.set("title",title);
        }
        if (type!=""&&type!=null){

            kv.set(type,type);
        }
        SqlPara sqlPara =  Db.getSqlPara("LoginControllers.getnew", kv);
        return  Db.paginate(page, limit, sqlPara);

    }

    public static List<Record> TaoBaoSearch(String title,  int page, int limit) {


        return Db.find(" select  *from t_map_dataitem order by rand() desc LIMIT 10");
        //return  Db.paginate(page, limit, sqlPara);
    }
}
