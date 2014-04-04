package ie.ucc.bis.supportinglife.assessment.model;

import java.util.ArrayList;

/**
 * Represents a list (ArrayList) of non-assessment pages
 * Used primarily for tracking analytics for non-assessment
 * pages (e.g. review page, classifications tab, treatments tab)
 * 
 * @author timothyosullivan
 */
public class AnalyticsPageList extends ArrayList<AbstractAnalyticsPage> implements AnalyticsPage {

	private static final long serialVersionUID = 4993886940022082251L;

	/**
	 * Constructor
	 * 
	 * @param pages : AnalyticsPage...
	 */	
	public AnalyticsPageList(AbstractAnalyticsPage... pages) {
        for (AbstractAnalyticsPage page : pages) {
            add(page);
        }
    }

	/**
	 * Method: findPageByKey
	 * 
	 * Utility method to retrieve an analytics page
	 * page based on the key
	 * 
	 * @param key : String
	 * @return AnalyticsPage
	 * 
	 */
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
