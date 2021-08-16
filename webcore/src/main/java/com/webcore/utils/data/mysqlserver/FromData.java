package com.webcore.utils.data.mysqlserver;

import com.webcore.utils.Unity;
import com.webcore.utils.data.MySql;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

import static com.asxsydutils.utils.StringUtil.GuidEmpty;
import static com.asxsydutils.utils.StringUtil.getPrimaryKey;


public class FromData {

    ///
    ///表单数据保存
    ///
    public static String save(Record data,String table) throws Exception {

            List<Record> field = MySql.getMySqlableStructure(table);
            if (field != null) {
                Object _ID = data.getColumns().get("ID");
                if (_ID == null) {
                    return GuidEmpty();
                }
                //查询数据库中的数据
                Record da = Db.findById(table, "ID", _ID);

                if (da != null) {
                    for(Record key : field){
                        String map=  key.getColumns().get("COLUMN_NAME").toString();
                        System.out.println(map.toLowerCase());
                        if(map.equals("ID")||map.equals("id")){
                            //主键不做任何操作

                        }else {
                            String keymap=map.toLowerCase();
                            System.out.println(keymap);
                          Object _data= data.get(keymap);
                            if(_data!=null){

                       if (_data.equals(Unity.SysOperation._SYS_DATETIME.name())){
                           _data=new java.util.Date();
                       }
                                da.set(keymap,_data);
                                System.out.println(_data);
                            }
                        }

                    }
                    if (Db.update(table.toLowerCase(),"ID",da)){
                        return da.getStr("id");

                    }else {
                        return GuidEmpty();
                    }


                }else {
                    String keyid= getPrimaryKey();
                    //新增数据
                    Record _data=new Record();
                    for(Record key : field){
                        //获取字段id
                        String map=  key.getColumns().get("COLUMN_NAME").toString();

                        if(map.equals("ID")||map.equals("id")){
                            _data.set(map.toLowerCase(),   keyid);
                        }else {
                            if (data.get(map)!=null){
                            System.out.println(key);
                            if (data.get(map.toLowerCase()).equals(Unity.SysOperation._SYS_DATETIME.name())){
                                _data.set(map.toLowerCase(),new java.util.Date())  ;
                            }else {

                                    _data.set(map.toLowerCase(),data.get(map.toLowerCase()))  ;
                                }

                            }

                        }
                    }
                    System.out.println(_data);
                    if ( Db.save(table.toLowerCase(),_data)){
                        return keyid;
                    }else {
                        return GuidEmpty();

                    }


                }


            } else {

                return GuidEmpty();
            }


    }

//    public  toDataType(){
//
//
//    }
}


