package lt.fivethreads.mapper;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.repositories.OfficeRepository;
import org.springframework.stereotype.Component;

@Component
public class ApartmentMapper {
    public Apartment convertRegisteredOfficeToOffice(ApartmentForm apartmentForm){
        Apartment apartment = new Apartment();
        Office office = new Office();
        office.setId(apartmentForm.getOfficeId());
        apartment.setNumber(apartmentForm.getNumber());
        apartment.setOffice(office);
        return apartment;
    }

    public ApartmentDTO getApartmentDTO (Apartment apartment){
        ApartmentDTO apartmentDTO = new ApartmentDTO ();

        apartmentDTO.setNumber(apartment.getNumber());
        apartmentDTO.setId(apartment.getId());
        apartmentDTO.setOfficeId(apartment.getOfficeId());

        return apartmentDTO;
    }
}
