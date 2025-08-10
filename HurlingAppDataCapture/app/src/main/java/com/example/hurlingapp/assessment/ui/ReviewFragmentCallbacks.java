package com.example.hurlingapp.assessment.ui;

import com.example.hurlingapp.assessment.model.AbstractAssessmentModel;

public interface ReviewFragmentCallbacks {
    public AbstractAssessmentModel getWizardModel();
    public void onEditScreenAfterReview(String pageKey);
}
