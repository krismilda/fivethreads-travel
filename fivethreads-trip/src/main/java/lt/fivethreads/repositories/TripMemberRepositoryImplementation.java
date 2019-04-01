package lt.fivethreads.repositories;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class TripMemberRepositoryImplementation implements TripMemberRepository
{

    @PersistenceContext
    EntityManager em;

    public void saveTripMember(TripMember tripMember){
        em.persist(tripMember);
    }
}
