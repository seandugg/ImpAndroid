package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;

public class BreathCounterDialogFragment extends DialogFragment implements OnCloseListener {
	
	private static final String BREATHE_COUNTER_RESET_ICON_TYPEFACE_ASSET = "fonts/breathe-reset-flaticon.ttf";
	private static final String BREATHE_COUNTER_INCREMENT_ICON_TYPEFACE_ASSET = "fonts/breathe-increment-flaticon.ttf";
	private static final String BREATH_COUNT_TYPEFACE_ASSET = "fonts/RobotoCondensed-Light.ttf";
	
	private static final int ZERO_BREATHS = 0;
	private static final int SECOND = 1;
	private static final int ONE_SECOND_IN_MILLISECONDS = 1000;
	private static final int ONE_MINUTE_IN_SECONDS = 10;
	private static final String BREATHS = "Breaths";
	private static final String BREATH = "Breath";
	private static final String SECONDS = "Seconds";
	
	/* Sounds */
	private static final String TICK_SOUND = "Tick";
	
	private Button resetCounterButton;
	private TextView breathCountTextView;
	private TextView lungsIconTextView;
	private TextView progressTimer;
	private ProgressBar progressBar;
	private Button incrementCounterButton;
	private int progressStatus;
	private int breathCount;
	private Handler handler;
	private Thread timerThread;
	private boolean timerThreadRunning;
	private MediaPlayer soundPlayer;
	private Map<String, Integer> sounds;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog customDialog = new Dialog(getActivity(), R.style.BreathCounterDialog);

	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    customDialog.setContentView(inflater.inflate(R.layout.fragment_ccm_breath_counter_assessment, null));
	    
		configureFonts(customDialog);
				
		configureProgressBar(customDialog);
		
		configureSounds();
		
		setBreathCount(ZERO_BREATHS);
		setTimerThreadRunning(false);
		
		displayProgressTime();
		
		// listen for breath count increment event
		getIncrementCounterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (getBreathCount() == Integer.valueOf(ZERO_BREATHS)) {
            		// need to kick off timer
            		setTimerThreadRunning(true);
           			setTimerThread(new BreathTimerThread());
           			getTimerThread().start();
            	}
            	setBreathCount(getBreathCount() + SECOND);
            	displayBreathNumber();

            	// let's animate the lungs icon to provide visual indicator to user
            	// add scaling effect to lungs
            	getLungsIconTextView().startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale));         
            }
        });
		
		// listen for breath count reset event
		getResetCounterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	setTimerThreadRunning(false);
            	if (getTimerThread() != null) {
            		getTimerThread().interrupt();
            	}
            	// stop ticking sound if timer still going
            	if (isTimerThreadRunning()) {
	            	getSoundPlayer().stop();
	            	getSoundPlayer().release();
            	}
            	
            	// reset breaths counted to zero
            	setBreathCount(ZERO_BREATHS);
            	displayBreathNumber();
            	
            	// reset progress bar
            	setProgressStatus(Integer.valueOf(ZERO_BREATHS));
            	getProgressBar().setProgress(getProgressStatus());
            	displayProgressTime();
            	
				// ensure breath counter button is enabled
				getIncrementCounterButton().setEnabled(true);
            }
        });		
		
		return customDialog;
	}

	/**
	 * Responsible for configuring the sounds associated
	 * with the Breath Counter
	 * 
	 */
	private void configureSounds() {	
		setSounds(new HashMap<String, Integer>());
		getSounds().put(TICK_SOUND, R.raw.second_tick);
		// configure 'second tick' sound
		setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(TICK_SOUND)));
		getSoundPlayer().setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
		getSoundPlayer().setOnPreparedListener(new MediaPlayerPreparedListener(getSoundPlayer()));
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
	
	/**
	 * Responsible for configuring the progress bar timer
	 * 
	 * @param customDialog
	 */
	private void configureProgressBar(Dialog customDialog) {
		setProgressBar((ProgressBar) customDialog.findViewById(R.id.ccm_breath_counter_assessment_progressbar));
		
		// configure max time for progress bar
		// TODO - this should be read from preferences with potential
		// 		  values being 15, 30, 45, 60
		getProgressBar().setMax(ONE_MINUTE_IN_SECONDS);
		
		setProgressTimer((TextView) customDialog.findViewById(R.id.ccm_breath_counter_assessment_progress_timer_status));
		
		setHandler(new Handler());
	}
	
	/**
	 * Thread class to facilitate updates to Timer
	 */
	private class BreathTimerThread extends Thread implements Runnable {
		public void run() {
			
			while (isTimerThreadRunning() && getProgressStatus() < ONE_MINUTE_IN_SECONDS) {
				// Update the progress bar and display the 
				//current value in the text view
				getHandler().post(new Runnable() {
					public void run() {
						setProgressStatus(getProgressStatus() + SECOND);
						getProgressBar().setProgress(getProgressStatus());
						displayProgressTime();
						
						if (getActivity() != null) {
							getSoundPlayer().release();
							setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(TICK_SOUND)));
							getSoundPlayer().setOnPreparedListener(new MediaPlayerPreparedListener(getSoundPlayer()));
						}
					}
				});
				try {
					Thread.sleep(ONE_SECOND_IN_MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (getProgressStatus() == ONE_MINUTE_IN_SECONDS) {
				getHandler().post(new Runnable() {
					public void run() {
						// need to disable breath counter button
						getIncrementCounterButton().setEnabled(false);
						
						setTimerThreadRunning(false);
		            	// stop ticking sound
		            	getSoundPlayer().stop();
		            	getSoundPlayer().release();
					}
				});	
			}
		}		
	}
	
	/**
	 * MediaPlayer Listener to play sound once sound is prepared to be played
	 */
	private class MediaPlayerPreparedListener implements OnPreparedListener {
		private MediaPlayer mediaPlayer;
		
		public MediaPlayerPreparedListener(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}
		
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp == mediaPlayer) {
            	mediaPlayer.start();
            }
        }
    }

	/**
	 * capture dialog close event
	 */
	@Override
	public boolean onClose() {
    	setTimerThreadRunning(false);
    	if (getTimerThread() != null) {
    		getTimerThread().interrupt();
    	}
    	
		if (getSoundPlayer() != null) {
        	// stop ticking sound
        	getSoundPlayer().stop();
        	getSoundPlayer().release();
		}
		return false;
	}
	
	/**
	 * display current timer progress 
	 */
	private void displayProgressTime() {
		getProgressTimer().setText(getProgressStatus() + "/" + getProgressBar().getMax() + " " + SECONDS);
	}
	
	/**
	 * display current number of breaths recorded
	 * 
	 */
	private void displayBreathNumber() {
		if (getBreathCount() == 1) {
			getBreathCountTextView().setText(getBreathCount() + " " + BREATH);
		}
		else {
			getBreathCountTextView().setText(getBreathCount() + " " + BREATHS);
		}
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

	public synchronized TextView getProgressTimer() {
		return progressTimer;
	}

	public synchronized void setProgressTimer(TextView progressTimer) {
		this.progressTimer = progressTimer;
	}

	public synchronized ProgressBar getProgressBar() {
		return progressBar;
	}

	public synchronized void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public synchronized Button getIncrementCounterButton() {
		return incrementCounterButton;
	}

	public synchronized void setIncrementCounterButton(Button incrementCounterButton) {
		this.incrementCounterButton = incrementCounterButton;
	}

	public synchronized int getProgressStatus() {
		return progressStatus;
	}

	public synchronized void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}

	public int getBreathCount() {
		return breathCount;
	}

	public void setBreathCount(int breathCount) {
		this.breathCount = breathCount;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Thread getTimerThread() {
		return timerThread;
	}

	public void setTimerThread(Thread timerThread) {
		this.timerThread = timerThread;
	}

	public synchronized boolean isTimerThreadRunning() {
		return timerThreadRunning;
	}

	public synchronized void setTimerThreadRunning(boolean timerThreadRunning) {
		this.timerThreadRunning = timerThreadRunning;
	}

	public synchronized MediaPlayer getSoundPlayer() {
		return soundPlayer;
	}

	public synchronized void setSoundPlayer(MediaPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	public Map<String, Integer> getSounds() {
		return sounds;
	}

	public void setSounds(Map<String, Integer>  sounds) {
		this.sounds = sounds;
	}
}