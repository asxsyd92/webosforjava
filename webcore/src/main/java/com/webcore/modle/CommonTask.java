package com.webcore.modle;

import com.asxsydutils.utils.StringUtil;
import com.asxsydutils.annotation.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="commontask", primaryKey="id")
public class CommonTask extends Model<CommonTask> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setInstanceID(java.lang.String InstanceID) {
        set("InstanceID", InstanceID);
    }

    public java.lang.String getInstanceID() {
        return get("InstanceID");
    }

    public void setType(java.lang.Integer Type) {
        set("Type", Type);
    }

    public java.lang.Integer getType() {
        return get("Type");
    }

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setSenderID(java.lang.String SenderID) {
        set("SenderID", SenderID);
    }

    public java.lang.String getSenderID() {
        return get("SenderID");
    }

    public void setSenderName(java.lang.String SenderName) {
        set("SenderName", SenderName);
    }

    public java.lang.String getSenderName() {
        return get("SenderName");
    }

    public void setAddTime(java.util.Date AddTime) {
        if (AddTime == null) return;
        set("AddTime", new java.sql.Timestamp(AddTime.getTime()));
    }

    public java.util.Date getAddTime() {
        return get("AddTime");
    }

    public void setTTable(java.lang.String tTable) {
        set("t_Table", tTable);
    }

    public java.lang.String getTTable() {
        return get("t_Table");
    }

    public void setStatus(java.lang.Integer Status) {
        set("Status", Status);
    }

    public java.lang.Integer getStatus() {
        return get("Status");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }

    public void setClassid(java.lang.String Classid) {
        set("Classid", Classid);
    }

    public java.lang.String getClassid() {
        return get("Classid");
    }
    public void setFromid(java.lang.String fromid) {
        set("fromid", fromid);
    }

    public java.lang.String getFromid() {
        return get("fromid");
    }

    //得到业务表单信息
    public Record Instance() {

        if (this.getTTable() != null && !this.getInstanceID().equals(StringUtil.GuidEmpty()) && !this.getTTable().equals("")) {
            return Db.findById(this.getTTable(), "id", this.getInstanceID());
        }
        return null;

    }
}
