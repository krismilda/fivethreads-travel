package lt.fivethreads.exporting;

import lt.fivethreads.exporting.csv.CsvUserExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class UserExportServiceImplementation implements UserExportService {

    @Autowired
    CsvUserExport csvUserExport;

    public File exportEntities(String email) {

        try {
            return csvUserExport.exportEntities(email);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the I/O");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the the user entities to export");
        }



    }
}
