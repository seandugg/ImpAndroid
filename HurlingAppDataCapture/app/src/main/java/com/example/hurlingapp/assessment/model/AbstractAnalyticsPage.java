package com.example.hurlingapp.assessment.model;

// import com.example.hurlingapp.analytics.DataAnalytic;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class AbstractAnalyticsPage implements AnalyticsPage {

    protected Bundle pageData = new Bundle();
    protected String title;
    protected String parentKey;

    protected AbstractAnalyticsPage(String title) {
        setTitle(title);
    }

    public abstract Fragment createFragment();

    /*
    public abstract void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics);
    */

    public String getKey() {
        return (getParentKey() != null) ? getParentKey() + ":" + getTitle() : getTitle();
    }

    public AbstractAnalyticsPage findAnalyticsPageByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    public boolean isCompleted() {
        return true;
    }

    public void setPageData(Bundle pageData) {
        this.pageData = pageData;
    }

    public Bundle getPageData() {
        return this.pageData;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getParentKey() {
        return this.parentKey;
    }
}
