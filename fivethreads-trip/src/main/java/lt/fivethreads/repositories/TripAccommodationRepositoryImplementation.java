package lt.fivethreads.repositories;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.TripAccommodationForm;
import lt.fivethreads.exception.WrongTripData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class TripAccommodationRepositoryImplementation implements TripAccommodationRepository {

    @PersistenceContext
    EntityManager em;

    public TripAccommodation findByID(long id) { return em.find(TripAccommodation.class, id); }

    public List<TripAccommodation> getAll() { return em.createNamedQuery("TripAccommodation.FindAll",
            TripAccommodation.class).getResultList(); }

    public TripAccommodation saveTripAccommodation(TripAccommodation tripAccommodation) {
        //TO DO check if user already belongs to that trip?
        em.persist(tripAccommodation);
        return tripAccommodation;
    }

    public TripAccommodation updateTripAccommodation(TripAccommodation tripAccommodation) {
        return em.merge(tripAccommodation);
    }

    public List<TripAccommodation> getAllByTrip(long tripId) {
        return em.createNamedQuery("TripAccommodation.FindByTrip", TripAccommodation.class)
                .setParameter("trip_ID", tripId)
                .getResultList();
    }

    public List<TripAccommodation> getAllByUser(long userId) {
        return em.createNamedQuery("TripAccommodation.FindByUser", TripAccommodation.class)
                .setParameter("user_ID", userId)
                .getResultList();
    }

    public List<TripAccommodation> getAllByApartment(long apartmentId) {
        return em.createNamedQuery("TripAccommodation.FindByApartment", TripAccommodation.class)
                .setParameter("apartment_ID", apartmentId)
                .getResultList();
    }

    @Override
    public void deleteTripAccommodation(Long id) {
        TripAccommodation  accommodation_to_delete = em.find(TripAccommodation.class, id);
        if(accommodation_to_delete.getAccommodationType() == AccommodationType.DEVRIDGE_APARTAMENTS)
            accommodation_to_delete.setHotelAddress(null);
        em.remove(accommodation_to_delete );
    }

    public List<TripAccommodation> getAllReservations(Long roomID){
        return em.createNamedQuery("TripMember.getAllReservations", TripAccommodation.class)
                .setParameter("roomID", roomID)
                .getResultList();
    }

}
