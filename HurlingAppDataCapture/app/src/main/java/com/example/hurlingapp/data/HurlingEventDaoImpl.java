package com.example.hurlingapp.data;

import com.example.hurlingapp.domain.HurlingEvent;
import java.util.ArrayList;
import java.util.List;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;

public class HurlingEventDaoImpl implements HurlingEventDao {

    private String[] allColumns = { HurlingEventTable.COLUMN_ID,
                                    HurlingEventTable.COLUMN_EVENT_TYPE,
                                    HurlingEventTable.COLUMN_ACTION_DETAIL,
                                    HurlingEventTable.COLUMN_OUTCOME,
                                    HurlingEventTable.COLUMN_START_X,
                                    HurlingEventTable.COLUMN_START_Y,
                                    HurlingEventTable.COLUMN_END_X,
                                    HurlingEventTable.COLUMN_END_Y,
                                    HurlingEventTable.COLUMN_EVENT_TIMESTAMP };

    private Context context;

    public HurlingEventDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public long addHurlingEvent(HurlingEvent hurlingEvent) {
        ContentValues values = new ContentValues();
        values.put(HurlingEventTable.COLUMN_EVENT_TYPE, hurlingEvent.getEventType());
        values.put(HurlingEventTable.COLUMN_ACTION_DETAIL, hurlingEvent.getActionDetail());
        values.put(HurlingEventTable.COLUMN_OUTCOME, hurlingEvent.getOutcome());
        values.put(HurlingEventTable.COLUMN_START_X, hurlingEvent.getStartLocation().x);
        values.put(HurlingEventTable.COLUMN_START_Y, hurlingEvent.getStartLocation().y);
        values.put(HurlingEventTable.COLUMN_END_X, hurlingEvent.getEndLocation().x);
        values.put(HurlingEventTable.COLUMN_END_Y, hurlingEvent.getEndLocation().y);
        values.put(HurlingEventTable.COLUMN_EVENT_TIMESTAMP, hurlingEvent.getTimestamp());

        SQLiteDatabase database = DatabaseHandler.getInstance(context).getWritableDatabase("password");

        long insertId = database.insert(HurlingEventTable.TABLE_HURLING_EVENT, null, values);
        database.close();
        return insertId;
    }

    @Override
    public List<HurlingEvent> getAllHurlingEvents() {
        List<HurlingEvent> hurlingEvents = new ArrayList<HurlingEvent>();

        SQLiteDatabase database = DatabaseHandler.getInstance(context).getReadableDatabase("password");

        Cursor cursor = database.query(HurlingEventTable.TABLE_HURLING_EVENT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HurlingEvent he = cursorToHurlingEvent(cursor);
            hurlingEvents.add(he);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return hurlingEvents;
    }

    private HurlingEvent cursorToHurlingEvent(Cursor cursor) {
        HurlingEvent hurlingEvent = new HurlingEvent();
        hurlingEvent.setId(cursor.getLong(0));
        hurlingEvent.setEventType(cursor.getString(1));
        hurlingEvent.setActionDetail(cursor.getString(2));
        hurlingEvent.setOutcome(cursor.getString(3));
        hurlingEvent.setStartLocation(new PointF(cursor.getFloat(4), cursor.getFloat(5)));
        hurlingEvent.setEndLocation(new PointF(cursor.getFloat(6), cursor.getFloat(7)));
        hurlingEvent.setTimestamp(cursor.getLong(8));
        return hurlingEvent;
    }
}
