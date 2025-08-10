package com.example.hurlingapp.assessment.model;

import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.assessment.ui.ActionDetailFragment;
import android.support.v4.app.Fragment;

public class ActionDetailPage extends AbstractAssessmentPage {

    public ActionDetailPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return ActionDetailFragment.create(getKey());
    }
}
