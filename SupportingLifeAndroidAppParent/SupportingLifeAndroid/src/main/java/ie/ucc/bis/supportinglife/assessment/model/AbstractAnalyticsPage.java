package ie.ucc.bis.supportinglife.assessment.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class AbstractAnalyticsPage implements AnalyticsPage {

    /**
     * Current wizard values/selections.
     */
    protected Bundle pageData = new Bundle();
    protected String title;
    protected String parentKey;
    
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */    
    protected AbstractAnalyticsPage(String title) {
        setTitle(title);
    }
    
    public abstract Fragment createFragment();
    
	/**
	 * Abstract Method: getDataAnalytics
	 * 
	 * Override this method to define the data analytics
	 * associated with the page.
	 * 
	 * @param dataAnalytics : ArrayList<DataAnalytic>
	 */
    public abstract void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics);

    public String getKey() {
        return (getParentKey() != null) ? getParentKey() + ":" + getTitle() : getTitle();
    }

    public AbstractAnalyticsPage findAnalyticsPageByKey(String key) {
        return getKey().equals(key) ? this : null;
    }
    
    public boolean isCompleted() {
        return true;
    }
    
	/**
	 * Setter Method: setData()
	 * 
	 */      
    public void setPageData(Bundle pageData) {
        this.pageData = pageData;
    }

	/**
	 * Getter Method: getData()
	 * 
	 */	   
    public Bundle getPageData() {
        return this.pageData;
    }

	/**
	 * Setter Method: setTitle()
	 * 
	 */      
    public void setTitle(String title) {
    	this.title = title;
    }
    
	/**
	 * Getter Method: getTitle()
	 * 
	 */	      
    public String getTitle() {
        return this.title;
    }

	/**
	 * Setter Method: setParentKey()
	 * 
	 */  	     
    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

	/**
	 * Getter Method: getParentKey()
	 * 
	 */	    
    public String getParentKey() {
        return this.parentKey;
    }


}
