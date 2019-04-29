package lt.fivethreads.repositories;

import lt.fivethreads.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfficeRepository{
    Office findById(long id);
    List<Office> getAll();
    void deleteOffice(long id);
    Office updateOffice(Office office);
    Office createOffice(Office office);
    Boolean existsByAddressAndName(String address, String name);
    Office findByName(String name);
    List<Office> getOfficesWithUnoccupiedRooms (Date startDate, Date finishDate);
}
