package ie.ucc.bis.supportinglife.dao;

import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

/**
 * Class: SensorVitalSignsTable
 * 
 * This class represents the assessment sensor vital sign readings table 
 * in the device database. It provides support for creating and 
 * upgrading the table.
 * 
 * @author TOSullivan
 */
public class SensorVitalSignsTable {

	// Table
	protected static final String TABLE_SENSOR_VITAL_SIGNS = "assessment_sensor";
	
	// 'AssessmentSensor' Table Columns
	protected static final String COLUMN_ID = "_id";
	protected static final String COLUMN_ASSESSMENT_ID = "assessment_id";
	protected static final String COLUMN_HEART_RATE = "heart_rate";
	protected static final String COLUMN_RESPIRATORY_RATE = "respiratory_rate";
	protected static final String COLUMN_BODY_TEMPERATURE = "body_temperature";
	
	// Assessment Sensor Table creation SQL statement
	private static final String DATABASE_ASSESSMENT_SENSOR_VITAL_SIGNS_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_SENSOR_VITAL_SIGNS + "(" + COLUMN_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
									+ COLUMN_ASSESSMENT_ID							+ " TEXT, "
									+ COLUMN_HEART_RATE 							+ " TEXT, "
									+ COLUMN_RESPIRATORY_RATE 						+ " TEXT, " 
									+ COLUMN_BODY_TEMPERATURE 						+ " TEXT)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_ASSESSMENT_SENSOR_VITAL_SIGNS_TABLE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DatabaseHandler.class.getName(),
				"Upgrading database sensor vital signs table from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + DATABASE_ASSESSMENT_SENSOR_VITAL_SIGNS_TABLE_CREATE);
		onCreate(database);
	}
	
}
