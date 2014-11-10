package ie.ucc.bis.supportinglife.assessment.model;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;

public class AssessmentViewPager extends ViewPager {
	private SparseBooleanArray pagingEnabled;
	public Context context;

	public AssessmentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPagingEnabled(new SparseBooleanArray());
		setContext(context);
	}

	public SparseBooleanArray getPagingEnabled() {
		return pagingEnabled;
	}

	public void setPagingEnabled(SparseBooleanArray pagingEnabled) {
		this.pagingEnabled = pagingEnabled;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean checkPagingEnabled(int currentItem) {
		return getPagingEnabled().get(currentItem);
	}

	public void configurePagingEnabledElement(int currentItem, boolean valid) {
		getPagingEnabled().put(currentItem, valid);
	}	
	
}
