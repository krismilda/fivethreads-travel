package lt.fivethreads.repositories;

import lt.fivethreads.entities.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    boolean existsByNumberAndAndOfficeId(Long number, Long officeId);
}
