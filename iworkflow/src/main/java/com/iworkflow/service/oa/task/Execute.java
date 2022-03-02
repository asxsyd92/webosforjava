package com.iworkflow.service.oa.task;

import com.webcore.modle.Users;

import java.util.List;

public class Execute {
    private String urls ;
    /// <summary>
    /// 流程ID
    /// </summary>
    private String flowid ;
    /// <summary>
    /// 步骤ID
    /// </summary>
    private String stepid ;
    /// <summary>
    /// 任务ID
    /// </summary>
    private String taskid ;
    /// <summary>
    /// 实例ID
    /// </summary>
    private String instanceid ;
    /// <summary>
    /// 分组ID
    /// </summary>
    private String groupid ;
    /// <summary>
    /// 标题
    /// </summary>
    private String title ;
    /// <summary>
    /// 操作类型
    /// </summary>
    private EnumType.ExecuteType ExecuteType ;
    /// <summary>
    /// 发送人员
    /// </summary>
    private Users sender ;

    public java.util.Map<String, List<Users>>getSteps() {
        return steps;
    }

    public void setSteps(java.util.Map<String, List<Users>> steps) {
        this.steps = steps;
    }

    /// <summary>
    /// 接收的步骤和人员
    /// </summary>
    private java.util.Map<String, List<Users>> steps ;
    /// <summary>
    /// 处理意见
    /// </summary>
    private String comment ;
    /// <summary>
    /// 是否签章
    /// </summary>
    private Boolean issign ;
    /// <summary>
    /// 备注
    /// </summary>
    private String note ;
    /// <summary>
    /// 其他类型
    /// </summary>
    private int othertype ;
    /// <summary>
    /// 相关附件
    /// </summary>
    private String files ;
    /// <summary>
    /// 收文通知单id
    /// </summary>
    private int sendid ;

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getFlowid() {
        return flowid;
    }

    public void setFlowid(String flowid) {
        this.flowid = flowid;
    }

    public String getStepid() {
        return stepid;
    }

    public void setStepid(String stepid) {
        this.stepid = stepid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EnumType.ExecuteType getExecuteType() {
        return ExecuteType;
    }

    public void setExecuteType(EnumType.ExecuteType executeType) {
        ExecuteType = executeType;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIssign() {
        return issign;
    }

    public void setIssign(Boolean issign) {
        this.issign = issign;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getOthertype() {
        return othertype;
    }

    public void setOthertype(int othertype) {
        this.othertype = othertype;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public int getSendid() {
        return sendid;
    }

    public void setSendid(int sendid) {
        this.sendid = sendid;
    }

    public String getF_whs() {
        return f_whs;
    }

    public void setF_whs(String f_whs) {
        this.f_whs = f_whs;
    }

    private String f_whs ;
}
