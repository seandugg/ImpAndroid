package ie.ucc.bis.supportinglife.dao;

import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

/**
 * Class: AssessmentAnalyticsTable
 * 
 * This class represents the assessment analytics table in the device
 * database. It provides support for creating and upgrading the table.
 * 
 * @author TOSullivan
 */
public class AssessmentAnalyticsTable {

	// Table
	protected static final String TABLE_ASSESSMENT_ANALYTICS = "assessment_analytics";
	
	// 'AssessmentAnalytics' Table Columns
	protected static final String COLUMN_ID = "_id";
	protected static final String COLUMN_ASSESSMENT_ID = "assessment_id";
	protected static final String COLUMN_BREATH_COUNTER_USED_ID = "breath_counter_used_id";
	protected static final String COLUMN_BREATH_FULL_TIME_ASSESSMENT_ID = "breath_full_time_assessment_id";
	protected static final String COLUMN_LATITUDE_COORDINATE = "latitude_coordinate";
	protected static final String COLUMN_LONGITUDE_COORDINATE = "longitude_coordinate";
	
	// Assessment Analytics Table creation SQL statement
	private static final String DATABASE_ASSESSMENT_ANALYTICS_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_ASSESSMENT_ANALYTICS + "(" + COLUMN_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
									+ COLUMN_ASSESSMENT_ID							+ " TEXT, "
									+ COLUMN_BREATH_COUNTER_USED_ID 				+ " TEXT, "
									+ COLUMN_BREATH_FULL_TIME_ASSESSMENT_ID 		+ " TEXT, " 
									+ COLUMN_LATITUDE_COORDINATE 					+ " TEXT, "
									+ COLUMN_LONGITUDE_COORDINATE					+ " TEXT)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_ASSESSMENT_ANALYTICS_TABLE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DatabaseHandler.class.getName(),
				"Upgrading database classification table from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + DATABASE_ASSESSMENT_ANALYTICS_TABLE_CREATE);
		onCreate(database);
	}
	
}
