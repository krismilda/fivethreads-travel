package lt.fivethreads.repositories;

import lt.fivethreads.entities.Address;
import lt.fivethreads.entities.Apartment;
import lt.fivethreads.exception.OfficeDataWasModified;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class ApartmentRepositoryImpl implements ApartmentRepository {

    @PersistenceContext
    EntityManager em;

    public Apartment findById(long id) {
        return em.find(Apartment.class, id);
    }

    public List<Apartment> getAll() {
        return em.createNamedQuery("Apartment.FindAll",
                Apartment.class).getResultList();
    }

    public void deleteApartment(long id) {
        Apartment apartment_to_delete = em.find(Apartment.class, id);
        em.remove(apartment_to_delete);
    }

    public Apartment updateApartment(Apartment apartment) {
        try{
            em.detach(apartment);
            return em.merge(apartment);
        }
        catch(OptimisticLockException e){
            throw new OfficeDataWasModified("Apartament was modified by other user.");
        }
    }

    public Apartment createApartment(Apartment apartment) {
        em.persist(apartment);
        em.flush();
        return apartment;
    }

    public boolean existsByAddressAndOfficeId(double latitude, double longitude, Long officeId) {
        return em.createNamedQuery("Apartment.ExistsByAddressAndOfficeId")
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("office_ID", officeId)
                .getResultList().size() == 1;
    }

    public List<Apartment> getApartmentsWithUnoccupiedRooms(Date startDate, Date finishDate) {
        return em.createNamedQuery("Apartment.FindUnoccupiedAccommodationApartments", Apartment.class)
                .setParameter("startDate", startDate)
                .setParameter("finishDate", finishDate)
                .getResultList();
    }

    public List<Apartment> getUnoccupiedApartmentsByOffice(Date startDate, Date finishDate, Long officeId) {
        return em.createNamedQuery("Apartment.FindUnoccupiedApartmentsByOfficeId", Apartment.class)
                .setParameter("startDate", startDate)
                .setParameter("finishDate", finishDate)
                .setParameter("office_ID", officeId)
                .getResultList();
    }
}
