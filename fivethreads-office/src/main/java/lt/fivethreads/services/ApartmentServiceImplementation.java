package lt.fivethreads.services;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Address;
import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.mapper.ApartmentMapper;
import lt.fivethreads.repositories.ApartmentRepository;
import lt.fivethreads.repositories.OfficeRepository;
import lt.fivethreads.repository.AddressRepository;
import lt.fivethreads.validation.DateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentServiceImplementation implements ApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    DateValidation dateValidation;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    AddressService addressService;

    @Autowired
    OfficeRepository officeRepository;

    public List<ApartmentDTO> getAllApartments() {
        List<Apartment> apartments = apartmentRepository.getAll();
        return apartments.stream()
                .map(e -> apartmentMapper.getApartmentDTO(e))
                .collect(Collectors.toList());
    }

    public Apartment getApartmentById(Long id) {
        Apartment apartment = apartmentRepository.findById(id);
        return apartment;
    }

    public Apartment updateApartment(ApartmentDTO apartmentDTO, Long version) {
        Apartment apartment = apartmentRepository.findById(apartmentDTO.getId());
        Address address = apartment.getAddress();
        address.setCity(apartmentDTO.getAddress().getCity());
        address.setCountry(apartmentDTO.getAddress().getCountry());
        address.setHouseNumber(apartmentDTO.getAddress().getHouseNumber());
        address.setFlatNumber(apartmentDTO.getAddress().getFlatNumber());
        address.setLatitude(apartmentDTO.getAddress().getLatitude());
        address.setLongitude(apartmentDTO.getAddress().getLongitude());
        address.setStreet(apartmentDTO.getAddress().getStreet());
        apartment.setVersion(version);
        apartment.setAddress(address);

        apartment.setOffice(officeRepository.findById(apartmentDTO.getOfficeId()));
        return apartmentRepository.updateApartment(apartment);
    }


    public void deleteApartment(Long id) {
        apartmentRepository.deleteApartment(id);
    }


    public Apartment createApartment(ApartmentForm apartmentForm) {
        Apartment apartment_to_save = apartmentMapper.convertRegisteredOfficeToOffice(apartmentForm);
        return apartmentRepository.createApartment(apartment_to_save);
    }

    public boolean checkIfApartmentExists(double latitude, double longitude, Long officeId) {
        return apartmentRepository.existsByAddressAndOfficeId(latitude, longitude, officeId);
    }

    public List<ApartmentDTO> getAllUnoccupiedAccommodationApartments(Date startDate, Date finishDate) {
        dateValidation.checkFinishStartDates(startDate,
                finishDate, "Finish date is earlier than start date.");
        dateValidation.checkStartDateToday(startDate);
        List<Apartment> apartmentList = apartmentRepository.getApartmentsWithUnoccupiedRooms(startDate, finishDate);
        List<ApartmentDTO> apartmentDTOList = new ArrayList<>();
        for(Apartment apartment: apartmentList){
            apartmentDTOList.add(apartmentMapper.getApartmentDTO(apartment));
        }
        return apartmentDTOList;
    }

    public List<ApartmentDTO> getAllUnoccupiedApartmentsByOfficeId(Date startDate, Date finishDate, Long officeId) {
        dateValidation.checkFinishStartDates(startDate,
                finishDate, "Finish date is earlier than start date.");
        dateValidation.checkStartDateToday(startDate);
        List<Apartment> apartmentList = apartmentRepository.getUnoccupiedApartmentsByOffice(startDate, finishDate, officeId);
        List<ApartmentDTO> apartmentDTOList= new ArrayList<>();
        for(Apartment apartment: apartmentList){
            apartmentDTOList.add(apartmentMapper.getApartmentDTO(apartment));
        }
        return apartmentDTOList;
    }

    public Boolean checkIfModified(Long apartmentID, String version){
        Apartment apartment = apartmentRepository.findById(apartmentID);
        String current_version = apartment.getVersion().toString();
        return !version.equals(current_version);
    }
}
