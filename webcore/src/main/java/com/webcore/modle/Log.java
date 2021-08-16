package com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="log", primaryKey="id")
public class Log extends Model<Log> {

    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setType(java.lang.String Type) {
        set("Type", Type);
    }

    public java.lang.String getType() {
        return get("Type");
    }

    public void setWriteTime(java.util.Date WriteTime) {
        if(WriteTime==null) return;
        set("WriteTime", new java.sql.Timestamp(WriteTime.getTime()));
    }

    public java.util.Date getWriteTime() {
        return get("WriteTime");
    }

    public void setUserID(java.lang.String UserID) {
        set("UserID", UserID);
    }

    public java.lang.String getUserID() {
        return get("UserID");
    }

    public void setUserName(java.lang.String UserName) {
        set("UserName", UserName);
    }

    public java.lang.String getUserName() {
        return get("UserName");
    }

    public void setIPAddress(java.lang.String IPAddress) {
        set("IPAddress", IPAddress);
    }

    public java.lang.String getIPAddress() {
        return get("IPAddress");
    }

    public void setURL(java.lang.String URL) {
        set("URL", URL);
    }

    public java.lang.String getURL() {
        return get("URL");
    }

    public void setContents(java.lang.String Contents) {
        set("Contents", Contents);
    }

    public java.lang.String getContents() {
        return get("Contents");
    }

    public void setOthers(java.lang.String Others) {
        set("Others", Others);
    }

    public java.lang.String getOthers() {
        return get("Others");
    }

    public void setOldXml(java.lang.String OldXml) {
        set("OldXml", OldXml);
    }

    public java.lang.String getOldXml() {
        return get("OldXml");
    }

    public void setNewXml(java.lang.String NewXml) {
        set("NewXml", NewXml);
    }

    public java.lang.String getNewXml() {
        return get("NewXml");
    }

}
