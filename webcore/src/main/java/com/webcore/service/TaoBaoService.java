package com.webcore.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

public class TaoBaoService {
    public static Page<Record> GetDataitemList(Kv kv, int page, int limit) {
        SqlPara sqlPara =  Db.getSqlPara("webos-article.t_map_dataitem", kv);


        return Db.paginate(page,limit,sqlPara);


    }

}
