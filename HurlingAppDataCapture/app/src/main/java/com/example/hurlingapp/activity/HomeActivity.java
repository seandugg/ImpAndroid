package com.example.hurlingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.hurlingapp.R;
import com.example.hurlingapp.data.HurlingEventDao;
import com.example.hurlingapp.data.HurlingEventDaoImpl;
import com.example.hurlingapp.domain.HurlingEvent;
import com.example.hurlingapp.util.CsvExporter;
import java.io.File;
import java.util.List;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startButton = findViewById(R.id.start_capture_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HurlingCaptureActivity.class));
            }
        });

        Button exportButton = findViewById(R.id.export_csv_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HurlingEventDao dao = new HurlingEventDaoImpl(HomeActivity.this);
                List<HurlingEvent> events = dao.getAllHurlingEvents();
                File csvFile = CsvExporter.exportHurlingEvents(events);
                if (csvFile != null) {
                    Toast.makeText(HomeActivity.this, "Exported to " + csvFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
