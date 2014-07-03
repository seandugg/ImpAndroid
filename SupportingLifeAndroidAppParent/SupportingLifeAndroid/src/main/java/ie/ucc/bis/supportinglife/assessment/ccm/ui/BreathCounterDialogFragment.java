package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;

public class BreathCounterDialogFragment extends DialogFragment implements OnCloseListener, SensorEventListener {
	
	private static final String LOG_TAG = " ie.ucc.bis.supportinglife.assessment.ccm.ui.BreathCounterDialogFragment";
	private static final String BREATHE_COUNTER_RESET_ICON_TYPEFACE_ASSET = "fonts/breathe-reset-flaticon.ttf";
	private static final String BREATHE_COUNTER_INCREMENT_ICON_TYPEFACE_ASSET = "fonts/breathe-increment-flaticon.ttf";
	private static final String BREATH_COUNT_TYPEFACE_ASSET = "fonts/RobotoCondensed-Light.ttf";
	
	private static final int ZERO_BREATHS = 0;
	private static final int SECOND = 1;
	private static final int THIRTY_SECONDS = 30;
	private static final int ONE_MINUTE_IN_SECONDS = 60;
	private static final int ONE_SECOND_IN_MILLISECONDS = 1000;
	private static final String BREATHS = "Breaths";
	private static final String BREATH = "Breath";
	private static final String SECONDS = "Seconds";
	
	/* Sounds */
	private static final String TICK_SOUND = "Tick";
	private static final String MIDWAY_NOTIFCATION_SOUND = "Midway";
	private static final String FINISHED_NOTIFICATION_SOUND = "Finished";
	
	/* Motion Direction */
	private static final String BREATHE_IN = "BREATHE_IN";
	private static final String BREATHE_OUT = "BREATHE_OUT";
	
	private SensorManager sensorManager;
    private Sensor accelerometer;
    private long sensorTimestamp;
    private float axisZPosition;
    private String motionDirection;
    
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
		configureSensors();
		
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
            	
            	// *** sensor handling ***
            	setSensorTimestamp(System.currentTimeMillis());
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
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		getSensorManager().unregisterListener(this);
		super.onDismiss(dialog);
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
		// TODO - this should be read from preferences with potential
		// 		  values being 15, 30, 45, 60
		getProgressBar().setMax(ONE_MINUTE_IN_SECONDS);
		
		setProgressTimer((TextView) customDialog.findViewById(R.id.ccm_breath_counter_assessment_progress_timer_status));
		
		setHandler(new Handler());
	}
	
	/**
	 * Responsible for configuring the accelerometer sensor
	 * 
	 * @param customDialog
	 */
	private void configureSensors() {
		if (getActivity() != null) {
			setSensorManager((SensorManager) getActivity().getSystemService(SupportingLifeBaseActivity.SENSOR_SERVICE));
	        setAccelerometer(getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	        getSensorManager().registerListener(this, getAccelerometer() , SensorManager.SENSOR_DELAY_NORMAL);
		}
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
							switch(getProgressStatus()) {
								case THIRTY_SECONDS: setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(MIDWAY_NOTIFCATION_SOUND)));
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
			if (getProgressStatus() == ONE_MINUTE_IN_SECONDS) {
				getHandler().post(new Runnable() {
					public void run() {
						if (getActivity() != null) {
							setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(FINISHED_NOTIFICATION_SOUND)));
							getSoundPlayer().setOnPreparedListener(new MediaPlayerListener(getSoundPlayer()));
							getSoundPlayer().setOnCompletionListener(new MediaPlayerListener(getSoundPlayer()));
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
	public boolean onClose() {
    	setTimerThreadRunning(false);
    	if (getTimerThread() != null) {
    		getTimerThread().interrupt();
    	}
    	
		if (getSoundPlayer() != null) {
        	// stop ticking sound
        	getSoundPlayer().reset();
        	getSoundPlayer().release();
		}
		return false;
	}
	
	/**
	 * Handle Accelerometer sensor event
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor slSensor = event.sensor;
		boolean directionChange = false;

		if (slSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// float x = event.values[0];
			// float y = event.values[1];
			float z = event.values[2];

			long currentTime = System.currentTimeMillis();

			if ((currentTime - getSensorTimestamp()) > 500) {
				float zPosChange = getAxisZPosition() - z;
				
				if (getMotionDirection() == null) {
					if (zPosChange > 4) {
						setMotionDirection(BREATHE_IN); // Upward motion
					}
					else if (zPosChange < -4){
						setMotionDirection(BREATHE_OUT); // Downward motion
					}
				}
				else {
					if (zPosChange < -4 && getMotionDirection().equals(BREATHE_IN)) {
						setMotionDirection(BREATHE_OUT); // Upward motion
						directionChange = true;
					}
					else if (zPosChange > 4 && getMotionDirection().equals(BREATHE_OUT)){
						setMotionDirection(BREATHE_IN); // Downward motion
						directionChange = true;
					}
				}

				if (directionChange) {
					LoggerUtils.i(LOG_TAG, "Z Original: " + getAxisZPosition());
					LoggerUtils.i(LOG_TAG, "Z New: " + z);
					LoggerUtils.i(LOG_TAG, "Z Change: " + zPosChange);
					LoggerUtils.i(LOG_TAG, "Direction Change!! " + getMotionDirection());
					LoggerUtils.i(LOG_TAG, "===================");
				}

				setAxisZPosition(z);
				setSensorTimestamp(currentTime);
			}
		}		
	}

	/**
	 * SensorEventListener event
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	
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
	
	public SensorManager getSensorManager() {
		return sensorManager;
	}

	public void setSensorManager(SensorManager sensorManager) {
		this.sensorManager = sensorManager;
	}

	public Sensor getAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(Sensor accelerometer) {
		this.accelerometer = accelerometer;
	}

	public long getSensorTimestamp() {
		return sensorTimestamp;
	}

	public void setSensorTimestamp(long sensorTimestamp) {
		this.sensorTimestamp = sensorTimestamp;
	}

	public float getAxisZPosition() {
		return axisZPosition;
	}

	public void setAxisZPosition(float axisZPosition) {
		this.axisZPosition = axisZPosition;
	}

	public String getMotionDirection() {
		return motionDirection;
	}

	public void setMotionDirection(String motionDirection) {
		this.motionDirection = motionDirection;
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