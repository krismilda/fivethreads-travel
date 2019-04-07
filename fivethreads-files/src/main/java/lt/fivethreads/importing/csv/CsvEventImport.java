package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.exception.importing.EventImportFailedException;
import lt.fivethreads.importing.file.FileConverter;
import lt.fivethreads.importing.EventImportService;
import lt.fivethreads.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class CsvEventImport implements EventImportService {
    @Autowired
    CsvFileProcessor csvFileProcessor;

    @Autowired
    FileConverter fileConverter;

    @Autowired
    EventService eventService;

    @Override
    public void importEvents(MultipartFile multipartFile) {
        File file = null;
        try {
            file = fileConverter.convert(multipartFile);
        } catch (IOException e) {
            throw new EventImportFailedException();
        }

        List<EventDTO> events = csvFileProcessor.loadObjectList(EventDTO.class, file);
        eventService.createEvents(events);

        file.delete();
    }
}
