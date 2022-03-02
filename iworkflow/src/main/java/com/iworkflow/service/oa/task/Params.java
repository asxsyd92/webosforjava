package com.service.oa.task;

import com.service.oa.workflow.Steps;

import java.util.List;

public class Params {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  List<Steps> getSteps() {
        return steps;
    }

    public void setSteps( List<Steps> steps) {
        this.steps = steps;
    }

    private String type;
    public List<Steps> steps;
}
