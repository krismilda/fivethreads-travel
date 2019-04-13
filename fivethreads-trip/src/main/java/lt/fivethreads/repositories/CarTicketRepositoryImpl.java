package lt.fivethreads.repositories;

import lt.fivethreads.entities.CarTicket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarTicketRepositoryImpl implements CarTicketRepository
{
    @PersistenceContext
    EntityManager em;

    public void saveCarTicket (CarTicket carTicket){
        em.persist(carTicket);
    }

    public void updateCarTicket(CarTicket carTicket){
        em.merge(carTicket);
    }
}
