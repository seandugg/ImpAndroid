package ie.ucc.bis.supportinglife.assessment.model;


/**
 * Callback interface connecting {@link AbstractAssessmentPage}, {@link AbstractAssessmentModel}, and model container
 * objects (e.g. {@link ie.ucc.bis.supportinglife.activity.ImciAssessmentActivity}.
 * 
 * @author timothyosullivan
 */
public interface ModelCallbacks {
    void onPageDataChanged(AbstractAssessmentPage page);
    void onPageTreeChanged();
}
