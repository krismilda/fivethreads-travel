package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.UserEventDTO;
import lt.fivethreads.exception.file.UserIDNotExists;
import lt.fivethreads.importing.EventImportService;
import lt.fivethreads.services.EventService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class CsvEventImport extends CsvImport implements EventImportService {

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Override
    protected void saveEntities(File file) {
        List<UserEventDTO> events = csvFileProcessor.loadObjectList(UserEventDTO.class, file);
        for (UserEventDTO usereventDTO:events
             ) {
            if(!userService.checkIfEmailExists(usereventDTO.getUserEmail())){
                throw new UserIDNotExists("User does not exist.");
            }
        }
        eventService.createEvents(events);
    }
}
