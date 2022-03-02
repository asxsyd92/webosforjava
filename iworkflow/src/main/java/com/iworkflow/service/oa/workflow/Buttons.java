package com.iworkflow.service.oa.workflow;

public class Buttons
{
    private String id;

    private int sort;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setSort(int sort){
        this.sort = sort;
    }
    public int getSort(){
        return this.sort;
    }
}