package com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="users", primaryKey="id")

public class Users extends Model<Users> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setName(java.lang.String Name) {
        set("Name", Name);
    }

    public java.lang.String getName() {
        return get("Name");
    }

    public void setAccount(java.lang.String Account) {
        set("Account", Account);
    }

    public java.lang.String getAccount() {
        return get("Account");
    }

    public void setPassword(java.lang.String Password) {
        set("Password", Password);
    }

    public java.lang.String getPassword() {
        return get("Password");
    }

    public void setStatus(java.lang.Integer Status) {
        set("Status", Status);
    }

    public java.lang.Integer getStatus() {
        return get("Status");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }

    public void setIdentifier(java.lang.Integer identifier) {
        set("identifier", identifier);
    }

    public java.lang.Integer getIdentifier() {
        return get("identifier");
    }

    public void setQqopenid(java.lang.String qqopenid) {
        set("qqopenid", qqopenid);
    }

    public java.lang.String getQqopenid() {
        return get("qqopenid");
    }

    public void setAvatar(java.lang.String avatar) {
        set("avatar", avatar);
    }

    public java.lang.String getAvatar() {
        return get("avatar");
    }

    public void setIDcard(java.lang.String IDcard) {
        set("IDcard", IDcard);
    }

    public java.lang.String getIDcard() {
        return get("IDcard");
    }

    public void setSex(java.lang.Integer Sex) {
        set("Sex", Sex);
    }

    public java.lang.Integer getSex() {
        return get("Sex");
    }

    public void setOpenid(java.lang.String openid) {
        set("openid", openid);
    }

    public java.lang.String getOpenid() {
        return get("openid");
    }

    public void setAddtime(java.util.Date Addtime) {
        if(Addtime==null) return;
        set("Addtime", new java.sql.Timestamp(Addtime.getTime()));
    }

    public java.util.Date getAddtime() {
        return get("Addtime");
    }

    public void setIslock(java.lang.Integer Islock) {
        if(Islock==null) return;
        set("Islock",Islock );
    }

    public java.lang.Integer getIslock() {
        return get("Islock");
    }

    public void setlocktime(java.util.Date locktime) {
        if(locktime==null) return;
        set("locktime", new java.sql.Timestamp(locktime.getTime()));
    }

    public java.util.Date getlocktime() {
        return get("locktime");
    }
}
