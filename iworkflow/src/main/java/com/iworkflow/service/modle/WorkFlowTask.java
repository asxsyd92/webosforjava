package com.service.modle;

import com.webcore.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="workflowtask", primaryKey="id")
public class WorkFlowTask extends Model<WorkFlowTask> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setPrevID(java.lang.String PrevID) {
        set("PrevID", PrevID);
    }

    public java.lang.String getPrevID() {
        return get("PrevID");
    }

    public void setPrevStepID(java.lang.String PrevStepID) {
        set("PrevStepID", PrevStepID);
    }

    public java.lang.String getPrevStepID() {
        return get("PrevStepID");
    }

    public void setFlowID(java.lang.String FlowID) {
        set("FlowID", FlowID);
    }

    public java.lang.String getFlowID() {
        return get("FlowID");
    }

    public void setStepID(java.lang.String StepID) {
        set("StepID", StepID);
    }

    public java.lang.String getStepID() {
        return get("StepID");
    }

    public void setStepName(java.lang.String StepName) {
        set("StepName", StepName);
    }

    public java.lang.String getStepName() {
        return get("StepName");
    }

    public void setInstanceID(java.lang.String InstanceID) {
        set("InstanceID", InstanceID);
    }

    public java.lang.String getInstanceID() {
        return get("InstanceID");
    }

    public void setGroupID(java.lang.String GroupID) {
        set("GroupID", GroupID);
    }

    public java.lang.String getGroupID() {
        return get("GroupID");
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

    public void setSenderTime(java.util.Date SenderTime) {
        if(SenderTime==null) return;
        set("SenderTime", new java.sql.Timestamp(SenderTime.getTime()));
    }

    public java.util.Date getSenderTime() {
        return get("SenderTime");
    }

    public void setReceiveID(java.lang.String ReceiveID) {
        set("ReceiveID", ReceiveID);
    }

    public java.lang.String getReceiveID() {
        return get("ReceiveID");
    }

    public void setReceiveName(java.lang.String ReceiveName) {
        set("ReceiveName", ReceiveName);
    }

    public java.lang.String getReceiveName() {
        return get("ReceiveName");
    }

    public void setReceiveTime(java.util.Date ReceiveTime) {
        if(ReceiveTime==null) return;
        set("ReceiveTime", new java.sql.Timestamp(ReceiveTime.getTime()));
    }

    public java.util.Date getReceiveTime() {
        return get("ReceiveTime");
    }

    public void setOpenTime(java.util.Date OpenTime) {
        if(OpenTime==null) return;
        set("OpenTime", new java.sql.Timestamp(OpenTime.getTime()));
    }

    public java.util.Date getOpenTime() {
        return get("OpenTime");
    }

    public void setCompletedTime(java.util.Date CompletedTime) {
        if(CompletedTime==null) return;
        set("CompletedTime", new java.sql.Timestamp(CompletedTime.getTime()));
    }

    public java.util.Date getCompletedTime() {
        return get("CompletedTime");
    }

    public void setCompletedTime1(java.util.Date CompletedTime1) {
        if(CompletedTime1==null) return;
        set("CompletedTime1", new java.sql.Timestamp(CompletedTime1.getTime()));
    }

    public java.util.Date getCompletedTime1() {
        return get("CompletedTime1");
    }

    public void setComment(java.lang.String Comment) {
        set("Comment", Comment);
    }

    public java.lang.String getComment() {
        return get("Comment");
    }

    public void setIsSign(java.lang.Integer IsSign) {
        set("IsSign", IsSign);
    }

    public java.lang.Integer getIsSign() {
        return get("IsSign");
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

    public void setSubFlowGroupID(java.lang.String SubFlowGroupID) {
        set("SubFlowGroupID", SubFlowGroupID);
    }

    public java.lang.String getSubFlowGroupID() {
        return get("SubFlowGroupID");
    }

    public void setUrls(java.lang.String Urls) {
        set("Urls", Urls);
    }

    public java.lang.String getUrls() {
        return get("Urls");
    }
}
