package lt.fivethreads.mapper;

import lt.fivethreads.entities.Occupancy;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.OccupancyDTO;
import lt.fivethreads.repositories.RoomRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.repositories.UserRepository;
import lt.fivethreads.entities.request.OccupancyForm;
import lt.fivethreads.repositories.OccupancyRepository;
import lt.fivethreads.services.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OccupancyMapper{
    //service needed
    //repository needed
    @Autowired
    OccupancyService occupancyService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    OccupancyRepository occupancyRepository;

    public Occupancy convertCreateOccupancyFormToOccupancy (OccupancyForm occupancyForm){
        Occupancy occupancy = new Occupancy();
        Room room = roomRepository.getOne(occupancyForm.getRoomId());
        User user = userRepository.getOne(occupancyForm.getUserId());
        Trip trip = tripRepository.findByID(occupancyForm.getTripId());
        occupancy.setUser(user);
        occupancy.setRoom(room);
        occupancy.setStartDate(occupancyForm.getStartDate());
        occupancy.setFinishDate(occupancyForm.getFinishDate());
        occupancy.setTrip(trip);

        return occupancy;
    }

    public OccupancyDTO getOccupancyDTO(Occupancy occupancy){
        OccupancyDTO occupancyDTO = new OccupancyDTO();

        occupancyDTO.setId(occupancy.getId());
        occupancyDTO.setStartDate(occupancy.getStartDate());
        occupancyDTO.setFinishDate(occupancy.getFinishDate());
        occupancyDTO.setRoom(occupancy.getRoom());
        occupancyDTO.setUser(occupancy.getUser());
        occupancyDTO.setTrip(occupancy.getTrip());

        return occupancyDTO;
    }
}
