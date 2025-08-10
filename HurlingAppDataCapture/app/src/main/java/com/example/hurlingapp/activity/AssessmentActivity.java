package com.example.hurlingapp.activity;

import com.example.hurlingapp.R;
import com.example.hurlingapp.assessment.model.AbstractAssessmentModel;
import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.AssessmentPagerAdapter;
import com.example.hurlingapp.assessment.model.AssessmentViewPager;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.assessment.ui.PageFragmentCallbacks;
import com.example.hurlingapp.assessment.ui.ReviewFragmentCallbacks;
import com.example.hurlingapp.assessment.ui.StepPagerStrip;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssessmentActivity extends BaseActivity implements
		PageFragmentCallbacks, ReviewFragmentCallbacks, ModelCallbacks {

	private AssessmentViewPager assessmentViewPager;
    private AssessmentPagerAdapter assessmentPagerAdapter;
	private AbstractAssessmentModel assessmentModel;
	private StepPagerStrip stepPagerStrip;

    private boolean editingAfterReview;
    private boolean consumePageSelectedEvent;

    private Button nextButton;
	private Button prevButton;

	@Override
    public void onPageTreeChanged() {
        recalculateCutOffPage();
        stepPagerStrip.setPageCount(assessmentModel.getAssessmentPageSequence().size() + 1); // + 1 = review step
        assessmentPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onPageDataChanged(AbstractAssessmentPage page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
		assessmentPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    private boolean recalculateCutOffPage() {
        int cutOffPage = assessmentModel.getAssessmentPageSequence().size() + 1;
        for (int i = 0; i < assessmentModel.getAssessmentPageSequence().size(); i++) {
            AbstractAssessmentPage page = assessmentModel.getAssessmentPageSequence().get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (assessmentPagerAdapter.getCutOffPage() != cutOffPage) {
		assessmentPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    protected void updateBottomBar() {
        int position = assessmentViewPager.getCurrentItem();
        if (position == assessmentModel.getAssessmentPageSequence().size()) {
            nextButton.setText("Finish");
        } else {
		nextButton.setText(editingAfterReview
                    ? "Review"
                    : "Next");
		nextButton.setBackgroundResource(R.drawable.breadcrumb_next_button);
		nextButton.setTextColor(getResources().getColor(android.R.color.white));

            nextButton.setEnabled(position != assessmentPagerAdapter.getCutOffPage());
        }
        prevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    public AbstractAssessmentPage getPage(String key) {
        return assessmentModel.findAssessmentPageByKey(key);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        assessmentModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("assessmentModel", assessmentModel.save());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
		assessmentModel.load(savedInstanceState.getBundle("assessmentModel"));
        }
    }

    public AbstractAssessmentModel getWizardModel() {
	return assessmentModel;
    }

	public AssessmentPagerAdapter getAssessmentPagerAdapter() {
		return assessmentPagerAdapter;
	}

	public void setAssessmentPagerAdapter(AssessmentPagerAdapter assessmentPagerAdapter) {
		this.assessmentPagerAdapter = assessmentPagerAdapter;
	}

	public AssessmentViewPager getAssessmentViewPager() {
		return assessmentViewPager;
	}

	public void setAssessmentViewPager(AssessmentViewPager assessmentViewPager) {
		this.assessmentViewPager = assessmentViewPager;
	}

	public StepPagerStrip getStepPagerStrip() {
		return stepPagerStrip;
	}

	public void setStepPagerStrip(StepPagerStrip stepPagerStrip) {
		this.stepPagerStrip = stepPagerStrip;
	}

	public AbstractAssessmentModel getAssessmentModel() {
		return assessmentModel;
	}

	public void setAssessmentModel(AbstractAssessmentModel assessmentModel) {
		this.assessmentModel = assessmentModel;
	}

    public Button getNextButton() {
		return nextButton;
	}

	public void setNextButton(Button nextButton) {
		this.nextButton = nextButton;
	}

	public Button getPrevButton() {
		return prevButton;
	}

	public void setPrevButton(Button prevButton) {
		this.prevButton = prevButton;
	}

	public boolean isEditingAfterReview() {
		return editingAfterReview;
	}

	public void setEditingAfterReview(boolean editingAfterReview) {
		this.editingAfterReview = editingAfterReview;
	}

	public boolean isConsumePageSelectedEvent() {
		return consumePageSelectedEvent;
	}

	public void setConsumePageSelectedEvent(boolean consumePageSelectedEvent) {
		this.consumePageSelectedEvent = consumePageSelectedEvent;
	}
}
