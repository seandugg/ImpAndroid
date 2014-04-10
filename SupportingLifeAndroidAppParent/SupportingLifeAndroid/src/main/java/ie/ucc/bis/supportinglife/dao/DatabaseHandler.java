package ie.ucc.bis.supportinglife.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// upgrade patient assessment table
		PatientAssessmentTable.onUpgrade(database, oldVersion, newVersion);
		// upgrade classification table
		ClassificationTable.onUpgrade(database, oldVersion, newVersion);
		// create treatment table
		TreatmentTable.onUpgrade(database, oldVersion, newVersion);
	}
}
