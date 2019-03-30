package lt.fivethreads.repositories;

import lt.fivethreads.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    Boolean existsByAddressAndName(String address, String name);
}
