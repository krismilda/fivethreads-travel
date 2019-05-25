package lt.fivethreads.exporting.csv;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class CsvUserExport extends CsvExport {

    @Autowired
    UserService userService;

    public File exportEntities (String email) throws IOException, ClassNotFoundException {

        List<ExtendedUserDTO> extendedUserDTOS = userService.getAllUser();
        prepareMapper(ExtendedUserDTO.class);
        return write(email, extendedUserDTOS, "users");
    }
}
