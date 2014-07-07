package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.model.listener.BreathCounterDialogListener;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BreathCounterDialogFragment extends DialogFragment implements OnDismissListener {

	private static final String BREATHING_DURATION_PREFERENCE = "BREATHING_DURATION_PREFERENCE";
	private static final String BREATHE_COUNTER_RESET_ICON_TYPEFACE_ASSET = "fonts/breathe-reset-flaticon.ttf";
	private static final String BREATHE_COUNTER_INCREMENT_ICON_TYPEFACE_ASSET = "fonts/breathe-increment-flaticon.ttf";
	private static final String BREATH_COUNT_TYPEFACE_ASSET = "fonts/RobotoCondensed-Light.ttf";
	
	private static final int ZERO_BREATHS = 0;
	private static final int SECOND = 1;
	private static final int THIRTY_SECONDS = 30;
	static final int ONE_MINUTE_IN_SECONDS = 60;
	private static final int ONE_SECOND_IN_MILLISECONDS = 1000;
	private static final String BREATHS = "Breaths";
	private static final String BREATH = "Breath";
	private static final String SECONDS = "Seconds";
	
	/* Sounds */
	private static final String TICK_SOUND = "Tick";
	private static final String MIDWAY_NOTIFCATION_SOUND = "Midway";
	private static final String FINISHED_NOTIFICATION_SOUND = "Finished";
    
	private BreathCounterDialogListener breathCounterDialogListener;
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
	private int totalDuration;
	private MediaPlayer soundPlayer;
	private Map<String, Integer> sounds;
	
	/**
	 * Constructor
	 *
	 */
	public BreathCounterDialogFragment() {}
	
	public static BreathCounterDialogFragment create(String breathingDurationPreference) {
		Bundle args = new Bundle();
		args.putInt(BREATHING_DURATION_PREFERENCE, Integer.parseInt(breathingDurationPreference));

		BreathCounterDialogFragment fragment = new BreathCounterDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

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
	            	getSoundPlayer().reset();
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
		getSounds().put(MIDWAY_NOTIFCATION_SOUND, R.raw.midway_notification);
		getSounds().put(FINISHED_NOTIFICATION_SOUND, R.raw.timer_finish);
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
		// this should be read from preferences with potential
		// values being 15, 30, 45, 60
	    setTotalDuration(getArguments().getInt(BREATHING_DURATION_PREFERENCE));
		getProgressBar().setMax(getTotalDuration());
		
		setProgressTimer((TextView) customDialog.findViewById(R.id.ccm_breath_counter_assessment_progress_timer_status));
		
		setHandler(new Handler());
	}
		
	/**
	 * Thread class to facilitate updates to Timer
	 */
	private class BreathTimerThread extends Thread implements Runnable {
		public void run() {
			
			while (isTimerThreadRunning() && getProgressStatus() < getTotalDuration()) {
				// Update the progress bar and display the 
				//current value in the text view
				getHandler().post(new Runnable() {
					public void run() {
						setProgressStatus(getProgressStatus() + SECOND);
						getProgressBar().setProgress(getProgressStatus());
						displayProgressTime();
						
						if (getActivity() != null) {						
							switch(getProgressStatus()) {
								case THIRTY_SECONDS: if (fullTimerDurationConfigured()) {
														setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(MIDWAY_NOTIFCATION_SOUND)));
													  }
													  break;
								default: setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(TICK_SOUND)));
										 break;
							}
							getSoundPlayer().setOnPreparedListener(new MediaPlayerListener(getSoundPlayer()));
							getSoundPlayer().setOnCompletionListener(new MediaPlayerListener(getSoundPlayer()));
						}
					}
				});
				try {
					Thread.sleep(ONE_SECOND_IN_MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (getProgressStatus() == getTotalDuration()) {
				getHandler().post(new Runnable() {
					public void run() {
						if (getActivity() != null) {
							if (fullTimerDurationConfigured()) {
								setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(FINISHED_NOTIFICATION_SOUND)));
								getSoundPlayer().setOnPreparedListener(new MediaPlayerListener(getSoundPlayer()));
								getSoundPlayer().setOnCompletionListener(new MediaPlayerListener(getSoundPlayer()));
							}
						}
						
						// need to disable breath counter button
						getIncrementCounterButton().setEnabled(false);
						
						setTimerThreadRunning(false);
					}
				});	
			}
		}		
	}
	
	/**
	 * MediaPlayer Listener to play sound once sound is prepared to be played
	 */
	private class MediaPlayerListener implements OnPreparedListener, OnCompletionListener {
		private MediaPlayer mediaPlayer;
		
		public MediaPlayerListener(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}
		
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp == mediaPlayer) {
            	mediaPlayer.start();
            }
        }

		@Override
		public void onCompletion(MediaPlayer mp) {
            if (mp == mediaPlayer) {
            	mediaPlayer.reset();
            	mediaPlayer.release();
            }
		}
    }

	/**
	 * capture dialog close event
	 */
	@Override
	public void onDismiss(DialogInterface dialog) {
    	setTimerThreadRunning(false);
    	if (getTimerThread() != null) {
    		getTimerThread().interrupt();
    	}
    	
		if (getSoundPlayer() != null) {
        	// stop ticking sound
        	getSoundPlayer().release();
		}
		
		// return results of breath count to calling fragment
		boolean timerComplete = getProgressStatus() == getTotalDuration();
		boolean fullTimeAssessment = getTotalDuration() == ONE_MINUTE_IN_SECONDS;

		if (timerComplete && !fullTimeAssessment) {
			// need to estimate breath count expectation if
			// assessment was run over a full minute
			int breathCountEstimate = (int) (((float) getBreathCount() / getTotalDuration()) * (float) ONE_MINUTE_IN_SECONDS);
			getBreathCounterDialogListener().dialogClosed(timerComplete, fullTimeAssessment, breathCountEstimate);
		}
		else {			
			getBreathCounterDialogListener().dialogClosed(timerComplete, fullTimeAssessment, getBreathCount());	
		}
		super.onDismiss(dialog);
	}
	
	/**
	 * Indicator for whether the timer duration is 
	 * configured to sixty seconds
	 * 
	 */
	private boolean fullTimerDurationConfigured() {
		if (getTotalDuration() == ONE_MINUTE_IN_SECONDS)
			return true;
		else
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

	public BreathCounterDialogListener getBreathCounterDialogListener() {
		return breathCounterDialogListener;
	}

	public void setBreathCounterDialogListener(
			BreathCounterDialogListener breathCounterDialogListener) {
		this.breathCounterDialogListener = breathCounterDialogListener;
	}

	private Button getResetCounterButton() {
		return resetCounterButton;
	}

	private void setResetCounterButton(Button resetCounterButton) {
		this.resetCounterButton = resetCounterButton;
	}

	private TextView getBreathCountTextView() {
		return breathCountTextView;
	}

	private void setBreathCountTextView(TextView breathCountTextView) {
		this.breathCountTextView = breathCountTextView;
	}

	private TextView getLungsIconTextView() {
		return lungsIconTextView;
	}

	private void setLungsIconTextView(TextView lungsIconTextView) {
		this.lungsIconTextView = lungsIconTextView;
	}

	private synchronized TextView getProgressTimer() {
		return progressTimer;
	}

	private synchronized void setProgressTimer(TextView progressTimer) {
		this.progressTimer = progressTimer;
	}

	private synchronized ProgressBar getProgressBar() {
		return progressBar;
	}

	private synchronized void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	private synchronized Button getIncrementCounterButton() {
		return incrementCounterButton;
	}

	private synchronized void setIncrementCounterButton(Button incrementCounterButton) {
		this.incrementCounterButton = incrementCounterButton;
	}

	private synchronized int getProgressStatus() {
		return progressStatus;
	}

	private synchronized void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}

	private int getBreathCount() {
		return breathCount;
	}

	private void setBreathCount(int breathCount) {
		this.breathCount = breathCount;
	}

	private Handler getHandler() {
		return handler;
	}

	private void setHandler(Handler handler) {
		this.handler = handler;
	}

	private Thread getTimerThread() {
		return timerThread;
	}

	private void setTimerThread(Thread timerThread) {
		this.timerThread = timerThread;
	}

	private synchronized boolean isTimerThreadRunning() {
		return timerThreadRunning;
	}

	private synchronized void setTimerThreadRunning(boolean timerThreadRunning) {
		this.timerThreadRunning = timerThreadRunning;
	}

	private int getTotalDuration() {
		return totalDuration;
	}

	private void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	private synchronized MediaPlayer getSoundPlayer() {
		return soundPlayer;
	}

	private synchronized void setSoundPlayer(MediaPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	private Map<String, Integer> getSounds() {
		return sounds;
	}

	private void setSounds(Map<String, Integer>  sounds) {
		this.sounds = sounds;
	}
	
	/*
	 * The following is an initial effort at calculating breathing rate
	 * of a patient by placing the device on their chest and using the
	 * acceleromter to determine upward and downward motion.
	 * 
	 * Determining the precision of movement with the current implementation
	 * was not successful. 
	 * 
	 * TODO Needs to be revisited
	 */
		
	/**
	 * Handle Accelerometer sensor event
	 */
	/*

	private void configureSensors() {
		if (getActivity() != null) {
			setSensorManager((SensorManager) getActivity().getSystemService(SupportingLifeBaseActivity.SENSOR_SERVICE));
	        setAccelerometer(getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	        getSensorManager().registerListener(this, getAccelerometer() , SensorManager.SENSOR_DELAY_NORMAL);
	        mAccelNoGrav = 0.0f;
	        mAccelWithGravCurrent = SensorManager.GRAVITY_EARTH;
	        mAccelWithGravLast = SensorManager.GRAVITY_EARTH;
	        setMotionDirection(BREATHE_OUT);
	        z.add(0.0f);
		}
	}
	 
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor slSensor = event.sensor;
		boolean directionChange = false;

		if (slSensor.getType() == Sensor.TYPE_ACCELEROMETER) {

	        	mGravity = event.values.clone();
		        float x = mGravity[0];
		        float y = mGravity[1];
		        z.add((mGravity[1])-SensorManager.GRAVITY_EARTH);

		        if (z.size() > 100) {
		        	z = z.subList(z.size() - 80, z.size());
		        }

		        mAccelWithGravLast = mAccelWithGravCurrent;
		        mAccelWithGravCurrent =  (float) java.lang.Math.sqrt(x * x + y * y + z.get(z.size()-1) * z.get(z.size()-1));
		        float delta = mAccelWithGravCurrent - mAccelWithGravLast;
		        mAccelNoGrav = mAccelNoGrav * 0.05f + delta; // Low-cut filter

	        	if(mAccelNoGrav > .1) { 
	        		if (getMotionDirection().equals(BREATHE_OUT)) {
	        			if (checkMotionDirection(BREATHE_IN)) {
		        			setMotionDirection(BREATHE_IN); // Upward motion
		        			directionChange = true;
	        			}
	        		}
	        		else if (getMotionDirection().equals(BREATHE_IN)){
	        			if (checkMotionDirection(BREATHE_OUT)) {
		        			setMotionDirection(BREATHE_OUT); // Downward motion
		        			directionChange = true;
	        			}
	        		}

	        		if (directionChange) {
	        			LoggerUtils.i(LOG_TAG, "Direction Change!! " + getMotionDirection());
	        			LoggerUtils.i(LOG_TAG, "===================");
	        			setDirectionChangeTimestamp(System.currentTimeMillis() + 400);
	        		}
        			setSensorTimestamp(System.currentTimeMillis());
	        	}
		}		
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	

	private boolean checkMotionDirection(String motion) {
		int sequenceNumber = 0;
		
		if (motion.equals(BREATHE_IN)) {
			for (int counter = z.size(); counter > 1; counter--) {
				if (z.get(counter - 1) > z.get(counter - 2)) {
					sequenceNumber++;
					if (sequenceNumber == 30) {
						return true;
					}
				}
			}
		}
		
		if (motion.equals(BREATHE_OUT)) {
			for (int counter = z.size(); counter > 1; counter--) {
				if (z.get(counter - 1) < z.get(counter - 2)) {
					sequenceNumber++;
					if (sequenceNumber == 30) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	*/
	
}