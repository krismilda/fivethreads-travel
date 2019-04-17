package lt.fivethreads.repositories;

import lt.fivethreads.entities.Occupancy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OccupancyRepositoryImplementation implements  OccupancyRepository {

    @PersistenceContext
    private EntityManager em;

    public Occupancy findByID(long id) { return em.find(Occupancy.class, id); }

    public List<Occupancy> getAll() { return em.createNamedQuery("Occupancy.FindAll", Occupancy.class).getResultList();}

    public Occupancy createOccupancy(Occupancy occupancy) {
        em.persist(occupancy);
        return occupancy;
    }

    public List<Occupancy> getOccupancyByTrip(long id) {
        return em.createNamedQuery("Occupancy.FindByTrip", Occupancy.class)
                .setParameter("trip_ID", id)
                .getResultList();
    }

    public List<Occupancy> getOccupancyByUser(long id) {
        return em.createNamedQuery("Occupancy,FindByUser", Occupancy.class)
                .setParameter("user_ID", id)
                .getResultList();
    }

    public List<Occupancy> getOccupancyByApartment(long id) {
        return em.createNamedQuery("Occupancy.FindByApartment", Occupancy.class)
                .setParameter("apartment_ID", id)
                .getResultList();
    }

    public Occupancy updateOccupancy(Occupancy occupancy) {
        return em.merge(occupancy);
    }
}
