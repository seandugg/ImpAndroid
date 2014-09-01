package ie.ucc.bis.supportinglife.dao;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "supportinglife.db";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase database) {		
		// create patient assessment table
		PatientAssessmentTable.onCreate(database);
		// create classification table
		ClassificationTable.onCreate(database);
		// create treatment table
		TreatmentTable.onCreate(database);
		// create analytics table
		AssessmentAnalyticsTable.onCreate(database);
		// create 'sensor vital signs' table
		SensorVitalSignsTable.onCreate(database);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// upgrade patient assessment table
		PatientAssessmentTable.onUpgrade(database, oldVersion, newVersion);
		// upgrade classification table
		ClassificationTable.onUpgrade(database, oldVersion, newVersion);
		// create treatment table
		TreatmentTable.onUpgrade(database, oldVersion, newVersion);
		// create analytics table
		AssessmentAnalyticsTable.onUpgrade(database, oldVersion, newVersion);
		// create 'sensor vital signs' table
		SensorVitalSignsTable.onUpgrade(database, oldVersion, newVersion);
	}
}
