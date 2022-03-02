package com.iworkflow.service.oa.workflow;
import java.util.List;
import java.util.Map;

public class RunJson {
  public List<Map<String, Object>> getData() {
    return data;
  }

  public void setData(List<Map<String, Object>> data) {
    this.data = data;
  }

  public Map<String, Object> getFrom() {
    return from;
  }

  public void setFrom(Map<String, Object> from) {
    this.from = from;
  }

  public Map<String, Object> getField() {
    return field;
  }

  public void setField(Map<String, Object> field) {
    this.field = field;
  }

  List<Map<String,Object>> data;
  Map<String,Object> from;
  Map<String,Object> field;
}
