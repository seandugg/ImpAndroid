package com.example.hurlingapp.assessment.model;

// import com.example.hurlingapp.assessment.model.review.ReviewItem;

import java.util.ArrayList;

import android.os.Bundle;

public abstract class AbstractAssessmentPage extends AbstractAnalyticsPage implements AssessmentPageTreeNode {

    protected boolean required = false;
	protected ModelCallbacks modelCallbacks;

    /*
    public abstract void getReviewItems(ArrayList<ReviewItem> reviewItems);
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
    
	public ModelCallbacks getModelCallbacks() {
		return modelCallbacks;
	}

	public void setModelCallbacks(ModelCallbacks modelCallbacks) {
		this.modelCallbacks = modelCallbacks;
	}

    public AbstractAssessmentPage setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public boolean isRequired() {
        return this.required;
    }
}
