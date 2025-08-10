package com.example.hurlingapp.assessment.model;

import com.example.hurlingapp.activity.AssessmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class AssessmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int REVIEW_PAGE_POSITION = 0;

	private int mCutOffPage;
    private Fragment mPrimaryItem;
    private AssessmentActivity assessmentActivity;

    public AssessmentPagerAdapter(AssessmentActivity assessmentActivity, FragmentManager fm) {
        super(fm);
        setAssessmentActivity(assessmentActivity);
    }

    @Override
    public Fragment getItem(int i) {
        if (i >= getAssessmentActivity().getAssessmentModel().getAssessmentPageSequence().size()) {
            return getAssessmentActivity().getAssessmentModel().getAnalyticsPages().get(REVIEW_PAGE_POSITION).createFragment();
        }

        return getAssessmentActivity().getAssessmentModel().getAssessmentPageSequence().get(i).createFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object == mPrimaryItem) {
            return POSITION_UNCHANGED;
        }

        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mPrimaryItem = (Fragment) object;
    }

    @Override
    public int getCount() {
        return Math.min(mCutOffPage + 1,
			getAssessmentActivity().getAssessmentModel().getAssessmentPageSequence() != null ?
					getAssessmentActivity().getAssessmentModel().getAssessmentPageSequence().size() + 1
					: mCutOffPage + 1);
    }

    public void setCutOffPage(int cutOffPage) {
        if (cutOffPage < 0) {
            cutOffPage = Integer.MAX_VALUE;
        }
        mCutOffPage = cutOffPage;
    }

    public int getCutOffPage() {
        return mCutOffPage;
    }

	public AssessmentActivity getAssessmentActivity() {
		return assessmentActivity;
	}

	public void setAssessmentActivity(AssessmentActivity assessmentActivity) {
		this.assessmentActivity = assessmentActivity;
	}
}
