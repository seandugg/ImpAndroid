package com.example.hurlingapp.assessment.model;

public interface FragmentLifecycle {
	public static final String ARG_PAGE_KEY = "PAGE_KEY";
	
	public void onPauseFragment(AbstractModel assessmentModel);
	public void onResumeFragment(AbstractModel assessmentModel);
}
