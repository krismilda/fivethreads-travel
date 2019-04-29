package lt.fivethreads.repositories;

import lt.fivethreads.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Boolean existsByName(String fileName);
}
