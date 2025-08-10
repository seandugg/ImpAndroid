package com.example.hurlingapp.assessment.model;

import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.assessment.ui.OutcomeFragment;
import android.support.v4.app.Fragment;

public class OutcomePage extends AbstractAssessmentPage {

    public OutcomePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return OutcomeFragment.create(getKey());
    }
}
