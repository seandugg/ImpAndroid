package ie.ucc.bis.supportinglife.assessment.model;

public interface FragmentLifecycle {
	public void onPauseFragment(AbstractModel assessmentModel);
	public void onResumeFragment(AbstractModel assessmentModel);
}
