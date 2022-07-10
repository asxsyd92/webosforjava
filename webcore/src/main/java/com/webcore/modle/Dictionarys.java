package com.webcore.modle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;
import com.asxsydutils.annotation.Table;

import java.util.List;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="dictionary", primaryKey="id")
public  class  Dictionarys extends Model<Dictionarys> {
    public java.lang.String getLabel() {
        return get("Title");
    }
    public java.lang.String getID() {
        return get("ID");
    }
    public java.lang.String getValue() {
        return get("ID");
    }
    public java.lang.String getName() {
        return get("Title");
    }
    public Boolean getselected() {
        return get("selected");
    }
    public Boolean getdisabled() {
        return get("disabled");
    }
    public List<Dictionarys> children;
    public Boolean selected;
    public Boolean disabled;
    public List<Dictionarys>  getchildren() {
        String sql = "select * from dictionary where ParentID='" + this.getID() + "'  order by Sort";
        return this.find(sql);
    }
}