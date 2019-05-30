package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.exception.OfficeDoesNotExist;
import lt.fivethreads.importing.UserImportService;
import lt.fivethreads.services.OfficeService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;

@Component
public class CsvUserImport extends CsvImport implements UserImportService {

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;

    @Override
    protected void saveEntities(File file) {
        List<ExtendedUserDTO> users = csvFileProcessor.loadObjectList(ExtendedUserDTO.class, file);
        for (ExtendedUserDTO user:users
             ) {
            if(!officeService.getAllOffices().stream().anyMatch(e->e.getName().equals(user.getOfficeName()))){
                throw new OfficeDoesNotExist("Office does not exits.");
            }
            ;
        }
        userService.createUsers(users);
    }
}
