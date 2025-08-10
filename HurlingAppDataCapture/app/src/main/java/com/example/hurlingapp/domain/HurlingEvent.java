package com.example.hurlingapp.domain;

import android.graphics.PointF;

public class HurlingEvent {
    private long id;
    private String eventType;
    private String actionDetail;
    private String outcome;
    private PointF startLocation;
    private PointF endLocation;
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public PointF getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(PointF startLocation) {
        this.startLocation = startLocation;
    }

    public PointF getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(PointF endLocation) {
        this.endLocation = endLocation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
