package com.webcore.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

public class CommomService {
    private static volatile CommomService instance = null;
    public static CommomService Instance() {
        if (instance == null) {
            synchronized (CommomService.class) {
                if (instance == null) {
                    instance = new CommomService();
                }
            }
        }
        return instance;
    }
    public Page<Record> GetCommonList(Kv kv, int page, int limit) {
        SqlPara sqlPara =  Db.getSqlPara("webos-commom.getcommonlist", kv);
        String tab= kv.getStr("tab").toLowerCase();
       String newsql= sqlPara.getSql().replace("$table$",tab);
       if(kv.getStr("desc")!=null){
           newsql=newsql+ " order by "+kv.getStr("desc") +" desc";
       }
        SqlPara newsqlPara =new SqlPara();
        newsqlPara.setSql(newsql);
        for (Object o : sqlPara.getPara()){
            newsqlPara.addPara(o);
        }

       // sqlPara.setSql( sqlPara.toString().replace("$table$",tab));

        return Db.paginate(page,limit,newsqlPara);


    }



}
