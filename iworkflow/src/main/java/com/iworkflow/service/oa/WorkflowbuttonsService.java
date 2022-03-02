package com.iworkflow.service.oa;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class WorkflowbuttonsService {

    public static List<Record> getAllList() {
        String  sql="select id as value,Title as title  from Workflowbuttons ";
       return Db.find(sql);
    }
}
