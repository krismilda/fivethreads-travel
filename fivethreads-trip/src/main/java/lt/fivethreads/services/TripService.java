package lt.fivethreads.services;

import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.exception.AccessRightProblem;
import lt.fivethreads.exception.TripIsNotEditable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {
    TripDTO createTrip(CreateTripForm form, String organizer_email);
    List<TripDTO> getAllTrips();
    List<TripDTO> getAllTripsByOrganizerEmail(String email);
    List<TripDTO> getAllTripsByUserEmail(String email);
    TripMemberDTO addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    void deleteTrip(Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    TripDTO editTripInformation(EditTripInformation editTripInformation, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    TripDTO changeOrganizer(ChangeOrganizer changeOrganizer);
}
