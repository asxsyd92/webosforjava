package com.service.oa.workflow;


import java.util.List;

public class RunModel
{
    private String id;

    private String name;

    private String type;

    private String manager;

    private String instanceManager;

    private String removeCompleted;

    private String debug;

    private String debugUsers;

    private String note;

    private List<Databases> databases;

    private TitleField titleField;

    private List<Steps> steps;

    private List<Lines> lines;

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
    public void setManager(String manager){
        this.manager = manager;
    }
    public String getManager(){
        return this.manager;
    }
    public void setInstanceManager(String instanceManager){
        this.instanceManager = instanceManager;
    }
    public String getInstanceManager(){
        return this.instanceManager;
    }
    public void setRemoveCompleted(String removeCompleted){
        this.removeCompleted = removeCompleted;
    }
    public String getRemoveCompleted(){
        return this.removeCompleted;
    }
    public void setDebug(String debug){
        this.debug = debug;
    }
    public String getDebug(){
        return this.debug;
    }
    public void setDebugUsers(String debugUsers){
        this.debugUsers = debugUsers;
    }
    public String getDebugUsers(){
        return this.debugUsers;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return this.note;
    }
    public void setDatabases(List<Databases> databases){
        this.databases = databases;
    }
    public List<Databases> getDatabases(){
        return this.databases;
    }
    public void setTitleField(TitleField titleField){
        this.titleField = titleField;
    }
    public TitleField getTitleField(){
        return this.titleField;
    }
    public void setSteps(List<Steps> steps){
        this.steps = steps;
    }
    public List<Steps> getSteps(){
        return this.steps;
    }
    public void setLines(List<Lines> lines){
        this.lines = lines;
    }
    public List<Lines> getLines(){
        return this.lines;
    }
}
