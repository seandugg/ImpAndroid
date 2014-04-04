package ie.ucc.bis.supportinglife.assessment.imci.ui;

import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;

/**
 * 
 * @author timothyosullivan
 */

public interface PageFragmentCallbacks {
	AbstractModel getWizardModel();
    AbstractAssessmentPage getPage(String key);
}
