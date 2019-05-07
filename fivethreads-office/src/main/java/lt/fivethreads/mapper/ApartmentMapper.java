package lt.fivethreads.mapper;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Address;
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

    @Autowired
    AddressMapper addressMapper;

    public Apartment convertRegisteredOfficeToOffice(ApartmentForm apartmentForm){
        Apartment apartment = new Apartment();
        Office office;

        Address address = addressMapper.convertFullAddressToAddress(apartmentForm.getAddress());

        office = officeRepository.findById(apartmentForm.getOfficeId());
        apartment.setAddress(address);
        apartment.setOffice(office);
        return apartment;
    }

    public ApartmentDTO getApartmentDTO (Apartment apartment){
        ApartmentDTO apartmentDTO = new ApartmentDTO ();

        apartmentDTO.setAddress(addressMapper.convertAddressToFullAddress(apartment.getAddress()));
        apartmentDTO.setId(apartment.getId());
        apartmentDTO.setOfficeId(apartment.getOffice().getId());

        return apartmentDTO;
    }
}
