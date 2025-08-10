package ie.ucc.bis.supportinglife.helper;

import ie.ucc.bis.supportinglife.assessment.hurling.domain.HurlingEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import android.os.Environment;

public class CsvExporter {

    private static final String CSV_HEADER = "id,eventType,actionDetail,outcome,startX,startY,endX,endY,timestamp\n";

    public static File exportHurlingEvents(List<HurlingEvent> events) throws IOException {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "SupportingLife/Exports");
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
    }
}
