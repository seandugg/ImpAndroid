package com.example.hurlingapp.data;

import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

public class HurlingEventTable {

    // Table
    public static final String TABLE_HURLING_EVENT = "hurling_event";

    // 'HurlingEvent' Table Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENT_TYPE = "event_type";
    public static final String COLUMN_ACTION_DETAIL = "action_detail";
    public static final String COLUMN_OUTCOME = "outcome";
    public static final String COLUMN_START_X = "start_x";
    public static final String COLUMN_START_Y = "start_y";
    public static final String COLUMN_END_X = "end_x";
    public static final String COLUMN_END_Y = "end_y";
    public static final String COLUMN_EVENT_TIMESTAMP = "event_timestamp";

    // Hurling Event Table creation SQL statement
    private static final String DATABASE_HURLING_EVENT_TABLE_CREATE = "CREATE TABLE "
            + TABLE_HURLING_EVENT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EVENT_TYPE + " TEXT, "
            + COLUMN_ACTION_DETAIL + " TEXT, "
            + COLUMN_OUTCOME + " TEXT, "
            + COLUMN_START_X + " REAL, "
            + COLUMN_START_Y + " REAL, "
            + COLUMN_END_X + " REAL, "
            + COLUMN_END_Y + " REAL, "
            + COLUMN_EVENT_TIMESTAMP + " INTEGER);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_HURLING_EVENT_TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w("DatabaseHandler",
                "Upgrading database hurling event table from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HURLING_EVENT);
        onCreate(database);
    }
}
