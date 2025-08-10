package com.example.hurlingapp.assessment.model;

import android.content.Context;
import android.graphics.PointF;
import com.example.hurlingapp.domain.ActionDetail;
import com.example.hurlingapp.domain.EventType;

public class HurlingAssessmentModel extends AbstractAssessmentModel {

    public static final String EVENT_TYPE_PAGE_TITLE = "Event Type";
    public static final String ACTION_DETAIL_PAGE_TITLE = "Action Detail";
    public static final String OUTCOME_PAGE_TITLE = "Outcome";
    public static final String PITCH_PAGE_TITLE = "Pitch";

    private EventType selectedEventType;
    private ActionDetail selectedActionDetail;
    private String selectedOutcome;
    private PointF startLocation;
    private PointF endLocation;

    public HurlingAssessmentModel(Context context) {
        super(context);
    }

    @Override
    protected AssessmentPageList configureAssessmentPageList() {
        return new AssessmentPageList(
                new EventTypePage(this, EVENT_TYPE_PAGE_TITLE).setRequired(true),
                new ActionDetailPage(this, ACTION_DETAIL_PAGE_TITLE).setRequired(true),
                new OutcomePage(this, OUTCOME_PAGE_TITLE).setRequired(true),
                new PitchPage(this, PITCH_PAGE_TITLE).setRequired(true)
        );
    }

    @Override
    protected AnalyticsPageList configureAnalyticsPageList() {
        return new AnalyticsPageList();
    }

    public EventType getSelectedEventType() {
        return selectedEventType;
    }

    public void setSelectedEventType(EventType selectedEventType) {
        this.selectedEventType = selectedEventType;
    }

    public ActionDetail getSelectedActionDetail() {
        return selectedActionDetail;
    }

    public void setSelectedActionDetail(ActionDetail selectedActionDetail) {
        this.selectedActionDetail = selectedActionDetail;
    }

    public String getSelectedOutcome() {
        return selectedOutcome;
    }

    public void setSelectedOutcome(String selectedOutcome) {
        this.selectedOutcome = selectedOutcome;
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
}
