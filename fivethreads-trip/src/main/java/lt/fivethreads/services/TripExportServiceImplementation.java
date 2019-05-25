package lt.fivethreads.services;

import lt.fivethreads.exporting.ApartmentExportService;
import lt.fivethreads.export.CsvTripExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TripExportServiceImplementation implements TripExportService {

    @Autowired
    CsvTripExport csvTripExport;

    public File exportEntities(String email) {

        try {
            return csvTripExport.exportEntities(email);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the I/O");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the the user entities to export");
        }



    }
}
