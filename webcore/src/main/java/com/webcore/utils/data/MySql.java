package com.webcore.utils.data;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

import static com.jfinal.kit.StrKit.isBlank;

public   class MySql {
public static List<Record> getMySqlableStructure(String table){
    if (isBlank(table)){
        return  null;
    }
    String sql=" select column_name ,column_type , data_type ,  character_maximum_length ,is_nullable , column_default ,column_comment from information_schema.columns where table_name  = '"+table+"'";
     return Db.find(sql);
}


}
