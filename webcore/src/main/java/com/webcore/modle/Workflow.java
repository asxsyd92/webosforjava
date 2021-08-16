package com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="workflow", primaryKey="id")
@SuppressWarnings("serial")
public class Workflow extends Model<Workflow>  {

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

    public void setType(java.lang.String Type) {
        set("Type", Type);
    }

    public java.lang.String getType() {
        return get("Type");
    }

    public void setManager(java.lang.String Manager) {
        set("Manager", Manager);
    }

    public java.lang.String getManager() {
        return get("Manager");
    }

    public void setInstanceManager(java.lang.String InstanceManager) {
        set("InstanceManager", InstanceManager);
    }

    public java.lang.String getInstanceManager() {
        return get("InstanceManager");
    }

    public void setCreateDate(java.util.Date CreateDate) {
        if(CreateDate==null) return;
        set("CreateDate", new java.sql.Timestamp(CreateDate.getTime()));
    }

    public java.util.Date getCreateDate() {
        return get("CreateDate");
    }

    public void setCreateUserID(java.lang.String CreateUserID) {
        set("CreateUserID", CreateUserID);
    }

    public java.lang.String getCreateUserID() {
        return get("CreateUserID");
    }

    public void setDesignJSON(java.lang.String DesignJSON) {
        set("DesignJSON", DesignJSON);
    }

    public java.lang.String getDesignJSON() {
        return get("DesignJSON");
    }

    public void setInstallDate(java.util.Date InstallDate) {
        if(InstallDate==null) return;
        set("InstallDate", new java.sql.Timestamp(InstallDate.getTime()));
    }

    public java.util.Date getInstallDate() {
        return get("InstallDate");
    }

    public void setInstallUserID(java.lang.String InstallUserID) {
        set("InstallUserID", InstallUserID);
    }

    public java.lang.String getInstallUserID() {
        return get("InstallUserID");
    }

    public void setRunJSON(java.lang.String RunJSON) {
        set("RunJSON", RunJSON);
    }

    public java.lang.String getRunJSON() {
        return get("RunJSON");
    }

    public void setStatus(java.lang.Integer Status) {
        set("Status", Status);
    }

    public java.lang.Integer getStatus() {
        return get("Status");
    }

}
