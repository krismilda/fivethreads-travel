package lt.fivethreads.services;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;

import java.util.Date;
import java.util.List;

public interface OfficeService {
    List<OfficeDTO> getAllOffices();
    Office getOfficeById(Long id);
    Office updateOffice(OfficeDTO office, Long version);
    void deleteOffice(Long id);
    Office createOffice(OfficeForm user);
    boolean checkIfOfficeExists(double latitude, double longitude, String name);
    void createOffices(List<OfficeDTO> officeDTOS);
    List <OfficeDTO> getAllUnoccupiedAccommodationOffices(Date startDate, Date finishDate);
    Boolean checkIfModified(Long officeID, String version);
}
