package lt.fivethreads.services;

import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;

import java.util.Date;
import java.util.List;

public interface ApartmentService {
    List<ApartmentDTO> getAllApartments();
    ApartmentDTO getApartmentById(Long id);
    ApartmentDTO updateApartment(ApartmentDTO apartment);
    void deleteApartment(Long id);
    ApartmentDTO createApartment(ApartmentForm apartment);
    boolean checkIfApartmentExists(String address, Long officeId);
    List <ApartmentDTO> getAllUnoccupiedAccommodationApartments(Date startDate, Date finishDate);
    List <ApartmentDTO> getAllUnoccupiedApartmentsByOfficeId (Date startDate, Date finishDate, Long officeId);
}
