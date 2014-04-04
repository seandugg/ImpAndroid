package ie.ucc.bis.supportinglife.assessment.imci.ui;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;

/**
 * 
 * @author timothyosullivan
 */

public interface PageFragmentCallbacks {
	AbstractAssessmentModel getWizardModel();
    AbstractAssessmentPage getPage(String key);
}
