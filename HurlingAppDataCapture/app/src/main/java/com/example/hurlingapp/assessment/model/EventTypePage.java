package com.example.hurlingapp.assessment.model;

import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.assessment.ui.EventTypeFragment;
import android.support.v4.app.Fragment;

public class EventTypePage extends AbstractAssessmentPage {

    public EventTypePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return EventTypeFragment.create(getKey());
    }
}
