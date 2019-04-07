package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.exception.importing.UserImportFailedException;
import lt.fivethreads.importing.file.FileConverter;
import lt.fivethreads.importing.UserImportService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class CsvUserImport implements UserImportService {

    @Autowired
    CsvFileProcessor csvFileProcessor;

    @Autowired
    FileConverter fileConverter;

    @Autowired
    UserService userService;

    public void importUsers(MultipartFile multipartFile) {
        File file = null;
        try {
            file = fileConverter.convert(multipartFile);
        } catch (IOException e) {
            throw new UserImportFailedException();
        }

        List<UserDTO> users = csvFileProcessor.loadObjectList(UserDTO.class, file);
        userService.createUsers(users);

        file.delete();
    }
}
