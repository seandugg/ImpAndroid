package com.example.hurlingapp.data;

import com.example.hurlingapp.domain.HurlingEvent;
import java.util.List;

public interface HurlingEventDao {
    long addHurlingEvent(HurlingEvent hurlingEvent);
    List<HurlingEvent> getAllHurlingEvents();
    // other methods as needed
}
