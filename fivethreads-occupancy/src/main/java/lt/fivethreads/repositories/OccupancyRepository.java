package lt.fivethreads.repositories;

import lt.fivethreads.entities.Occupancy;

import java.util.List;

public interface OccupancyRepository {
    Occupancy findByID(long id);
    List<Occupancy> getAll();
    Occupancy createOccupancy(Occupancy occupancy);
    List<Occupancy> getOccupancyByTrip(long id);
    List<Occupancy> getOccupancyByUser(long id);
    List<Occupancy> getOccupancyByApartment(long id);
    Occupancy updateOccupancy(Occupancy occupancy);
}
