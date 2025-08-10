package com.example.hurlingapp.assessment.model;

// import com.example.hurlingapp.analytics.DataAnalytic;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;

public abstract class AbstractModel {
	private Context applicationContext;
    private AnalyticsPageList analyticsPages;
    
    protected abstract AnalyticsPageList configureAnalyticsPageList();
    
    public AbstractModel(Context context) {
    	setApplicationContext(context);
        setAnalyticsPages(configureAnalyticsPageList());
    }

	public AbstractAnalyticsPage findAnalyticsPageByKey(String key) {
        return getAnalyticsPages().findAnalyticsPageByKey(key);
	}
	
	public Bundle save() {
        Bundle bundle = new Bundle();
        saveAnalyticPages(bundle);
        return bundle;
	}
	
	public void load(Bundle savedValues) {
		loadAnalyticPages(savedValues);
	}
        
    public void loadAnalyticPages(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
        	getAnalyticsPages().findAnalyticsPageByKey(key).setPageData(savedValues.getBundle(key));
        }
    }
    
    public Bundle saveAnalyticPages(Bundle bundle) {
        for (AbstractAnalyticsPage analyticsPage : getAnalyticsPages()) {
            bundle.putBundle(analyticsPage.getKey(), analyticsPage.getPageData());
        }
        return bundle;
    }
    
    /*
	public ArrayList<DataAnalytic> gatherPageDataAnalytics() {
		ArrayList<DataAnalytic> dataAnalyticItems = new ArrayList<DataAnalytic>();
        
        for (AbstractAnalyticsPage analyticsPage : getAnalyticsPages()) {
        	analyticsPage.getDataAnalytics(dataAnalyticItems);
        }        
		return dataAnalyticItems;
	}
	*/
	
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
