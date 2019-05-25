package lt.fivethreads.services;

import lt.fivethreads.entities.request.TripDTO;

import java.io.File;
import java.util.List;

public interface TripExportService {
    File exportAllEntities(String email);
    File exportMyEntities(String email);
    File exportOrganizerEntities(String email);
}
