package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class BreathCounterDialogFragment extends DialogFragment {
	
	private static final String BREATHE_COUNTER_RESET_ICON_TYPEFACE_ASSET = "fonts/breathe-reset-flaticon.ttf";
	private static final String BREATHE_COUNTER_INCREMENT_ICON_TYPEFACE_ASSET = "fonts/breathe-increment-flaticon.ttf";
	private static final String BREATH_COUNT_TYPEFACE_ASSET = "fonts/RobotoCondensed-Light.ttf";
	
	private static final String ZERO_BREATHS = "0";
	
	private Button resetCounterButton;
	private TextView breathCountTextView;
	private TextView lungsIconTextView;
	private Button incrementCounterButton;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog customDialog = new Dialog(getActivity(), R.style.BreathCounterDialog);

	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    customDialog.setContentView(inflater.inflate(R.layout.fragment_ccm_breath_counter_assessment, null));

		configureFonts(customDialog);
		
		// listen for breath count increment event
		getIncrementCounterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int latestCount = Integer.parseInt(getBreathCountTextView().getText().toString()) + 1;
               getBreathCountTextView().setText(String.valueOf(latestCount));
               
               // let's animate the lungs icon to provide visual indicator to user
               // add scaling effect to lungs
               getLungsIconTextView().startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));         
            }
        });
		
		// listen for breath count reset event
		getResetCounterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getBreathCountTextView().setText(ZERO_BREATHS);
            }
        });		
		
		return customDialog;
	}

	/**
	 * Responsible for configuring the font typefaces for the
	 * customised UI elements on the Breath Counter interface
	 * 
	 * @param customDialog
	 */
	private void configureFonts(Dialog customDialog) {
		// load the flaticon 'counter reset' button font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), BREATHE_COUNTER_RESET_ICON_TYPEFACE_ASSET);		
		setResetCounterButton((Button) customDialog.findViewById(R.id.ccm_breath_counter_assessment_reset_button));
		getResetCounterButton().setTypeface(font);
		
		// load the Roboto 'breath count' font
		font = Typeface.createFromAsset(getActivity().getAssets(), BREATH_COUNT_TYPEFACE_ASSET);		
		setBreathCountTextView((TextView) customDialog.findViewById(R.id.ccm_breath_counter_assessment_breath_count));
		getBreathCountTextView().setTypeface(font);					
		
		// load the flaticon 'increment counter' button font
		font = Typeface.createFromAsset(getActivity().getAssets(), BREATHE_COUNTER_INCREMENT_ICON_TYPEFACE_ASSET);		
		setIncrementCounterButton((Button) customDialog.findViewById(R.id.ccm_breath_counter_assessment_increment_button));
		getIncrementCounterButton().setTypeface(font);		
	    
		// load the flaticon lungs font for the breathe status display
		font = Typeface.createFromAsset(getActivity().getAssets(), LookCcmFragment.BREATHE_ICON_TYPEFACE_ASSET);		
		setLungsIconTextView((TextView) customDialog.findViewById(R.id.ccm_breath_counter_assessment_lungs_icon));
		getLungsIconTextView().setTypeface(font);
	}

	public Button getResetCounterButton() {
		return resetCounterButton;
	}

	public void setResetCounterButton(Button resetCounterButton) {
		this.resetCounterButton = resetCounterButton;
	}

	public TextView getBreathCountTextView() {
		return breathCountTextView;
	}

	public void setBreathCountTextView(TextView breathCountTextView) {
		this.breathCountTextView = breathCountTextView;
	}

	public TextView getLungsIconTextView() {
		return lungsIconTextView;
	}

	public void setLungsIconTextView(TextView lungsIconTextView) {
		this.lungsIconTextView = lungsIconTextView;
	}

	public Button getIncrementCounterButton() {
		return incrementCounterButton;
	}

	public void setIncrementCounterButton(Button incrementCounterButton) {
		this.incrementCounterButton = incrementCounterButton;
	}
}