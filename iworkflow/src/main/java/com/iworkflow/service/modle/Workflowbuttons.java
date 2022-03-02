package com.iworkflow.service.modle;

import com.asxsydutils.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="workflowbuttons", primaryKey="id")
public class Workflowbuttons  extends Model<Workflowbuttons> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setTitle(java.lang.String Title) {
        set("Title", Title);
    }

    public java.lang.String getTitle() {
        return get("Title");
    }

    public void setIco(java.lang.String Ico) {
        set("Ico", Ico);
    }

    public java.lang.String getIco() {
        return get("Ico");
    }

    public void setScript(java.lang.String Script) {
        set("Script", Script);
    }

    public java.lang.String getScript() {
        return get("Script");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }

    public void setSort(java.lang.Integer Sort) {
        set("Sort", Sort);
    }

    public java.lang.Integer getSort() {
        return get("Sort");
    }
}
