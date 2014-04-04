package ie.ucc.bis.supportinglife.assessment.model;

import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;

import java.util.ArrayList;

import android.os.Bundle;

/**
 * Represents a single page in the bread-crumb UI wizard.
 * 
 * @author timothyosullivan
 */
public abstract class AbstractAssessmentPage extends AbstractAnalyticsPage implements AssessmentPageTreeNode {

    protected boolean required = false;
	protected ModelCallbacks modelCallbacks;

	/**
	 * Abstract Method: getReviewItems
	 * 
	 * Override this method to define the review items
	 * associated with the page.
	 * 
	 * @param reviewItems : ArrayList<ReviewItem>
	 */
    public abstract void getReviewItems(ArrayList<ReviewItem> reviewItems);
    
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */    
    protected AbstractAssessmentPage(ModelCallbacks callbacks, String title) {
    	super(title);
    	setModelCallbacks(callbacks);
    }
    
    public AbstractAssessmentPage findAssessmentPageByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    public void flattenCurrentPageSequence(ArrayList<AbstractAssessmentPage> pages) {
    	pages.add(this);
    }

    public String getKey() {
        return (getParentKey() != null) ? getParentKey() + ":" + getTitle() : getTitle();
    }

    public boolean isCompleted() {
        return true;
    }

    public void resetPageData(Bundle pageData) {
        setPageData(pageData);
        notifyDataChanged();
    }

    public void notifyDataChanged() {
    	getModelCallbacks().onPageDataChanged(this);
    }
    
	/**
	 * Getter Method: getModelCallbacks()
	 * 
	 */		    
	public ModelCallbacks getModelCallbacks() {
		return modelCallbacks;
	}

	/**
	 * Setter Method: setModelCallbacks()
	 * 
	 */  	
	public void setModelCallbacks(ModelCallbacks modelCallbacks) {
		this.modelCallbacks = modelCallbacks;
	}
	
	/**
	 * Setter Method: setRequired()
	 * 
	 */  	    
    public AbstractAssessmentPage setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public boolean isRequired() {
        return this.required;
    }
}
