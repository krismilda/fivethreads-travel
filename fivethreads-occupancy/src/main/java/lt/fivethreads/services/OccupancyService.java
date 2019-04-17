package lt.fivethreads.services;

import lt.fivethreads.entities.request.OccupancyDTO;
import lt.fivethreads.entities.request.OccupancyForm;

import java.util.List;

public interface OccupancyService {
    OccupancyDTO createOccupancy(OccupancyForm occupancyForm);
    List<OccupancyDTO> getAllOccupancies();
    List<OccupancyDTO> getAllOccupanciesByUser(long id);
    List<OccupancyDTO> getAllOccupanciesByTrip(long id);
    List<OccupancyDTO> getAllOccupanciesByApartment(long id);
    //getOccupancyById
}
