package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.exception.AccessRightProblem;
import lt.fivethreads.exception.TripIsNotEditable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TripService {
    Trip createTrip(CreateTripForm form, String organizer_email);
    List<TripDTO> getAllTrips();
    Trip getById(long id);
    List<TripDTO> getAllTripsByOrganizerEmail(String email);
    List<TripDTO> getAllTripsByUserEmail(String email);
    TripMemberDTO addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    void deleteTrip(Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    Trip editTripInformation(EditTripInformation editTripInformation, String organizer_email) throws AccessRightProblem, TripIsNotEditable;
    TripDTO changeOrganizer(ChangeOrganizer changeOrganizer);
    UserTripDTO getUserTripById (String email, Long tripID);
    Boolean checkIfModified(Long tripID, String version);
}
