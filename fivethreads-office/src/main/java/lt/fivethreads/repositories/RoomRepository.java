package lt.fivethreads.repositories;

import lt.fivethreads.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByNumberAndApartmentId(Long number, Long apartmentId);
}
