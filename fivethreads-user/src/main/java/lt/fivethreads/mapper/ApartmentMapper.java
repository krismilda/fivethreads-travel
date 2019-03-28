package lt.fivethreads.mapper;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentMapper {
    @Autowired
    OfficeRepository officeRepository;
    public Apartment convertRegisteredOfficeToOffice(ApartmentForm apartmentForm){
        Apartment apartment = new Apartment();
        Office office;

        office = officeRepository.getOne(apartmentForm.getOfficeId());
        apartment.setAddress(apartmentForm.getAddress());
        apartment.setOffice(office);
        return apartment;
    }

    public ApartmentDTO getApartmentDTO (Apartment apartment){
        ApartmentDTO apartmentDTO = new ApartmentDTO ();

        apartmentDTO.setAddress(apartment.getAddress());
        apartmentDTO.setId(apartment.getId());
        apartmentDTO.setOfficeId(apartment.getOffice().getId());

        return apartmentDTO;
    }
}
