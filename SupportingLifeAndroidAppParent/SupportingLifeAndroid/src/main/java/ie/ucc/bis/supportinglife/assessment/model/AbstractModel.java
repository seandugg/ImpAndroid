package ie.ucc.bis.supportinglife.assessment.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;

/**
 * Represents an assessment model i.e. the data analytics of the assessment
 * screens
 * 
 * @author timothyosullivan
 */

public abstract class AbstractModel {
	private Context applicationContext;
    private AnalyticsPageList analyticsPages;
    
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
    public AbstractModel(Context context) {
    	setApplicationContext(context);
        setAnalyticsPages(configureAnalyticsPageList());
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
        saveAnalyticPages(bundle);
        return bundle;
	}
	
	/**
	 * Load assessment state
	 * 
	 * @return Bundle
	 */   	
	public void load(Bundle savedValues) {
		loadAnalyticPages(savedValues);
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
        	getAnalyticsPages().findAnalyticsPageByKey(key).setPageData(savedValues.getBundle(key));
        }
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
     * Retrieves the data analytics associated with 
     * each non-assessment page
     * 
     * @return ArrayList<DataAnalytic>
     */
	public ArrayList<DataAnalytic> gatherPageDataAnalytics() {
		ArrayList<DataAnalytic> dataAnalyticItems = new ArrayList<DataAnalytic>();
        
		// pull back analytics from any non-assessment related pages (e.g. review page)
        for (AbstractAnalyticsPage analyticsPage : getAnalyticsPages()) {
        	analyticsPage.getDataAnalytics(dataAnalyticItems);
        }        
		return dataAnalyticItems;
	}
	
    public Context getApplicationContext() {
		return applicationContext;
	}
 
	public void setApplicationContext(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	public AnalyticsPageList getAnalyticsPages() {
		return analyticsPages;
	}

	public void setAnalyticsPages(AnalyticsPageList analyticsPages) {
		this.analyticsPages = analyticsPages;
	}
}
