package lt.fivethreads.importing;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.exception.user.UserImportFailedException;
import lt.fivethreads.importing.csv.CsvFileProcessor;
import lt.fivethreads.importing.file.FileConverter;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import lt.fivethreads.services.UserImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUserImport implements UserImportService {

    @Autowired
    CsvFileProcessor csvFileProcessor;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileConverter fileConverter;

    @Autowired
    UserMapper userMapper;

    public void importUsers(MultipartFile multipartFile) {
        File file = null;
        try {
            file = fileConverter.convert(multipartFile);
        } catch (IOException e) {
            throw new UserImportFailedException();
        }

        List<UserDTO> users = csvFileProcessor.loadObjectList(UserDTO.class, file);

        List<User> userEntities = new ArrayList<>();
        for (UserDTO user: users) {
            User userEntity = userMapper.getUser(user);
            userEntities.add(userEntity);
        }

        userRepository.saveAll(userEntities);
        file.delete();
    }
}
