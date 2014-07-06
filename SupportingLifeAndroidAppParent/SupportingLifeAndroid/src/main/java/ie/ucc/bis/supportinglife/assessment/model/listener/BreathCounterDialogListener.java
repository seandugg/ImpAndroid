package ie.ucc.bis.supportinglife.assessment.model.listener;

public interface BreathCounterDialogListener {	
	public void dialogClosed(boolean timerComplete, boolean fullTimeAssessment, int breathCount);
}
