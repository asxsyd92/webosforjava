package com.asxsyd92.Bll;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class UsersBll {
    private static volatile UsersBll instance = null;
    public static UsersBll Instance() {
        if (instance == null) {
            synchronized (UsersBll.class) {
                if (instance == null) {
                    instance = new UsersBll();
                }
            }
        }
        return instance;
    }

    public Record Login(String user) {
        String sql="SELECT u.ID,u.Account,o.Community,u.avatar,u.Status,u.Name,o.Name as Organize,u.identifier,o.ID as OrganizeID,o.Leader ,u.Password from  Users u,Organize o,UsersRelation ur where o.ID=ur.OrganizeID and ur.ID=u.ID and o.Status=0 and u.Account='" + user.toLowerCase()+ "'";
        return  Db.findFirst(sql);

    }
    public String GetAppList(int role)
    {
        if (role == 110)
        {
          /*  String sql = "select *from RoleApp where ParentID=@ParentID order by Sort";

            var da = xRoleAppDal.Instance.GetList(sql, new { ParentID = Guid.Empty }).ToList();


            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = da.Count == 0 ? 0 : da.First().Count, data = da }, false);
    */    }
        else
        {
          /*  String sql = "select *from RoleApp RA,( select MenuID,ur.identifier from  UsersRole ur,Role r where  ur.identifier=r.identifier and r.identifier=" + role + ") as tab  where  ParentID='00000000-0000-0000-0000-000000000000' and tab.MenuID=RA.ID  order by Sort";
            var da = RoleAppDal.Instance.GetList(sql, null).ToList();

            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = 0, data = da }, false);
*/        }
        return "";
    }
}
