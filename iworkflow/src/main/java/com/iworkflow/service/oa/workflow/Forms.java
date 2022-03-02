package com.iworkflow.service.oa.workflow;

public class Forms
{
    private String id;

    private String name;

    private String type;

    private int srot;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setSrot(int srot){
        this.srot = srot;
    }
    public int getSrot(){
        return this.srot;
    }
}