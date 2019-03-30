package lt.fivethreads.services;

import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import java.util.List;

public interface OfficeService {
    List<OfficeDTO> getAllOffices();

    OfficeDTO getOfficeById(Long id);

    void updateOffice(OfficeDTO office);

    void deleteOffice(Long id);

    void createOffice(OfficeForm user);

    boolean checkIfOfficeExists(String name, String address);
}
