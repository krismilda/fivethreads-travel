package lt.fivethreads.services;

import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {
    void createTrip(CreateTripForm form);
    List<TripDTO> getAllTrips();
    List<TripDTO> getAllTripsByOrganizerEmail(String email);
    List<TripDTO> getAllTripsByUserEmail(String email);
    void addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID);
    void deleteTrip(String tripID);
}
