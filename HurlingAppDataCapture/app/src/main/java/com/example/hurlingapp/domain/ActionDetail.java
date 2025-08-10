package com.example.hurlingapp.domain;

import java.util.List;

public class ActionDetail {
    private String action_detail;
    private List<String> outcomes;

    public String getActionDetail() {
        return action_detail;
    }

    public void setActionDetail(String action_detail) {
        this.action_detail = action_detail;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }
}
