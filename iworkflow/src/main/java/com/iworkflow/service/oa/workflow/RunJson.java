package com.iworkflow.service.oa.workflow;
import java.util.List;
import java.util.Map;

public class RunJson {


  List<Map<String,Object>> data;

  public List<Map<String, Object>> getData() {
    return data;
  }

  public void setData(List<Map<String, Object>> data) {
    this.data = data;
  }

  public Map<String, Object> getForm() {
    return form;
  }

  public void setForm(Map<String, Object> form) {
    this.form = form;
  }

  public Map<String, Object> getRules() {
    return rules;
  }

  public void setRules(Map<String, Object> rules) {
    this.rules = rules;
  }

  Map<String,Object> form;
  Map<String,Object> rules;

  public Map<String, Object> getField() {
    return field;
  }

  public void setField(Map<String, Object> field) {
    this.field = field;
  }

  Map<String,Object>  field;
}
