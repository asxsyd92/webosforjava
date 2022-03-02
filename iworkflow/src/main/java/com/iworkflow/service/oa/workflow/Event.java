package com.iworkflow.service.oa.workflow;
public class Event
{
    private String submitBefore;

    private String submitAfter;

    private String backBefore;

    private String backAfter;

    public void setSubmitBefore(String submitBefore){
        this.submitBefore = submitBefore;
    }
    public String getSubmitBefore(){
        return this.submitBefore;
    }
    public void setSubmitAfter(String submitAfter){
        this.submitAfter = submitAfter;
    }
    public String getSubmitAfter(){
        return this.submitAfter;
    }
    public void setBackBefore(String backBefore){
        this.backBefore = backBefore;
    }
    public String getBackBefore(){
        return this.backBefore;
    }
    public void setBackAfter(String backAfter){
        this.backAfter = backAfter;
    }
    public String getBackAfter(){
        return this.backAfter;
    }
}