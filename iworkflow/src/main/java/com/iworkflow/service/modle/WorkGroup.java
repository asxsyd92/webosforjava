package com.service.modle;

import com.asxsydutils.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="workgroup", primaryKey="id")
public class WorkGroup extends Model<WorkGroup> {
    public void setID(java.lang.String ID) {
        set("ID", ID);
    }

    public java.lang.String getID() {
        return get("ID");
    }

    public void setName(java.lang.String Name) {
        set("Name", Name);
    }

    public java.lang.String getName() {
        return get("Name");
    }

    public void setMembers(java.lang.String Members) {
        set("Members", Members);
    }

    public java.lang.String getMembers() {
        return get("Members");
    }

    public void setNote(java.lang.String Note) {
        set("Note", Note);
    }

    public java.lang.String getNote() {
        return get("Note");
    }
}
