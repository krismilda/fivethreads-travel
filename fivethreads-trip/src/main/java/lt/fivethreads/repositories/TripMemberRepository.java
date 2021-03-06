package lt.fivethreads.repositories;

import lt.fivethreads.entities.CarTicket;
import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;

import java.util.List;

public interface TripMemberRepository {
    void saveTripMember(TripMember tripMember);
    void updateTripMember(TripMember tripMember);
    TripMember findById(Long id);
    Boolean checkIfExistByID(Long id);
    TripMember getTripMemberByTripIDAndEmail(Long tripID, String email);
    void addCancellation(TripCancellation tripCancellation);
    void saveFlightTicket(TripMember tripMember);
    void saveCarTicket(TripMember tripMember);
    TripMember findByFlightFileID(Long fileID);
    TripMember findByCarFileID(Long fileID);
    TripMember findByAccommodationFileID(Long fileID);
    void saveAccommodationTicket(TripMember tripMember);
    void deleteTripMember(TripMember tripMember);
    void removeCarTicket (CarTicket carTicket);
    void removeTripAccomodation (TripAccommodation tripAccommodation);
    List<TripMember> getAll();
}
