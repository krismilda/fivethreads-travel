package lt.fivethreads.repositories;


import lt.fivethreads.entities.FlightTicket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class FlightTicketRepositoryImpl implements FlightTicketRepository {
    @PersistenceContext
    EntityManager em;

    public void saveFlightTicket(FlightTicket flightTicket) {
        em.persist(flightTicket);
    }

    public void updateFlightTicket(FlightTicket flightTicket){
        em.merge(flightTicket);
    }
}
