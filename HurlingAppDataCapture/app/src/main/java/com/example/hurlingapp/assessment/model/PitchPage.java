package com.example.hurlingapp.assessment.model;

import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.assessment.ui.PitchFragment;
import android.support.v4.app.Fragment;

public class PitchPage extends AbstractAssessmentPage {

    public PitchPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return PitchFragment.create(getKey());
    }
}
