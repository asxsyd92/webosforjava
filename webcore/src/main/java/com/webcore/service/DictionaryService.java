package com.webcore.service;

import com.asxsydutils.utils.StringUtil;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import com.webcore.modle.Dictionary;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.List;

public class DictionaryService {
    private  static final Dictionary instance =new  Dictionary().dao();

    public static List<Dictionary> GetDictionary( Kv kv)
    {

        SqlPara para = instance.getSqlPara("webos-dictionary.getpage", kv);

       //return instance.paginate(page,limt);
        //var da = DictionaryDal.Instance.GetPage(page, limt, "ID", new { ParentID = Guid.Empty }).ToList();
        //return Asxsyd92Core.Utils.JSONhelper.ToJson(da);
        //return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = da.Count, data = da }, false);
  return    instance.find(para);
    }
    public static List<Dictionary> GetDictionaryByID(String id)
    {
        String sql="select * from Dictionary where ParentID='" + id + "' ";
        return instance.find(sql);

 }
//    public String GetOptionsByCode(String v, String value)
//    {     object o = null;
//        var da = DictionaryDal.Instance.GetWhere(new { Code = v }).FirstOrDefault();
//        if (da != null)
//        {
//            var das = DictionaryDal.Instance.GetWhere(new { ParentID = da.ID }).ToList();
//            var db = DBConnectionBll.Instance.getTables_SqlServer();
//            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = das.Count == 0 ? 0 : das.First().Count, data = das, db = db }, false);
//        }
//
//        return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = 0, data = o }, false);
//    }

//    public String Delte(String guid) {
//        var tag= DictionaryDal.Instance.Delete(guid);
//        DictionaryDal.Instance.Delete(new { ParentID= guid });
//        if (tag > 0)
//        {
//            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { msg = "删除成功！", Success = true });
//        }
//        else {
//            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { msg = "删除失败！", Success = false });
//        }
//
//    }
    /// <summary>
    /// 通过code获取字典项
    /// </summary>
    /// <param name="code"></param>
    /// <returns></returns>
    public static List<Dictionary> GetByCode(String code)
    {


        return    instance.find("select  id as value  ,title  from dictionary where  IsUse=1 and ParentID in (select ID from dictionary where Code='" + code + "')");

    }

    public static Record GetOptionsByCode(String v, String value) {


        Dictionary da = instance.findFirst("select * from dictionary where Code='"+v+"'");
        if (da != null)
        {
            List<Dictionary> das =instance.find("select * from dictionary where ParentID='"+da.getID()+"'"); //instance.GetWhere(new { ParentID = da.ID }).ToList();
            List<Record> db =getTables_SqlServer();
            Record d=new Record();
            d.set("code",0);
            d.set("msg","获取成功");
            d.set("count",0);
            d.set("data",das);
            d.set("db",db);
            return d;
      }
        Record d=new Record();
        d.set("code",0);
        d.set("msg","获取失败");
        d.set("count",0);
        d.set("data",null);
        d.set("db",null);
        return d;
       // return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = 0, data = o }, false);

    }

    public static List<Record> getTables_SqlServer(){
      String sql=  "select TABLE_NAME as name from information_schema.TABLES where TABLE_SCHEMA=(select database())";
      return Db.find(sql);
    }

    public  Page<Dictionary> getPageById(int page, int lime, String id) {
            SqlPara para = instance.getSqlPara("webos-dictionary.getpage", Kv.by("parentid",id));

            return instance.paginate(page,lime,para);
        }

    public  boolean Save(Dictionary dictionary) {
       if (dictionary.getID().equals("")||dictionary.getID().equals(StringUtil.GuidEmpty())){
          if( dictionary.getParentID().equals("")){
              dictionary.setParentID(StringUtil.GuidEmpty());
          }
           dictionary.setID(StringUtil.getPrimaryKey());
         return dictionary.save();


       }else {
          return dictionary.update();
       }
    }

    public boolean Del(String data) {
       return Db.deleteById("dictionary","id",data);

    }
}
