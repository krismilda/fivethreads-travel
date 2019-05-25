package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.exporting.ApartmentExportService;
import lt.fivethreads.export.CsvTripExport;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TripExportServiceImplementation implements TripExportService {

    @Autowired
    CsvTripExport csvTripExport;

    @Autowired
    TripRepository  tripRepository;

    public File exportAllEntities(String email) {

        List<Trip> trips = tripRepository.getAll();
        try {
            return csvTripExport.exportEntities(email, trips);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the I/O");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the the user entities to export");
        }
    }


    public File exportMyEntities(String email) {
        List<Trip> trips = tripRepository.getAllByUserEmail(email);
        try {
            return csvTripExport.exportEntities(email, trips);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the I/O");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the the user entities to export");
        }
    }

    public File exportOrganizerEntities(String email) {
        List<Trip> trips = tripRepository.getAllByOrganizerEmail(email);
        try {
            return csvTripExport.exportEntities(email, trips);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the I/O");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the the user entities to export");
        }
    }
}
