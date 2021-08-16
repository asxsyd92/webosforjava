package  com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

@SuppressWarnings("serial")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="roleapp", primaryKey="id")

public class xRoleApp extends Model<xRoleApp>{

    public List<xRoleApp> children ;

    public List<xRoleApp> getchildren() {

       // String sql = "select * from RoleApp RA,( select MenuID, ur.identifier from UsersRole ur, Role r where  ur.identifier = r.identifier ) as tab  where RA.ParentID = '" + this.getID() + "' and tab.MenuID = RA.ID and ParentID !='00000000-0000-0000-0000-000000000000' order by Sort";
        String sql = "select * from RoleApp where ParentID='" + this.getID() + "' order by Sort";
        return this.find(sql);
    }

    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setParentID(java.lang.String ParentID) {
        set("ParentID", ParentID);
    }

    public java.lang.String getParentID() {
        return get("ParentID");
    }

    public void setRoleID(java.lang.String RoleID) {
        set("RoleID", RoleID);
    }

    public java.lang.String getRoleID() {
        return get("RoleID");
    }

    public void setAppID(java.lang.String AppID) {
        set("AppID", AppID);
    }

    public java.lang.String getAppID() {
        return get("AppID");
    }

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setParams(java.lang.String Params) {
        set("Params", Params);
    }

    public java.lang.String getParams() {
        return get("Params");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }

    public void setIco(java.lang.String Ico) {
        set("Ico", Ico);
    }

    public java.lang.String getIco() {
        return get("Ico");
    }

    public void setType(java.lang.Integer Type) {
        set("Type", Type);
    }

    public java.lang.Integer getType() {
        return get("Type");
    }

    public void setMenuUser(java.lang.String MenuUser) {
        set("MenuUser", MenuUser);
    }

    public java.lang.String getMenuUser() {
        return get("MenuUser");
    }

    public void setIcon(java.lang.String icon) {
        set("icon", icon);
    }

    public java.lang.String getIcon() {
        return get("icon");
    }

    public void setTag(java.lang.String tag) {
        set("tag", tag);
    }

    public java.lang.String getTag() {
        return get("tag");
    }

    public void setTagtype(java.lang.Integer tagtype) {
        set("tagtype", tagtype);
    }

    public java.lang.Integer getTagtype() {
        return get("tagtype");
    }

    public void setOpentype(java.lang.Integer opentype) {
        set("opentype", opentype);
    }

    public java.lang.Integer getOpentype() {
        return get("opentype");
    }

    public void setMaxOpen(java.lang.Integer maxOpen) {
        set("maxOpen", maxOpen);
    }

    public java.lang.Integer getMaxOpen() {
        return get("maxOpen");
    }

    public void setExtend(java.lang.String extend) {
        set("extend", extend);
    }

    public java.lang.String getExtend() {
        return get("extend");
    }

    public void setColor(java.lang.String Color) {
        set("Color", Color);
    }

    public java.lang.String getColor() {
        return get("Color");
    }

}
