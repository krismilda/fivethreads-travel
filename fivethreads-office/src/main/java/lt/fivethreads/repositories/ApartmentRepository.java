package lt.fivethreads.repositories;

import lt.fivethreads.entities.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository {
    Apartment findById(long id);
    List<Apartment> getAll();
    void deleteApartment(long id);
    Apartment updateApartment(Apartment apartment);
    Apartment createApartment(Apartment apartment);
    boolean existsByAddressAndOfficeId(double latitude, double longitude , Long officeId);
    List<Apartment> getApartmentsWithUnoccupiedRooms(Date startDate, Date finishDate);
    List <Apartment> getUnoccupiedApartmentsByOffice(Date startDate, Date finishDate, Long officeId);
}
