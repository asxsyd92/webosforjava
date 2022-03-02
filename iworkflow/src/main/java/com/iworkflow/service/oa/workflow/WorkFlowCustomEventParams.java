package com.iworkflow.service.oa.workflow;

/// <summary>
/// 调用流程事件时的参数实体
/// </summary>

public class WorkFlowCustomEventParams
        {
/// <summary>
/// 流程ID
/// </summary>
private String flowid ;

/// <summary>
/// 步骤ID
/// </summary>
private String stepid ;
/// <summary>
/// 组ID
/// </summary>
private String groupid ;
/// <summary>
/// 任务ID
/// </summary>
private String taskid ;
/// <summary>
/// 实例ID
/// </summary>
private String instanceid ;
/// <summary>
/// 其它参数
/// </summary>
private Object other ;

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

                public String getGroupid() {
                        return groupid;
                }

                public void setGroupid(String groupid) {
                        this.groupid = groupid;
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

                public Object getOther() {
                        return other;
                }

                public void setOther(Object other) {
                        this.other = other;
                }

        }