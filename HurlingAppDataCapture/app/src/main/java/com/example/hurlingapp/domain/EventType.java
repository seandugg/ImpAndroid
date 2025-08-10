package com.example.hurlingapp.domain;

import java.util.List;

public class EventType {
    private String event_type;
    private List<ActionDetail> action_details;

    public String getEventType() {
        return event_type;
    }

    public void setEventType(String event_type) {
        this.event_type = event_type;
    }

    public List<ActionDetail> getActionDetails() {
        return action_details;
    }

    public void setActionDetails(List<ActionDetail> action_details) {
        this.action_details = action_details;
    }
}
