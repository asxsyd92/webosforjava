package  com.asxsyd92.Modle;

import com.asxsyd92.Dal.RoleAppDal;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

public class RoleApp<M extends RoleApp<M>> extends Model<M> implements IBean {
public String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getParams() {
        return Params;
    }

    public void setParams(String params) {
        Params = params;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getIco() {
        return Ico;
    }

    public void setIco(String ico) {
        Ico = ico;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTagtype() {
        return tagtype;
    }

    public void setTagtype(int tagtype) {
        this.tagtype = tagtype;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public void setMaxOpen(int maxOpen) {
        this.maxOpen = maxOpen;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String ParentID;
    public String RoleID ;
    public String AppID ;
    public String Title ;
    public String Params ;
    public int Sort ;
    public String Ico ;
    public int Type ;

    public String icon ;

    public String tag ;

    public int tagtype ;
    public int openType ;
    public int maxOpen ;
    public String Color ;
    public String extend ;
    public int identifier ;
/*    public long Count ;*/


    public String id() {  return this.ID; }
    public String name() { return this.Title;  }

    public String title() { return this.Title;  }

    public List<RoleAppDal> children()
    {

            String sql = "select * from RoleApp RA,( select MenuID, ur.identifier from UsersRole ur, Role r where  ur.identifier = r.identifier and r.identifier = " + this.identifier + ") as tab  where RA.ParentID = '" + this.ID + "' and tab.MenuID = RA.ID and ParentID !='00000000-0000-0000-0000-000000000000' order by Sort";
            return RoleAppDal.dao.find(sql);

    }

}

