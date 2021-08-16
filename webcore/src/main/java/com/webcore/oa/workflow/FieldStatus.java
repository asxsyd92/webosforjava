package com.webcore.oa.workflow;
public class FieldStatus
{
    private String field;

    private String status;

    private String check;

    public void setField(String field){
        this.field = field;
    }
    public String getField(){
        return this.field;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setCheck(String check){
        this.check = check;
    }
    public String getCheck(){
        return this.check;
    }
}