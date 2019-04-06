package lt.fivethreads.repositories;

import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;

public interface TripMemberRepository {
    void saveTripMember(TripMember tripMember);
    void updateTripMember(TripMember tripMember);
    TripMember findById(Long id);
    Boolean checkIfExistByID(Long id);
    TripMember getTripMemberByTripIDAndEmail(Long tripID, String email);
    void addCancellation(TripCancellation tripCancellation);
}
