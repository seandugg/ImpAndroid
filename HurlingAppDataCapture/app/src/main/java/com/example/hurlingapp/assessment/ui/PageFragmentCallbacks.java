package com.example.hurlingapp.assessment.ui;

import com.example.hurlingapp.assessment.model.AbstractAssessmentModel;
import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;

public interface PageFragmentCallbacks {
	AbstractAssessmentModel getWizardModel();
    AbstractAssessmentPage getPage(String key);
}
