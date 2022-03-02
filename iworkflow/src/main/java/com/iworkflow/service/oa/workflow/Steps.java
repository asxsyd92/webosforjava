package com.iworkflow.service.oa.workflow;

import java.util.List;

public class Steps
{
    private String id;
    private String type ;
    private String name;

    private String opinionDisplay;

    private String expiredPrompt;

    private String signatureType;

    private String workTime;

    private String limitTime;

    private String otherTime;

    private String archives;

    private String archivesParams;

    private String note;

    private Position position;

    private int countersignature;

    private Behavior behavior;

    private List<Forms> forms;

    private List<Buttons> buttons;

    private List<FieldStatus> fieldStatus;
    private  String member;
    private Event event;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

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
    public void setOpinionDisplay(String opinionDisplay){
        this.opinionDisplay = opinionDisplay;
    }
    public String getOpinionDisplay(){
        return this.opinionDisplay;
    }
    public void setExpiredPrompt(String expiredPrompt){
        this.expiredPrompt = expiredPrompt;
    }
    public String getExpiredPrompt(){
        return this.expiredPrompt;
    }
    public void setSignatureType(String signatureType){
        this.signatureType = signatureType;
    }
    public String getSignatureType(){
        return this.signatureType;
    }
    public void setWorkTime(String workTime){
        this.workTime = workTime;
    }
    public String getWorkTime(){
        return this.workTime;
    }
    public void setLimitTime(String limitTime){
        this.limitTime = limitTime;
    }
    public String getLimitTime(){
        return this.limitTime;
    }
    public void setOtherTime(String otherTime){
        this.otherTime = otherTime;
    }
    public String getOtherTime(){
        return this.otherTime;
    }
    public void setArchives(String archives){
        this.archives = archives;
    }
    public String getArchives(){
        return this.archives;
    }
    public void setArchivesParams(String archivesParams){
        this.archivesParams = archivesParams;
    }
    public String getArchivesParams(){
        return this.archivesParams;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return this.note;
    }
    public void setPosition(Position position){
        this.position = position;
    }
    public Position getPosition(){
        return this.position;
    }
    public void setCountersignature(int countersignature){
        this.countersignature = countersignature;
    }
    public int getCountersignature(){
        return this.countersignature;
    }
    public void setBehavior(Behavior behavior){
        this.behavior = behavior;
    }
    public Behavior getBehavior(){
        return this.behavior;
    }
    public void setForms(List<Forms> forms){
        this.forms = forms;
    }
    public List<Forms> getForms(){
        return this.forms;
    }
    public void setButtons(List<Buttons> buttons){
        this.buttons = buttons;
    }
    public List<Buttons> getButtons(){
        return this.buttons;
    }
    public void setFieldStatus(List<FieldStatus> fieldStatus){
        this.fieldStatus = fieldStatus;
    }
    public List<FieldStatus> getFieldStatus(){
        return this.fieldStatus;
    }
    public void setEvent(Event event){
        this.event = event;
    }
    public Event getEvent(){
        return this.event;
    }
}
