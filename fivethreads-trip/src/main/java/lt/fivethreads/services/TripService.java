package lt.fivethreads.services;

import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {
    TripDTO createTrip(CreateTripForm form);
    List<TripDTO> getAllTrips();
    List<TripDTO> getAllTripsByOrganizerEmail(String email);
    List<TripDTO> getAllTripsByUserEmail(String email);
    TripMemberDTO addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID);
    void deleteTrip(Long tripID);
    TripDTO editTripInformation(EditTripInformation editTripInformation);
}
