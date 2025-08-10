package com.example.hurlingapp.assessment.model;

import java.util.ArrayList;

public class AnalyticsPageList extends ArrayList<AbstractAnalyticsPage> implements AnalyticsPage {

	private static final long serialVersionUID = 4993886940022082251L;

	public AnalyticsPageList(AbstractAnalyticsPage... pages) {
        for (AbstractAnalyticsPage page : pages) {
            add(page);
        }
    }

	@Override
    public AbstractAnalyticsPage findAnalyticsPageByKey(String key) {
        for (AbstractAnalyticsPage childPage : this) {
        	AbstractAnalyticsPage found = childPage.findAnalyticsPageByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
