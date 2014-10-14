package ie.ucc.bis.supportinglife.assessment.model;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class AssessmentViewPager extends ViewPager {
	private boolean isPagingEnabled;
	public Context context;

	public AssessmentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPagingEnabled(true);
		setContext(context);
	}
	
	public boolean isPagingEnabled() {
		return isPagingEnabled;
	}

	public void setPagingEnabled(boolean isPagingEnabled) {
		this.isPagingEnabled = isPagingEnabled;
	}

	public void setContext(Context context) {
		this.context = context;
	}	
	
}
