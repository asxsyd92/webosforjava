package com.service.modle;

import com.asxsydutils.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="workflowarchives", primaryKey="id")
public class WorkFlowArchives extends Model<WorkFlowArchives> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
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

    public void setFlowName(java.lang.String FlowName) {
        set("FlowName", FlowName);
    }

    public java.lang.String getFlowName() {
        return get("FlowName");
    }

    public void setStepName(java.lang.String StepName) {
        set("StepName", StepName);
    }

    public java.lang.String getStepName() {
        return get("StepName");
    }

    public void setTaskID(java.lang.String TaskID) {
        set("TaskID", TaskID);
    }

    public java.lang.String getTaskID() {
        return get("TaskID");
    }

    public void setGroupID(java.lang.String GroupID) {
        set("GroupID", GroupID);
    }

    public java.lang.String getGroupID() {
        return get("GroupID");
    }

    public void setInstanceID(java.lang.String InstanceID) {
        set("InstanceID", InstanceID);
    }

    public java.lang.String getInstanceID() {
        return get("InstanceID");
    }

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setContents(java.lang.String Contents) {
        set("Contents", Contents);
    }

    public java.lang.String getContents() {
        return get("Contents");
    }

    public void setComments(java.lang.String Comments) {
        set("Comments", Comments);
    }

    public java.lang.String getComments() {
        return get("Comments");
    }

    public void setWriteTime(java.util.Date WriteTime) {
        if(WriteTime==null) return;
        set("WriteTime", new java.sql.Timestamp(WriteTime.getTime()));
    }

    public java.util.Date getWriteTime() {
        return get("WriteTime");
    }
}
