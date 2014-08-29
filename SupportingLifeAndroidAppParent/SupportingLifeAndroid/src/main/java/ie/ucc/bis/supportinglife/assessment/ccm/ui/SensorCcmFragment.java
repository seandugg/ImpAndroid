package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.CcmAssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.GeneralPatientDetailsCcmPage;
import ie.ucc.bis.supportinglife.assessment.ccm.model.SensorCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.listener.AssessmentWizardTextWatcher;
import ie.ucc.bis.supportinglife.sensor.BioHarnessConnectedListener;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import zephyr.android.BioHarnessBT.BTClient;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Responsible for UI fragment to display CCM 
 * 'zephyr' sensor assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class SensorCcmFragment extends Fragment implements FragmentLifecycle {
	
	private static final int SECOND = 1;
	private static final int ONE_SECOND_IN_MILLISECONDS = 1000;
	private static final int THIRTY_SECONDS = 30;
	
	private static final String CONNECTION_ESTABLISHED = "Connection to BioHarness successfully established";
	private static final String FAILED_TO_ESTABLISH_CONNECTION = "Failed to establish connection to BioHarness";
	private static final String RESETTING_CONNECTION = "Disconnecting BioHarness";
	private static final String DEFAULT_SENSOR_READING = "0";
		
	private static final String BLUETOOTH_CONNECTION_CONNECT_ICON_TYPEFACE_ASSET = "fonts/bluetooth-connect-flaticon.ttf";
	private static final String BLUETOOTH_CONNECTION_RESET_ICON_TYPEFACE_ASSET = "fonts/bluetooth-reset-flaticon.ttf";
	private static final String BLUETOOTH_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	private static final String BLUETOOTH_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
	private static final String BIO_HARNESS_TAG = "BH";
	public static final String SENSOR_READING_DURATION_SELECTION_KEY = "sensor_reading_duration_selection";
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.assessment.ccm.ui.SensorCcmFragment";

	private BluetoothAdapter bluetoothAdapter;
	private BTClient bluetoothClient;
	private BioHarnessConnectedListener bioHarnessConnectedListener;
	
    private SensorCcmPage sensorCcmPage;    
    private PageFragmentCallbacks pageFragmentCallbacks;
    private String pageKey;
    
    private TextView heartRateTextView;
    private TextView respirationRateTextView;
    private TextView skinTemperatureTextView;
    
    private CheckBox heartRateCheckBox;
    private CheckBox respirationRateCheckBox;
    private CheckBox skinTemperatureCheckBox;
    
    private ProgressBar progressTimer;
    private Thread timerThread;
    private boolean timerThreadRunning;
    private int progressStatus;
    private int totalDuration;
    private Handler timerHandler;
    private TextView progressTimerTextView;
    
    private Button connectSensorButton;
    private Button disconnectSensorButton;
    private Handler bioHarnessListenerHandler;
    
	private MediaPlayer soundPlayer;
	private Map<String, Integer> sounds;
    
	/* Sounds */
	private static final String TICK_SOUND = "Tick";
	private static final String FINISHED_NOTIFICATION_SOUND = "Finished";

    public static SensorCcmFragment create(String pageKey) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_KEY, pageKey);

        SensorCcmFragment fragment = new SensorCcmFragment();
        fragment.setArguments(args);
        return fragment;
    }

	/**
	 * Constructor
	 */
    public SensorCcmFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        setPageKey(args.getString(ARG_PAGE_KEY));
    }
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setSensorCcmPage((SensorCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
        
        View rootView = inflater.inflate(R.layout.fragment_ccm_page_sensor_assessment, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(getSensorCcmPage().getTitle());
        
		configureFonts(rootView);
		configureSounds();

        // heart rate
        setHeartRateTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_heart_rate)));
        getHeartRateTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.HEART_RATE_DATA_KEY));
        setHeartRateCheckBox(((CheckBox) rootView.findViewById(R.id.ccm_sensor_assessment_heart_rate_checkbox)));
        
        // respiration rate
        setRespirationRateTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_respiration_rate)));
        getRespirationRateTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.RESPIRATION_RATE_DATA_KEY));
        setRespirationRateCheckBox(((CheckBox) rootView.findViewById(R.id.ccm_sensor_assessment_respiration_rate_checkbox)));
        
        // skin temperature
        setSkinTemperatureTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_skin_temperature)));
        getSkinTemperatureTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.SKIN_TEMPERATURE_DATA_KEY));
        setSkinTemperatureCheckBox(((CheckBox) rootView.findViewById(R.id.ccm_sensor_assessment_skin_temperature_checkbox)));
        		
		configureProgressTimer(rootView);

		configureBioharnessCommunication();
		
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);

        return rootView;
    }

	/**
	 * Responsible for configuring the font typefaces for the
	 * customised UI elements on the Breath Counter interface
	 * 
	 * @param rootView
	 */
	private void configureFonts(View rootView) {
		// load the flaticon 'bluetooth connection reset' button font
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), BLUETOOTH_CONNECTION_RESET_ICON_TYPEFACE_ASSET);
		setDisconnectSensorButton((Button) rootView.findViewById(R.id.disconnect_sensor_button));
		getDisconnectSensorButton().setTypeface(font);
		
		// load the flaticon 'bluetooth establish connection' button font
		font = Typeface.createFromAsset(getActivity().getAssets(), BLUETOOTH_CONNECTION_CONNECT_ICON_TYPEFACE_ASSET);
		setConnectSensorButton((Button) rootView.findViewById(R.id.connect_sensor_button));
		getConnectSensorButton().setTypeface(font);		
	}
    
	/**
	 * Responsible for configuring the sounds associated
	 * with the Breath Counter
	 * 
	 */
	private void configureSounds() {	
		setSounds(new HashMap<String, Integer>());
		getSounds().put(TICK_SOUND, R.raw.second_tick);
		getSounds().put(FINISHED_NOTIFICATION_SOUND, R.raw.timer_finish);
	}
    
	/**
	 * Responsible for handling comms between the device 
	 * and the bioharness over bluetooth
	 */
	private void configureBioharnessCommunication() {	
		// configure handler for BioHarness Listener
		configureBioHarnessListenerHandler();
		
    	// reset progress timer
    	initialiseProgressTimer();
		
		// add click listener to the connect 'sensor' button
		getConnectSensorButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	LoggerUtils.i(LOG_TAG, "Connect Sensor Button: onClick -- Connect Sensor Button on CCM Sensor Assessment Fragment Clicked!");
            	
				// untick vital sign acceptance indicators
				signalVitalSignsAcceptance(false);

            	String bioHarnessMacID = null;    			
            	setBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());

            	Set<BluetoothDevice> pairedDevices = getBluetoothAdapter().getBondedDevices();

            	if (pairedDevices.size() > 0) {
            		for (BluetoothDevice device : pairedDevices) {
            			if (device.getName().startsWith(BIO_HARNESS_TAG)) {
            				BluetoothDevice btDevice = device;
            				bioHarnessMacID = btDevice.getAddress();
            				break;
            			}
            		}
            	}
            	
            	if (bioHarnessMacID != null) {
	    			setBluetoothClient(new BTClient(getBluetoothAdapter(), bioHarnessMacID));
	    			setBioHarnessConnectedListener(new BioHarnessConnectedListener(getBioHarnessListenerHandler()));
	    			getBluetoothClient().addConnectedEventListener(getBioHarnessConnectedListener());
	    			
	    			if(getBluetoothClient().IsConnected()) {
	    				
	    				// disable sensor connection button
	    				getConnectSensorButton().setEnabled(false);
	    				
	    				getBluetoothClient().start();
	    				
	            		// need to kick off timer
	            		setTimerThreadRunning(true);
	           			setTimerThread(new SensorReadingTimerThread());
	           			getTimerThread().start();
	    				
	    				LoggerUtils.i(LOG_TAG, String.format("Started BioHarness: %s", getBluetoothClient().getDevice().getName()));
	    				Crouton.makeText((SensorCcmFragment.this).getActivity(), CONNECTION_ESTABLISHED, Style.CONFIRM).show();  
	    			}
	    			else
	    			{
	    				LoggerUtils.i(LOG_TAG, String.format("Unable to connect: %s", getBluetoothClient().getDevice().getName()));
	    				Crouton.makeText((SensorCcmFragment.this).getActivity(), FAILED_TO_ESTABLISH_CONNECTION, Style.ALERT).show();
	    			}
            	}
            	else {
    				LoggerUtils.i(LOG_TAG, String.format("Unable to establish connection"));
    				Crouton.makeText((SensorCcmFragment.this).getActivity(), FAILED_TO_ESTABLISH_CONNECTION, Style.ALERT).show();           		
            	}
            }
		});
		
		// add click listener to the disconnect 'sensor' button
		getDisconnectSensorButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// untick vital sign acceptance indicators
				signalVitalSignsAcceptance(false);

				disconnectBioHarness();
			}
		});
	}
    
	/**
	 * Responsible for initiating intents to handle bluetooth pairing request
	 * between android device and bioharness
	 */
	private void configureBioHarnessBluetoothPairing() {
		// send message to android that we are going to initiate a pairing request
		IntentFilter pairingRequestFilter = new IntentFilter(BLUETOOTH_PAIRING_REQUEST);
		// register a new BTBroadcast receiver from the fragment's parent activity context with pairing request event
		((CcmAssessmentActivity) this.getActivity()).setBluetoothBroadcastReceiver(new BluetoothBroadcastReceiver());
		this.getActivity().registerReceiver(((CcmAssessmentActivity) this.getActivity()).getBluetoothBroadcastReceiver(), pairingRequestFilter);
		
		// register the BTBondReceiver in the application that the status of the receiver has changed to Paired
		IntentFilter pairedRequestFilter = new IntentFilter(BLUETOOTH_BOND_STATE_CHANGED);
		((CcmAssessmentActivity) this.getActivity()).setBluetoothBondReceiver(new BluetoothBondReceiver());
		this.getActivity().registerReceiver(((CcmAssessmentActivity) this.getActivity()).getBluetoothBondReceiver(), pairedRequestFilter);
	}

	/**
	 * Visual indicator to user of the duration for which the device will
	 * communicate with the Bioharness and read the sensor outputs
	 */
	private void configureProgressTimer(View rootView) {
		setTimerThreadRunning(false);
		setProgressTimer(((ProgressBar) rootView.findViewById(R.id.ccm_sensor_assessment_progress_timer)));
		setProgressTimerTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_progress_timer_text)));
		
		// configure max time for progress bar
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String sensorReadingDurationPreference = settings.getString(SupportingLifeBaseActivity.SENSOR_READING_DURATION_SELECTION_KEY, 
				String.valueOf(THIRTY_SECONDS));
		// increment by one second to facilitate timer count-down approach
	    setTotalDuration(Integer.valueOf(sensorReadingDurationPreference) + SECOND);
		getProgressTimer().setMax(getTotalDuration());
	
		setTimerHandler(new Handler());
	}

	private class SensorReadingTimerThread extends Thread implements Runnable {
		public void run() {

			while (isTimerThreadRunning() && getProgressStatus() < getTotalDuration()) {
				// Update the progress bar and display the 
				//current value in the text view
				getTimerHandler().post(new Runnable() {
					public void run() {
						setProgressStatus(getProgressStatus() + SECOND);
						getProgressTimer().setProgress(getProgressStatus());
						getProgressTimerTextView().setText(String.valueOf((getTotalDuration() - getProgressStatus()) + "s"));

						if (getActivity() != null) {						
							switch(getProgressStatus()) {
										default: 	setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(TICK_SOUND)));
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
				getTimerHandler().post(new Runnable() {
					public void run() {
						if (getActivity() != null) {
							setSoundPlayer(MediaPlayer.create(getActivity(), getSounds().get(FINISHED_NOTIFICATION_SOUND)));
							getSoundPlayer().setOnPreparedListener(new MediaPlayerListener(getSoundPlayer()));
							getSoundPlayer().setOnCompletionListener(new MediaPlayerListener(getSoundPlayer()));
							
							// indicate acceptance of transmitted vital sign values
							signalVitalSignsAcceptance(true);
						}
						setTimerThreadRunning(false);
					}		
				});
			}
		} // end run
	} // end inner class
    
	/**
	 * Signals to user whether vital sign reading are accepted
	 * i.e. by ticking/unticking associated vital sign checkboxes
	 * 
	 * @param acceptance
	 */
	private void signalVitalSignsAcceptance(boolean acceptance) {
		getHeartRateCheckBox().setChecked(acceptance);
		getRespirationRateCheckBox().setChecked(acceptance);
//		getSkinTemperatureCheckBox().setChecked(acceptance);
		
		if (!acceptance) {
			getHeartRateTextView().setText(DEFAULT_SENSOR_READING);
			getRespirationRateTextView().setText(DEFAULT_SENSOR_READING);
//			getSkinTemperatureTextView().setText(DEFAULT_SENSOR_READING);
		}
	}		
	
    /** 
     * Inner class to handle callback from the BioHarnessConnectedListener
     */
	private static final class HandlerExtension extends Handler {
		
	    private final WeakReference<SensorCcmFragment> sensorFragment; 

	    public HandlerExtension(SensorCcmFragment sensorFragment) {
	    	this.sensorFragment = new WeakReference<SensorCcmFragment>(sensorFragment);
	    }
	        
	    @Override
		public void handleMessage(Message msg)
		{
	    	SensorCcmFragment frag = getSensorFragment().get();
	    	if (frag != null) {
				switch (msg.what) {
					case BioHarnessConnectedListener.HEART_RATE_ID: 	
											String heartRateValue = msg.getData().getString(BioHarnessConnectedListener.HEART_RATE_KEY);
											frag.getHeartRateTextView().setText(heartRateValue);
											break;
	
					case BioHarnessConnectedListener.RESPIRATION_RATE_ID:
											String respirationRateValue = msg.getData().getString(BioHarnessConnectedListener.RESPIRATION_RATE_KEY);
											frag.getRespirationRateTextView().setText(respirationRateValue);
											break;
	
					case BioHarnessConnectedListener.SKIN_TEMPERATURE_ID:
											String skinTemperatureValue = msg.getData().getString(BioHarnessConnectedListener.CORE_TEMPERATURE_KEY);
											frag.getSkinTemperatureTextView().setText(skinTemperatureValue);
											break;
				} // end switch
	    	} // end if
		} // end handleMessage(..) method

		private WeakReference<SensorCcmFragment> getSensorFragment() {
			return sensorFragment;
		}
	} // end HandlerExtension inner class

	/** 
	 * Inner class to handle callback from the pairing request event
	 */ 
	private class BluetoothBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			
			LoggerUtils.i(LOG_TAG, String.format("Bluetooth Intent: %s", intent.getAction()));
			LoggerUtils.i(LOG_TAG, String.format("Bluetooth Intent: %s", bundle.get("android.bluetooth.device.extra.DEVICE").toString()));
			LoggerUtils.i(LOG_TAG, String.format("Bluetooth Intent: %s", bundle.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString()));

			try {
				BluetoothDevice device = getBluetoothAdapter().getRemoteDevice(bundle.get("android.bluetooth.device.extra.DEVICE").toString());
				Method reflectiveMethod = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
				byte[] pin = (byte[]) reflectiveMethod.invoke(device, "1234");
				reflectiveMethod = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
				Object result = reflectiveMethod.invoke(device, pin);
				LoggerUtils.i(LOG_TAG, String.format("Bluetooth Test: %s", result.toString()));
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	} // end inner class
	
	/** 
	 * Inner class to handle callback from the paired request event
	 */    
	private class BluetoothBondReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			BluetoothDevice device = getBluetoothAdapter().getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			LoggerUtils.i(LOG_TAG, String.format("Bond State: %s", "BOND_STATED = " + device.getBondState()));
		}
	} // end inner class
    	
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
		
    private void configureBioHarnessListenerHandler() {
		setBioHarnessListenerHandler(new HandlerExtension(this));
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        } 
        setPageFragmentCallbacks((PageFragmentCallbacks) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setPageFragmentCallbacks(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // heart rate
        getHeartRateTextView().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getSensorCcmPage(), 
        				SensorCcmPage.HEART_RATE_DATA_KEY));
        
        // respiratory rate
        getRespirationRateTextView().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getSensorCcmPage(), 
        				SensorCcmPage.RESPIRATION_RATE_DATA_KEY));
        
        // core body temperature
       getSkinTemperatureTextView().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getSensorCcmPage(), 
        				SensorCcmPage.SKIN_TEMPERATURE_DATA_KEY));
    }
    
    @Override
    public void onPause() {
    	// remove connection to BioHarness
    	disconnectBioHarness();
    	
       	if (((CcmAssessmentActivity) this.getActivity()).getBluetoothBroadcastReceiver() != null) {
       		this.getActivity().unregisterReceiver(((CcmAssessmentActivity) this.getActivity()).getBluetoothBroadcastReceiver());
        }

       	if (((CcmAssessmentActivity) this.getActivity()).getBluetoothBondReceiver() != null) {
       		this.getActivity().unregisterReceiver(((CcmAssessmentActivity) this.getActivity()).getBluetoothBondReceiver());
       	}
       	
       	super.onPause();
    }
	
    @Override
    public void onResume() {   	
        super.onResume();
   
		// initiate intents to handle bluetooth pairing request
		// between android device and bioharness
        configureBioHarnessBluetoothPairing();
        
    	// put progress timer back to starting point
    	initialiseProgressTimer();
    }
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { 
        	// put progress timer back to starting point
        	initialiseProgressTimer();
        }
        else {  
        	// remove connection to BioHarness
        	disconnectBioHarness();
        }
    }
    
    
    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {   	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (assessmentPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(assessmentPage, GeneralPatientDetailsCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(assessmentPage,
					GeneralPatientDetailsCcmPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) assessmentPage.getPageData().getSerializable(GeneralPatientDetailsCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) assessmentPage.getPageData().getSerializable(GeneralPatientDetailsCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (assessmentPage != null) {			
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(assessmentPage, GeneralPatientDetailsCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
    
	/**
	 * Responsible for disconnecting/disabling the connection from the SL App to the BioHarness
	 */
	private void disconnectBioHarness() {		
		if (getBluetoothClient() != null) {
			LoggerUtils.i(LOG_TAG, String.format("Disconnecting from BioHarness: %s", getBluetoothClient().getDevice().getName()));
			Crouton.makeText((SensorCcmFragment.this).getActivity(), RESETTING_CONNECTION, Style.INFO).show();
			
			setTimerThreadRunning(false);
			if (getTimerThread() != null) {
				getTimerThread().interrupt();
			}
			
			// reset progress timer
			initialiseProgressTimer();
			
			// enable sensor connection button
			getConnectSensorButton().setEnabled(true);
			
			// disconnect listener from acting on received messages	
			getBluetoothClient().removeConnectedEventListener(getBioHarnessConnectedListener());
			// close the communication with the device & throw an exception if failure
			if (getBluetoothClient().IsConnected()) {
				LoggerUtils.i(LOG_TAG, "Closing bluetooth client connection");
				getBluetoothClient().Close();
			}
			
			setBluetoothClient(null);
		}
	}
    
	/**
	 * Responsible for initialising/resetting the progress timer 
	 */
	private void initialiseProgressTimer() {
		setProgressStatus(SECOND);
		if (getProgressTimer() != null) {
			getProgressTimer().setProgress(getProgressStatus());
		}
		if (getProgressTimerTextView() != null) {
			getProgressTimerTextView().setText(String.valueOf((getTotalDuration() - getProgressStatus()) + "s"));
		}
	}
    
	private BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}

	private void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		this.bluetoothAdapter = bluetoothAdapter;
	}

	private BTClient getBluetoothClient() {
		return bluetoothClient;
	}

	private void setBluetoothClient(BTClient bluetoothClient) {
		this.bluetoothClient = bluetoothClient;
	}

	private BioHarnessConnectedListener getBioHarnessConnectedListener() {
		return bioHarnessConnectedListener;
	}

	private void setBioHarnessConnectedListener(BioHarnessConnectedListener bioHarnessConnectedListener) {
		this.bioHarnessConnectedListener = bioHarnessConnectedListener;
	}

	private SensorCcmPage getSensorCcmPage() {
		return sensorCcmPage;
	}

	private void setSensorCcmPage(SensorCcmPage sensorCcmPage) {
		this.sensorCcmPage = sensorCcmPage;
	}

	private PageFragmentCallbacks getPageFragmentCallbacks() {
		return pageFragmentCallbacks;
	}

	private void setPageFragmentCallbacks(PageFragmentCallbacks pageFragmentCallbacks) {
		this.pageFragmentCallbacks = pageFragmentCallbacks;
	}

	private String getPageKey() {
		return pageKey;
	}

	private void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	private TextView getHeartRateTextView() {
		return heartRateTextView;
	}

	private void setHeartRateTextView(TextView heartRateTextView) {
		this.heartRateTextView = heartRateTextView;
	}

	private TextView getRespirationRateTextView() {
		return respirationRateTextView;
	}

	private void setRespirationRateTextView(TextView respirationRateTextView) {
		this.respirationRateTextView = respirationRateTextView;
	}

	private TextView getSkinTemperatureTextView() {
		return skinTemperatureTextView;
	}

	private void setSkinTemperatureTextView(TextView skinTemperatureTextView) {
		this.skinTemperatureTextView = skinTemperatureTextView;
	}

	private CheckBox getHeartRateCheckBox() {
		return heartRateCheckBox;
	}

	private void setHeartRateCheckBox(CheckBox heartRateCheckBox) {
		this.heartRateCheckBox = heartRateCheckBox;
	}

	private CheckBox getRespirationRateCheckBox() {
		return respirationRateCheckBox;
	}

	private void setRespirationRateCheckBox(CheckBox respirationRateCheckBox) {
		this.respirationRateCheckBox = respirationRateCheckBox;
	}

	private CheckBox getSkinTemperatureCheckBox() {
		return skinTemperatureCheckBox;
	}

	private void setSkinTemperatureCheckBox(CheckBox skinTemperatureCheckBox) {
		this.skinTemperatureCheckBox = skinTemperatureCheckBox;
	}

	private ProgressBar getProgressTimer() {
		return progressTimer;
	}

	private void setProgressTimer(ProgressBar progressTimer) {
		this.progressTimer = progressTimer;
	}

	private Thread getTimerThread() {
		return timerThread;
	}

	private void setTimerThread(Thread timerThread) {
		this.timerThread = timerThread;
	}

	private boolean isTimerThreadRunning() {
		return timerThreadRunning;
	}

	private void setTimerThreadRunning(boolean timerThreadRunning) {
		this.timerThreadRunning = timerThreadRunning;
	}

	private int getProgressStatus() {
		return progressStatus;
	}

	private void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}

	private int getTotalDuration() {
		return totalDuration;
	}

	private void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	private Handler getTimerHandler() {
		return timerHandler;
	}

	private void setTimerHandler(Handler timerHandler) {
		this.timerHandler = timerHandler;
	}

	private TextView getProgressTimerTextView() {
		return progressTimerTextView;
	}

	private void setProgressTimerTextView(TextView progressTimerTextView) {
		this.progressTimerTextView = progressTimerTextView;
	}

	private Button getConnectSensorButton() {
		return connectSensorButton;
	}

	private void setConnectSensorButton(Button connectSensorButton) {
		this.connectSensorButton = connectSensorButton;
	}

	private Button getDisconnectSensorButton() {
		return disconnectSensorButton;
	}

	private void setDisconnectSensorButton(Button disconnectSensorButton) {
		this.disconnectSensorButton = disconnectSensorButton;
	}

	private Handler getBioHarnessListenerHandler() {
		return bioHarnessListenerHandler;
	}

	private void setBioHarnessListenerHandler(Handler bioHarnessListenerHandler) {
		this.bioHarnessListenerHandler = bioHarnessListenerHandler;
	}

	private MediaPlayer getSoundPlayer() {
		return soundPlayer;
	}

	private void setSoundPlayer(MediaPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	private Map<String, Integer> getSounds() {
		return sounds;
	}

	private void setSounds(Map<String, Integer> sounds) {
		this.sounds = sounds;
	}
}
