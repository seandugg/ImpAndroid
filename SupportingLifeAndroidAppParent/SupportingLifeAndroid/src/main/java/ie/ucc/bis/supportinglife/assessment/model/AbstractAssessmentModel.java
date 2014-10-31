package ie.ucc.bis.supportinglife.assessment.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

/**
 * Represents an assessment model, including the pages/steps in the assessment, their dependencies, their 
 * data analytics, and the currently populated choices/values/selections on the assessment page.
 *
 * To create the SupportingLIFE breadcrumb UI assessment, extend this class 
 * and implement {@link #configurePageList()}.
 * 
 * @author timothyosullivan
 */

public abstract class AbstractAssessmentModel extends AbstractModel implements ModelCallbacks {
	private List<ModelCallbacks> modelListeners = new ArrayList<ModelCallbacks>();
    private AssessmentPageList assessmentPages;

	/**
	 * Abstract Method: configureAssessmentPageList
	 * 
	 * Override this method to define assessment pages
	 */
    protected abstract AssessmentPageList configureAssessmentPageList();
    
	/**
	 * Abstract Method: configureAnalyticsPageList
	 * 
	 * Override this method to define analytic pages
	 */
    protected abstract AnalyticsPageList configureAnalyticsPageList();
    
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */
    public AbstractAssessmentModel(Context context) {
    	super(context);
    	setApplicationContext(context);
        setAssessmentPages(configureAssessmentPageList());
        setAnalyticsPages(configureAnalyticsPageList());
    }

	/**
	 * onPageDataChanged method
	 * 
	 * Notify model listeners of a 'pageDataChanged' event
	 * 
	 * @param page : AnalyticsPage
	 */
    public void onPageDataChanged(AbstractAssessmentPage page) {
    	for (ModelCallbacks modelCallback : getModelListeners()) {
    		modelCallback.onPageDataChanged(page);
    	}
    }
    
	/**
	 * onPageTreeChanged method
	 * 
	 * Notify model listeners of a 'pageTreeChanged' event
	 * 
	 */    
    public void onPageTreeChanged() {
    	for (ModelCallbacks modelCallback : getModelListeners()) {
    		modelCallback.onPageTreeChanged();
        }
    }

	/**
	 * findAssessmentPageByKey
	 * 
	 * Utility method to retrieve a bread-crumb UI Wizard
	 * page based on the key
	 * 
	 * @param key : String
	 * @return AbstractAssessmentPage
	 *  
	 */ 
    public AbstractAssessmentPage findAssessmentPageByKey(String key) {
        return getAssessmentPages().findAssessmentPageByKey(key);
    }

	/**
	 * findAnalyticsPageByKey
	 * 
	 * Utility method to retrieve an analytics
	 * page based on the key
	 * 
	 * @param key : String
	 * @return AbstractAnalyticsPage
	 *  
	 */    
	public AbstractAnalyticsPage findAnalyticsPageByKey(String key) {
        return getAnalyticsPages().findAnalyticsPageByKey(key);
	}
	
	/**
	 * Save assessment state
	 * 
	 * @return Bundle
	 */   	
	public Bundle save() {
        Bundle bundle = new Bundle();
        saveAssessmentPages(bundle);
        saveAnalyticPages(bundle);
        return bundle;
	}
	
	/**
	 * Load assessment state
	 * 
	 * @return Bundle
	 */   	
	public void load(Bundle savedValues) {
		loadAssessmentPages(savedValues);
		loadAnalyticPages(savedValues);
	}
    
	/**
	 * Load Assessment Pages
	 * 
	 * @param savedValues : Bundle
	 * @return void
	 */     
    public void loadAssessmentPages(Bundle savedValues) {
    	// load assessment pages into memory    	
        for (String key : savedValues.keySet()) {
        	if (getAssessmentPages() != null) {
        		AbstractAssessmentPage assessmentPage = getAssessmentPages().findAssessmentPageByKey(key);
        		if (assessmentPage != null) {
        			assessmentPage.resetPageData(savedValues.getBundle(key));
        		}
        	}
        }
    }
    
	/**
	 * Load Data Analytic Pages
	 * 
	 * @param savedValues : Bundle
	 * @return void
	 */     
    public void loadAnalyticPages(Bundle savedValues) {
    	// load data analytic pages into memory
        for (String key : savedValues.keySet()) {
        	if (getAnalyticsPages() != null) {
        		AbstractAnalyticsPage analyticsPage = getAnalyticsPages().findAnalyticsPageByKey(key);
        		if (analyticsPage != null) {
        			analyticsPage.setPageData(savedValues.getBundle(key));
        		}
        	}
        }
    }

	/**
	 * Method will capture the assessment page data associated with each 
	 * assessment page and store in bundle.
	 * 
	 * @return Bundle
	 */       
    public Bundle saveAssessmentPages(Bundle bundle) {        
        // save assessment pages 
        for (AbstractAssessmentPage assessmentpage : getAssessmentPageSequence()) {
            bundle.putBundle(assessmentpage.getKey(), assessmentpage.getPageData());
        }
        return bundle;
    }
    
	/**
	 * Method will capture the analytic page data associated with each 
	 * non-assessment page and store in bundle.
	 * 
	 * @return Bundle
	 */       
    public Bundle saveAnalyticPages(Bundle bundle) {
        // save data analytic pages
        for (AbstractAnalyticsPage analyticsPage : getAnalyticsPages()) {
            bundle.putBundle(analyticsPage.getKey(), analyticsPage.getPageData());
        }
        return bundle;
    }
    
	/**
	 * Facilitates the registration of model listeners
	 * (e.g. AssessmentWizardActivity)
	 * 
	 * This ensures the model listener will be informed of 
	 * 'onPageDataChanged' and 'onPageTreeChanged' events
	 * 
	 * @param listener : ModelCallbacks
	 */
    public void registerListener(ModelCallbacks listener) {
        getModelListeners().add(listener);
    }

	/**
	 * Facilitates the unregistering of model listeners
	 * (e.g. AssessmentWizardActivity)
	 * 
	 * This ensures the model listener will no longer be informed of 
	 * 'onPageDataChanged' and 'onPageTreeChanged' events
	 * 
	 * @param listener : ModelCallbacks
	 */ 
    public void unregisterListener(ModelCallbacks listener) {
    	getModelListeners().remove(listener);
    }    
    
    /**
     * Gets the list of wizard steps
     */
    public List<AbstractAssessmentPage> getAssessmentPageSequence() {
        ArrayList<AbstractAssessmentPage> flattened = new ArrayList<AbstractAssessmentPage>();
        getAssessmentPages().flattenCurrentPageSequence(flattened);
        return flattened;
    }

    /**
     * Retrieves the data analytics associated with 
     * each assessment page
     * 
     * @return ArrayList<DataAnalytic>
     */
	public ArrayList<DataAnalytic> gatherPageDataAnalytics() {
		ArrayList<DataAnalytic> dataAnalyticItems =  super.gatherPageDataAnalytics();
        
		// pull back analytics from any assessment related pages (e.g. CCM Ask page)
		if (getAssessmentPages() != null) {
	        for (AbstractAnalyticsPage analyticsPage : getAssessmentPages()) {
	        	analyticsPage.getDataAnalytics(dataAnalyticItems);
	        }
		}
		return dataAnalyticItems;
	}
	
    
    /**
     * Gets the current list of review items, associated with each assessment page, as
     * entered by the user
     * 
     * @return ArrayList<ReviewItem>
     */
	public ArrayList<ReviewItem> gatherAssessmentReviewItems() {
		ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (AbstractAssessmentPage assessmentPage : getAssessmentPageSequence()) {
        	assessmentPage.getReviewItems(reviewItems);
        }
        
        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
		return reviewItems;
	}
	
	public AssessmentPageList getAssessmentPages() {
		return assessmentPages;
	}
 	
	public void setAssessmentPages(AssessmentPageList assessmentPages) {
		this.assessmentPages = assessmentPages;
	}

	public List<ModelCallbacks> getModelListeners() {
		return modelListeners;
	}
 	
	public void setModelListeners(List<ModelCallbacks> modelListeners) {
		this.modelListeners = modelListeners;
	}
}
