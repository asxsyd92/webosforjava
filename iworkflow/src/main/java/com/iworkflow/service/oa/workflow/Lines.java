package com.service.oa.workflow;

public class Lines
{
    private String id;

    private String from;

    private String to;

    private String customMethod;

    private String sql;

    private String noaccordMsg;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public String getFrom(){
        return this.from;
    }
    public void setTo(String to){
        this.to = to;
    }
    public String getTo(){
        return this.to;
    }
    public void setCustomMethod(String customMethod){
        this.customMethod = customMethod;
    }
    public String getCustomMethod(){
        return this.customMethod;
    }
    public void setSql(String sql){
        this.sql = sql;
    }
    public String getSql(){
        return this.sql;
    }
    public void setNoaccordMsg(String noaccordMsg){
        this.noaccordMsg = noaccordMsg;
    }
    public String getNoaccordMsg(){
        return this.noaccordMsg;
    }
}