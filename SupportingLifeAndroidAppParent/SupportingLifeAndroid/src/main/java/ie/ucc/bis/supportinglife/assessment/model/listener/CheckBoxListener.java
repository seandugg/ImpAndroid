package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * @author timothyosullivan
 */

public class CheckBoxListener implements OnClickListener {
		
	private AbstractAssessmentPage page;
	private String dataKey;
	
	public CheckBoxListener(AbstractAssessmentPage page, String dataKey) {
		setPage(page);
		setDataKey(dataKey);
	}
	
	@Override
	public void onClick(View v) {
		if (((CheckBox) v).isChecked()) {
			getPage().getPageData().putBoolean(dataKey, true);
		}
		else {
			getPage().getPageData().putBoolean(dataKey, false);
		}
	}
	
	public AbstractAssessmentPage getPage() {
		return page;
	}

	public void setPage(AbstractAssessmentPage page) {
		this.page = page;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
}
