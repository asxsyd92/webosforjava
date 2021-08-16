package com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="dictionary", primaryKey="id")
public  class  Dictionary extends Model<Dictionary>{
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

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setCode(java.lang.String Code) {
        set("Code", Code);
    }

    public java.lang.String getCode() {
        return get("Code");
    }

    public void setValue(java.lang.String Value) {
        set("Value", Value);
    }

    public java.lang.String getValue() {
        return get("Value");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }

    public void setOther(java.lang.String Other) {
        set("Other", Other);
    }

    public java.lang.String getOther() {
        return get("Other");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }

    public void setIsUse(java.lang.Integer IsUse) {
        set("IsUse", IsUse);
    }

    public java.lang.Integer getIsUse() {
        return get("IsUse");
    }


    public List<Dictionary> children;
    public List<Dictionary>  getchildren() {
        String sql = "select * from dictionary where ParentID='" + this.getID() + "'  order by Sort";
     return this.find(sql);
    }

}

