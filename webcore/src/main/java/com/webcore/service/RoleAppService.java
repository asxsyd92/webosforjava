package com.webcore.service;

import com.asxsydutils.utils.StringUtil;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.webcore.modle.RoleApp;
import com.webcore.modle.xRoleApp;
import com.jfinal.plugin.activerecord.Db;


import java.util.List;



public class RoleAppService {
    private  static final RoleApp userDao = new RoleApp().dao();
    private  static final xRoleApp xDao = new xRoleApp().dao();
    // public static final RoleAppService dao = new RoleAppService();
   // private static final  RoleApp userDao = new RoleApp().dao();
    public static java.util.List<RoleApp> GetAllApp(int i) {
        String  sql="select * from RoleApp where ParentID='00000000-0000-0000-0000-000000000000' order by Sort";
        List<RoleApp> da=userDao.find(sql);
       return da;
    }

    public static List<xRoleApp> GetxAppList(String role) {

        if (role.equals("")){
            role="00000000-0000-0000-0000-000000000000";
        }
            String sql = "select *from RoleApp where ParentID='"+role+"' order by Sort";
            List<xRoleApp> da=xDao.find(sql);

            return da;

    }
    public static List<RoleApp> GetAppList(int role)
  {
            String sql = "select *from RoleApp RA,( select MenuID,ur.identifier from  UsersRole ur,Role r where  ur.identifier=r.identifier and r.identifier=" + role + ") as tab  where  ParentID='00000000-0000-0000-0000-000000000000' and tab.MenuID=RA.ID  order by Sort";
            List<RoleApp> da=userDao.find(sql);
            return da;


    }
    public static boolean DelByID(RoleApp iD)
    {
        RoleApp da= userDao.findById(iD.getID());
     if (da!=null){
         ;
         if (da.delete()){
             Db.deleteById(" usersrole","MenuID",iD.getID());
             Db.deleteById("roleapp","ParentID",iD.getID());
             return true;
         }


     }

      return  false;
    }

    public Page<xRoleApp> getPageById(int page, int limit, String id) {
        SqlPara sqlPara =  Db.getSqlPara("webos-users.getusersapp", Kv.by("parentid",id));
       return   xDao.paginate(page,limit,sqlPara);

    }

    public boolean Save(RoleApp dictionary) {
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
}
