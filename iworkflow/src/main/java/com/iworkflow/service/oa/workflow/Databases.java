package com.service.oa.workflow;

public class Databases
{
    private String link;

    private String linkName;

    private String table;

    private String primaryKey;

    public void setLink(String link){
        this.link = link;
    }
    public String getLink(){
        return this.link;
    }
    public void setLinkName(String linkName){
        this.linkName = linkName;
    }
    public String getLinkName(){
        return this.linkName;
    }
    public void setTable(String table){
        this.table = table;
    }
    public String getTable(){
        return this.table;
    }
    public void setPrimaryKey(String primaryKey){
        this.primaryKey = primaryKey;
    }
    public String getPrimaryKey(){
        return this.primaryKey;
    }
}