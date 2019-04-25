package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.UserEventDTO;
import lt.fivethreads.importing.EventImportService;
import lt.fivethreads.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class CsvEventImport extends CsvImport implements EventImportService {

    @Autowired
    EventService eventService;

    @Override
    protected void saveEntities(File file) {
        List<UserEventDTO> events = csvFileProcessor.loadObjectList(UserEventDTO.class, file);
        eventService.createEvents(events);
    }
}
