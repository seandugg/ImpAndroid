package ie.ucc.bis.supportinglife.sensor;

import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;
import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ConnectListenerImpl;
import zephyr.android.BioHarnessBT.ConnectedEvent;
import zephyr.android.BioHarnessBT.PacketTypeRequest;
import zephyr.android.BioHarnessBT.ZephyrPacketArgs;
import zephyr.android.BioHarnessBT.ZephyrPacketEvent;
import zephyr.android.BioHarnessBT.ZephyrPacketListener;
import zephyr.android.BioHarnessBT.ZephyrProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BioHarnessConnectedListener extends ConnectListenerImpl {

	private static final int GP_MSG_ID = 0x20;
	private static final int BREATHING_MSG_ID = 0x21;
	private static final int ECG_MSG_ID = 0x22;
	private static final int RtoR_MSG_ID = 0x24;
	private static final int ACCEL_100mg_MSG_ID = 0x2A;
	private static final int SUMMARY_MSG_ID = 0x2B;	
	public static final int HEART_RATE_ID = 0x100;
	public static final int RESPIRATION_RATE_ID = 0x101;
	public static final int SKIN_TEMPERATURE_ID = 0x102;
	
	public static final String HEART_RATE_KEY = "HEART_RATE";
	public static final String RESPIRATION_RATE_KEY = "RESPIRATION_RATE";
	public static final String CORE_TEMPERATURE_KEY = "CORE_TEMPERATURE";
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.sensor.BioHarnessConnectedListener";

	private Handler handler; 
	/*Creating the different Objects for different types of Packets*/
	private GeneralPacketInfo generalPacketInfo;
	private ECGPacketInfo ecgPacketInfo;
	private BreathingPacketInfo breathingPacketInfo;
	private RtoRPacketInfo rToRPacketInfo;
	private AccelerometerPacketInfo accelerometerPacketInfo;
	private SummaryPacketInfo summaryPacketInfo;
	private PacketTypeRequest packetTypeRequest;

	public BioHarnessConnectedListener(Handler handler) {
		super(handler, null);
		setHandler(handler);
	}


	public void Connected(ConnectedEvent<BTClient> eventArgs) {
		LoggerUtils.i(LOG_TAG, String.format("Connected to BioHarness: %s", eventArgs.getSource().getDevice().getName()));

		configurePacketTypeRequest();
		//Creates a new ZephyrProtocol object and passes it the BTComms object
		ZephyrProtocol zephyrProtocol = new ZephyrProtocol(eventArgs.getSource().getComms(), getPacketTypeRequest());

		zephyrProtocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
			public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
				ZephyrPacketArgs msg = eventArgs.getPacket();
				// byte crcFailStatus = msg.getCRCStatus();
				// byte rcvdBytes = msg.getNumRvcdBytes();
				int msgId = msg.getMsgID();				
				byte [] msgData = msg.getBytes();
				
				switch (msgId)
				{
					case GP_MSG_ID:		//*************** HEART RATE ************************
										int heartRate =  getGeneralPacketInfo().GetHeartRate(msgData);
										Message vitalSignMessage = getHandler().obtainMessage(HEART_RATE_ID);
										Bundle vitalSignBundle = new Bundle();
										vitalSignBundle.putString(HEART_RATE_KEY, String.valueOf(heartRate));
										vitalSignMessage.setData(vitalSignBundle);
										getHandler().sendMessage(vitalSignMessage);
										LoggerUtils.i(LOG_TAG, String.format("Heart Rate is %d", heartRate));

										//*************** RESPIRATION RATE ************************
										double respirationRate = getGeneralPacketInfo().GetRespirationRate(msgData);
										vitalSignMessage = getHandler().obtainMessage(RESPIRATION_RATE_ID);
										vitalSignBundle.putString(RESPIRATION_RATE_KEY, String.valueOf(respirationRate));
										vitalSignMessage.setData(vitalSignBundle);
										getHandler().sendMessage(vitalSignMessage);
										LoggerUtils.i(LOG_TAG, String.format("Respiration Rate is %.4f", respirationRate));
										break;
										
				case BREATHING_MSG_ID:	LoggerUtils.i(LOG_TAG, String.format("Breathing Packet Sequence Number is %d", getBreathingPacketInfo().GetSeqNum(msgData)));
										break;
										
				case ECG_MSG_ID:		LoggerUtils.i(LOG_TAG, String.format("ECG Packet Sequence Number is %d", getEcgPacketInfo().GetSeqNum(msgData)));
										break;
										
				case RtoR_MSG_ID:		LoggerUtils.i(LOG_TAG, String.format("R to R Packet Sequence Number is %d", getRToRPacketInfo().GetSeqNum(msgData)));
										break;
										
				case ACCEL_100mg_MSG_ID: LoggerUtils.i(LOG_TAG, String.format("Accelerometry Packet Sequence Number is %d", getAccelerometerPacketInfo().GetSeqNum(msgData)));
										 break;
										 
				case SUMMARY_MSG_ID:	LoggerUtils.i(LOG_TAG, String.format("Summary Packet Sequence Number is %d", getSummaryPacketInfo().GetSeqNum(msgData)));
										//***************Displaying the Subject Core Temperature*******************************
//										double coreTemperature = getSummaryPacketInfo().Get
//										vitalSignMessage = getHandler().obtainMessage(SKIN_TEMPERATURE_ID);
//										vitalSignBundle.putString(CORE_TEMPERATURE_KEY, String.valueOf(coreTemperature));
//										vitalSignMessage.setData(vitalSignBundle);
//										getHandler().sendMessage(vitalSignMessage);
//										LoggerUtils.i(LOG_TAG, String.format("Core Temperature is %.4f", coreTemperature));
//										break;
				} // end switch
			} // end ReceivedPacket(..)
		}); // end ZephyrPacketListener inner class
	} // end Connected()

	/**
	 * Responsible for configuring the Packet Type request
	 * i.e. enabling or disabling the different Packet types
	 */	
	private void configurePacketTypeRequest() {
		// initialise packets
		setGeneralPacketInfo(new GeneralPacketInfo());
		setEcgPacketInfo(new ECGPacketInfo());
		setBreathingPacketInfo(new BreathingPacketInfo());
		setRToRPacketInfo(new RtoRPacketInfo());
		setAccelerometerPacketInfo(new AccelerometerPacketInfo());
		setSummaryPacketInfo(new SummaryPacketInfo());
		setPacketTypeRequest(new PacketTypeRequest());
		
		getPacketTypeRequest().EnableGP(true);
		getPacketTypeRequest().EnableBreathing(true);
		getPacketTypeRequest().EnableSummary(true);
		getPacketTypeRequest().EnableLogging(true);
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public GeneralPacketInfo getGeneralPacketInfo() {
		return generalPacketInfo;
	}

	public void setGeneralPacketInfo(GeneralPacketInfo generalPacketInfo) {
		this.generalPacketInfo = generalPacketInfo;
	}

	public ECGPacketInfo getEcgPacketInfo() {
		return ecgPacketInfo;
	}

	public void setEcgPacketInfo(ECGPacketInfo ecgPacketInfo) {
		this.ecgPacketInfo = ecgPacketInfo;
	}

	public BreathingPacketInfo getBreathingPacketInfo() {
		return breathingPacketInfo;
	}

	public void setBreathingPacketInfo(BreathingPacketInfo breathingPacketInfo) {
		this.breathingPacketInfo = breathingPacketInfo;
	}

	public RtoRPacketInfo getRToRPacketInfo() {
		return rToRPacketInfo;
	}

	public void setRToRPacketInfo(RtoRPacketInfo rToRPacketInfo) {
		this.rToRPacketInfo = rToRPacketInfo;
	}

	public AccelerometerPacketInfo getAccelerometerPacketInfo() {
		return accelerometerPacketInfo;
	}

	public void setAccelerometerPacketInfo(
			AccelerometerPacketInfo accelerometerPacketInfo) {
		this.accelerometerPacketInfo = accelerometerPacketInfo;
	}

	public SummaryPacketInfo getSummaryPacketInfo() {
		return summaryPacketInfo;
	}

	public void setSummaryPacketInfo(SummaryPacketInfo summaryPacketInfo) {
		this.summaryPacketInfo = summaryPacketInfo;
	}

	public PacketTypeRequest getPacketTypeRequest() {
		return packetTypeRequest;
	}

	public void setPacketTypeRequest(PacketTypeRequest packetTypeRequest) {
		this.packetTypeRequest = packetTypeRequest;
	}
}
