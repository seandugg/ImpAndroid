package com.example.hurlingapp.util;

import com.example.hurlingapp.domain.HurlingEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import android.os.Environment;
import android.util.Log;

public class CsvExporter {

    private static final String CSV_HEADER = "id,eventType,actionDetail,outcome,startX,startY,endX,endY,timestamp\n";

    public static File exportHurlingEvents(List<HurlingEvent> events) {
        try {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "HurlingApp/Exports");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "hurling_events_" + System.currentTimeMillis() + ".csv");
            FileWriter writer = new FileWriter(file);

            writer.append(CSV_HEADER);

            for (HurlingEvent event : events) {
                writer.append(String.valueOf(event.getId()));
                writer.append(',');
                writer.append(event.getEventType());
                writer.append(',');
                writer.append(event.getActionDetail());
                writer.append(',');
                writer.append(event.getOutcome());
                writer.append(',');
                writer.append(String.valueOf(event.getStartLocation().x));
                writer.append(',');
                writer.append(String.valueOf(event.getStartLocation().y));
                writer.append(',');
                writer.append(String.valueOf(event.getEndLocation().x));
                writer.append(',');
                writer.append(String.valueOf(event.getEndLocation().y));
                writer.append(',');
                writer.append(String.valueOf(event.getTimestamp()));
                writer.append('\n');
            }

            writer.flush();
            writer.close();
            return file;
        } catch (IOException e) {
            Log.e("CsvExporter", "Error exporting to CSV", e);
            return null;
        }
    }
}
