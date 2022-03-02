package com.service.oa.task;


import com.service.modle.WorkFlowTask;

import java.util.List;


public class Result {
    public Boolean getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(Boolean issuccess) {
        this.issuccess = issuccess;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getDebugmessages() {
        return debugmessages;
    }

    public void setDebugmessages(String debugmessages) {
        this.debugmessages = debugmessages;
    }

    public Object[] getOther() {
        return other;
    }

    public void setOther(Object[] other) {
        this.other = other;
    }

    public List<WorkFlowTask> getNexttasks() {
        return nexttasks;
    }

    public void setNexttasks(List<WorkFlowTask> nexttasks) {
        this.nexttasks = nexttasks;
    }

    /// <summary>
    /// 是否成功
    /// </summary>
    private Boolean issuccess ;
    /// <summary>
    /// 提示信息
    /// </summary>
    private String messages ;
    /// <summary>
    /// 调试信息
    /// </summary>
    private String debugmessages ;
    /// <summary>
    /// 其它信息
    /// </summary>
    private Object[] other ;
    /// <summary>
    /// 后续任务
    /// </summary>
    private List<WorkFlowTask> nexttasks ;
}
