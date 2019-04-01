package lt.fivethreads.repositories;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TripRepositoryImplementation implements TripRepository
{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TripMemberRepository tripMemberRepository;

    public Trip findByID(long id){
        return em.find(Trip.class, id);
    }

    public List<Trip> getAll(){
        return em.createNamedQuery("Trip.findAll", Trip.class).getResultList();
    }

    public void createTrip(Trip trip){
        for (TripMember tripMember:trip.getTripMembers()
             ) {
            tripMemberRepository.saveTripMember(tripMember);
        }
        em.persist(trip);
    }
}
