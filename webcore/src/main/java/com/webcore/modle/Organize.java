package com.webcore.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="organize", primaryKey="id")
public class Organize extends Model<Organize> {

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

    public void setNumber(java.lang.String Number) {
        set("Number", Number);
    }

    public java.lang.String getNumber() {
        return get("Number");
    }

    public void setType(java.lang.Integer Type) {
        set("Type", Type);
    }

    public java.lang.Integer getType() {
        return get("Type");
    }

    public void setStatus(java.lang.Integer Status) {
        set("Status", Status);
    }

    public java.lang.Integer getStatus() {
        return get("Status");
    }

    public void setParentID(java.lang.String ParentID) {
        set("ParentID", ParentID);
    }

    public java.lang.String getParentID() {
        return get("ParentID");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }

    public void setDepth(java.lang.Integer Depth) {
        set("Depth", Depth);
    }

    public java.lang.Integer getDepth() {
        return get("Depth");
    }

    public void setChildsLength(java.lang.Integer ChildsLength) {
        set("ChildsLength", ChildsLength);
    }

    public java.lang.Integer getChildsLength() {
        return get("ChildsLength");
    }

    public void setChargeLeader(java.lang.String ChargeLeader) {
        set("ChargeLeader", ChargeLeader);
    }

    public java.lang.String getChargeLeader() {
        return get("ChargeLeader");
    }

    public void setLeader(java.lang.String Leader) {
        set("Leader", Leader);
    }

    public java.lang.String getLeader() {
        return get("Leader");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }

    public void setCommunity(java.lang.String Community) {
        set("Community", Community);
    }

    public java.lang.String getCommunity() {
        return get("Community");
    }
    public List<Organize> children;
    public List<Organize>  getchildren() {
        String sql = "select * from organize where ParentID='" + this.getID() + "'  order by Sort";
        return this.find(sql);
    }
}
