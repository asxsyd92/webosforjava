package com.service.oa.workflow;
public class Behavior
{
    private String flowType;

    private String runSelect;

    private String handlerType;

    public String getCopyFor() {
        return copyFor;
    }

    public void setCopyFor(String copyFor) {
        this.copyFor = copyFor;
    }

    private String copyFor;
    private String selectRange;

    private String handlerStep;

    private String valueField;

    private String defaultHandler;

    private String hanlderModel;

    private String backModel;

    private String backType;

    private String backStep;

    private String percentage;

    private String countersignature;

    private String countersignaturePercentage;

    public void setFlowType(String flowType){
        this.flowType = flowType;
    }
    public String getFlowType(){
        return this.flowType;
    }
    public void setRunSelect(String runSelect){
        this.runSelect = runSelect;
    }
    public String getRunSelect(){
        return this.runSelect;
    }
    public void setHandlerType(String handlerType){
        this.handlerType = handlerType;
    }
    public String getHandlerType(){
        return this.handlerType;
    }
    public void setSelectRange(String selectRange){
        this.selectRange = selectRange;
    }
    public String getSelectRange(){
        return this.selectRange;
    }
    public void setHandlerStep(String handlerStep){
        this.handlerStep = handlerStep;
    }
    public String getHandlerStep(){
        return this.handlerStep;
    }
    public void setValueField(String valueField){
        this.valueField = valueField;
    }
    public String getValueField(){
        return this.valueField;
    }
    public void setDefaultHandler(String defaultHandler){
        this.defaultHandler = defaultHandler;
    }
    public String getDefaultHandler(){
        return this.defaultHandler;
    }
    public void setHanlderModel(String hanlderModel){
        this.hanlderModel = hanlderModel;
    }
    public String getHanlderModel(){
        return this.hanlderModel;
    }
    public void setBackModel(String backModel){
        this.backModel = backModel;
    }
    public String getBackModel(){
        return this.backModel;
    }
    public void setBackType(String backType){
        this.backType = backType;
    }
    public String getBackType(){
        return this.backType;
    }
    public void setBackStep(String backStep){
        this.backStep = backStep;
    }
    public String getBackStep(){
        return this.backStep;
    }
    public void setPercentage(String percentage){
        this.percentage = percentage;
    }
    public String getPercentage(){
        return this.percentage;
    }
    public void setCountersignature(String countersignature){
        this.countersignature = countersignature;
    }
    public String getCountersignature(){
        return this.countersignature;
    }
    public void setCountersignaturePercentage(String countersignaturePercentage){
        this.countersignaturePercentage = countersignaturePercentage;
    }
    public String getCountersignaturePercentage(){
        return this.countersignaturePercentage;
    }
}
