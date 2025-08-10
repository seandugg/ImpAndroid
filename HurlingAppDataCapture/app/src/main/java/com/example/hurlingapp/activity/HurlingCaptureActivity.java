package com.example.hurlingapp.activity;

import com.example.hurlingapp.R;
import com.example.hurlingapp.assessment.model.HurlingAssessmentModel;
import com.example.hurlingapp.assessment.ui.PageSelectedListener;
import com.example.hurlingapp.assessment.ui.StepPagerStrip;
import com.example.hurlingapp.assessment.model.AssessmentPagerAdapter;
import com.example.hurlingapp.assessment.model.AssessmentViewPager;
import com.example.hurlingapp.assessment.model.FragmentLifecycle;
import com.example.hurlingapp.data.HurlingEventDao;
import com.example.hurlingapp.data.HurlingEventDaoImpl;
import com.example.hurlingapp.domain.HurlingEvent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

public class HurlingCaptureActivity extends AssessmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAssessmentModel(new HurlingAssessmentModel(this));

        setContentView(R.layout.activity_hurling_capture);

        if (savedInstanceState != null) {
		getAssessmentModel().load(savedInstanceState.getBundle("assessmentModel"));
        }

        getAssessmentModel().registerListener(this);

        setAssessmentPagerAdapter(new AssessmentPagerAdapter(this, getSupportFragmentManager()));
        setAssessmentViewPager((AssessmentViewPager) findViewById(R.id.pager));
        getAssessmentViewPager().setAdapter(getAssessmentPagerAdapter());
        setStepPagerStrip((StepPagerStrip) findViewById(R.id.strip));

        getStepPagerStrip().setPageSelectedListener(new PageSelectedListener() {
            public void onPageStripSelected(int position) {
                position = Math.min(getAssessmentPagerAdapter().getCount() - 1, position);
                if (getAssessmentViewPager().getCurrentItem() != position) {
			getAssessmentViewPager().setCurrentItem(position);
                }
            }
        });

        setNextButton((Button) findViewById(R.id.next_button));
        setPrevButton((Button) findViewById(R.id.prev_button));

        getAssessmentViewPager().setOnPageChangeListener(pageChangeListener);

        getNextButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (getAssessmentViewPager().getCurrentItem() == getAssessmentModel().getAssessmentPageSequence().size()) {
                    // This is the finish button
                    HurlingAssessmentModel model = (HurlingAssessmentModel) getAssessmentModel();

                    HurlingEvent event = new HurlingEvent();
                    event.setEventType(model.getSelectedEventType().getEventType());
                    event.setActionDetail(model.getSelectedActionDetail().getActionDetail());
                    event.setOutcome(model.getSelectedOutcome());
                    event.setStartLocation(model.getStartLocation());
                    event.setEndLocation(model.getEndLocation());
                    event.setTimestamp(System.currentTimeMillis());

                    HurlingEventDao dao = new HurlingEventDaoImpl(HurlingCaptureActivity.this);
                    dao.addHurlingEvent(event);

                    finish();
                } else {
                    if (isEditingAfterReview()) {
                        getAssessmentViewPager().setCurrentItem(getAssessmentPagerAdapter().getCount() - 1);
                    } else {
                        getAssessmentViewPager().setCurrentItem(getAssessmentViewPager().getCurrentItem() + 1);
                    }
                }
            }
        });

        getPrevButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
		getAssessmentViewPager().setCurrentItem(getAssessmentViewPager().getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
	int currentPosition = 0;

	@Override
	public void onPageSelected(int newPosition) {
		FragmentLifecycle fragmentToShow = (FragmentLifecycle) getAssessmentPagerAdapter().getItem(newPosition);
		fragmentToShow.onResumeFragment(getAssessmentModel());

		FragmentLifecycle fragmentToHide = (FragmentLifecycle) getAssessmentPagerAdapter().getItem(currentPosition);
		fragmentToHide.onPauseFragment(getAssessmentModel());

		currentPosition = newPosition;

		getStepPagerStrip().setCurrentPage(newPosition);

	        if (isConsumePageSelectedEvent()) {
	            setConsumePageSelectedEvent(false);
	            return;
	        }

	        setEditingAfterReview(false);
	        updateBottomBar();
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) { }

	public void onPageScrollStateChanged(int arg0) { }
    };
}
