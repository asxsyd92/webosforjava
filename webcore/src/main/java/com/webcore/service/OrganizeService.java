package com.webcore.service;

import com.jfinal.aop.Inject;
import com.webcore.modle.Dictionary;
import com.webcore.modle.Organize;
import com.webcore.modle.Users;
import com.webcore.modle.WorkGroup;

import java.util.*;
import java.util.stream.Collectors;

public class OrganizeService {
    private  static final Organize instance =new Organize().dao();
    @Inject
    UsersService usersService;
  public   List<Organize> GetOrganizeById(String id){
        String sql="select * from Organize where ParentID='" + id + "' ";
        return instance.find(sql);
    }
    /// <summary>
    /// 得到一组机构字符串下所有人员
    /// </summary>
    /// <param name="idString"></param>
    /// <returns></returns>
    public  List<Users> GetAllUsers(String idString)
    {
        if (idString==null)
        {
            return new ArrayList<Users>();
        }
        String[] idArray = idString.split(",");
        List<Users> userList = new ArrayList<Users>();
        WorkGroup bwg = new WorkGroup();
        for (String id : idArray)
        {
            if (id.startsWith("u_"))//人员
            {
                userList.add(usersService.Get(id.replace("u_", "")));

            }

            else if (id.startsWith("w_"))//工作组
            {
                //addWorkGroupUsers(userList, bwg.Get(id.Replace("u_", "").ToString().ToGuid()));
            }
            else  //机构
            {
               // userList.add(GetAllUsers(id.ToGuid()));
            }
        }
        userList=  userList.stream().filter(p -> !(p == null)).collect(Collectors.toList());
        return userList;
    }

    /// <summary>
    /// 得到一个机构下的所有人员
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
//    public List<Users> GetAllUsers(String id)
//    {
//        var childs = GetAllChilds(id);
//        List<String> ids = new List<String>();
//        ids.Add(id.ToString());
//        foreach (var child in childs)
//        {
//            ids.Add(child.ID.ToString());
//        }
//        String formData = "";
//
//        foreach (String url in ids)
//        {
//            formData += "'" + url + "'" + ",";
//        }
//        formData = formData.Replace(",", "");
//        return UsersDal.Instance.GetList("SELECT u.ID,u.Account,u.Name,o.Name as Organize,o.ID as OrganizeID,o.Leader from  Users u,Organize o,UsersRelation ur where o.ID=ur.OrganizeID and ur.UserID=u.ID and o.Status=0 and ur.OrganizeID in(" + formData + ")", null).ToList();
//        //   return UsersDal.Instance.GetList("",null).ToList();//.GetAllByOrganizeIDArray(ids.ToArray());
//    }
    /// <summary>
    /// 查询一组岗位下所有人员
    /// </summary>
    /// <param name="organizeID"></param>
    /// <returns></returns>
//    public List<Users> GetAllByOrganizeIDArray(Guid[] organizeIDArray)
//    {
//
//        return UsersDal.Instance.GetList("SELECT u.ID,u.Account,u.Name,o.Name as Organize,o.ID as OrganizeID,o.Leader from  Users u,Organize o,UsersRelation ur where o.ID=ur.OrganizeID and ur.UserID=u.ID and o.Status=0 ur.OrganizeID in(" + organizeIDArray.ToArray() + ")", null).ToList();
//
//    }

    /// <summary>
    /// 查询一个组织的所有下级
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
//    public List<Organize> GetAllChilds(Guid id)
//    {
//        var org = Dal.OrganizeDal.Instance.Get(id);
//        if (org == null)
//        {
//            return new List<Organize>();
//        }
//        return Dal.OrganizeDal.Instance.GetWhere(new { Number = org.Number }).ToList();//.GetAllChild(org.Number);
//    }
}
