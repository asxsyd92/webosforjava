package com.webcore.service;

import com.webcore.modle.Users;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.Date;
import java.util.List;
import java.util.Map;

public  class UsersService {
    private static volatile Users instance = new Users().dao();

    public  Record Login(String user) {
        String sql="SELECT u.ID,u.Account,u.Islock,u.locktime,o.Community,u.avatar,u.Status,u.Name,o.Title as Organize,u.identifier,o.ID as OrganizeID,o.Leader ,u.Password from  Users u,Organize o,UsersRelation ur where o.ID=ur.OrganizeID and ur.ID=u.ID and o.Status=0 and u.Account='" + user.toLowerCase()+ "'";
        return  Db.findFirst(sql);

    }

    public  List<Users> GetUsersTreeAsync() {
        String sql="select u.ID,u.Name,o.Community ,o.Title as Organize,o.ID as OrganizeID  from UsersRelation ur,Users u, Organize o where u.ID = ur.UserID and o.ID = ur.OrganizeID";
       return instance.find(sql);
    }

    public  Users Get(String toUser) {
       return instance.findById(toUser);
    }

    public static List<Record> getUserAll() {

     String sql="select u.ID,u.Name,o.Community ,o.Title as Organize,o.ID as OrganizeID  from UsersRelation ur,Users u, Organize o where u.ID = ur.UserID and o.ID = ur.OrganizeID ";

      return   Db.find(sql);
    }

    public List<Record>  getChildren(String id,int identifier) {
        String sql = "select * from RoleApp RA,( select MenuID, ur.identifier from UsersRole ur, Role r where  ur.identifier = r.identifier and r.identifier = " + identifier + ") as tab  where RA.ParentID = '" + id + "' and tab.MenuID = RA.ID and ParentID !='00000000-0000-0000-0000-000000000000' order by Sort";
        return Db.find(sql);
    }
    public static Page<Record> GetUserList(int page, int limt, Kv kv)
    {

        SqlPara sqlPara =  Db.getSqlPara("webos-users.getusers", kv);
        return Db.paginate(page,limt,sqlPara);
    }

    public  List<Record> GetAppList(int role)
    {

        if (role == 110)
        {

            String sql = "select *from RoleApp where ParentID='00000000-0000-0000-0000-000000000000' order by Sort";

          List<Record> da= Db.find(sql);
          return da;}
       else
        {
            String sql = "select *from RoleApp RA,( select MenuID,ur.identifier from  UsersRole ur,Role r where  ur.identifier=r.identifier and r.identifier=" + role + ") as tab  where  ParentID='00000000-0000-0000-0000-000000000000' and tab.MenuID=RA.ID  order by Sort";
            List<Record> da= Db.find(sql);
            return da;

        }
    }

    public  List<Record> GetOrganizeAndRole() {
        return  Db.find("SELECT ID,Title ,'1' as type,3 AS identifier FROM Organize UNION ALL SELECT ID, Title, '0' as type,identifier FROM Role");
    }

    public  Record GetUserByAccount(Object account) {
        return  Db.findFirst("select * from users where account='"+account+"'");
    }

    public  boolean Insert(Record user) {

          if( Db.save("users",user)) {

              Record ors = Db.findFirst("select * from  UsersRelation where OrganizeID='" + user.getStr("OrganizeID") + "' and id='" + user.getStr("ID") + "'");

              if (ors == null) {
                  Record ur = new Record();

                  ur.set("OrganizeID", user.getStr("OrganizeID"));
                  ur.set("ID", user.getStr("ID"));
                  ur.set("IsMain", 1);
                  ur.set("Sort", 1);
                  return Db.save("UsersRelation", user);

              } else {
                  ors.set("OrganizeID", user.getStr("OrganizeID"));
                  return Db.save("UsersRelation", user);
              }
          }
        return false;
    }

    public boolean  update(Users users) {
        return users.update();
    }

    public  Boolean InsertIslock(String id , boolean islock) {

        if (islock){
            //锁定
            Users da=  instance.findById(id);
            if (da!=null){
                da.setlocktime(new Date());
                int i=da.getIslock()==null?0: da.getIslock()+1;
                da.setIslock(i);
             return    da.update();
            }
        }else {
            //解锁
            Users da=  instance.findById(id);
            if (da!=null){

                da.setIslock(0);
            return     da.update();
            }
        }
        return false;
    }

    public boolean save(Users users, Map user) {

        if( users.save()) {
            Record ors = Db.findFirst("select * from  UsersRelation where OrganizeID='" + user.get("organizeid") + "' and id='" + users.getID() + "'");

            if (ors == null) {
                Record ur = new Record();

                ur.set("organizeid", user.get("organizeid"));
                ur.set("id", users.getID());
                ur.set("ismain", 1);
                ur.set("sort", 1);
                return Db.save("UsersRelation", ur);

            } else {
                ors.set("organizeid", user.get("OrganizeID"));
                return Db.save("UsersRelation", ors);
            }

        }
        return false;
    }
}
