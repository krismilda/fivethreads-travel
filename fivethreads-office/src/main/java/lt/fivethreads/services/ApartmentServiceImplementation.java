package lt.fivethreads.services;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.mapper.ApartmentMapper;
import lt.fivethreads.repositories.ApartmentRepository;
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


    public List<ApartmentDTO> getAllApartments() {
        List<Apartment> apartments = apartmentRepository.getAll();
        return apartments.stream()
                .map(e -> apartmentMapper.getApartmentDTO(e))
                .collect(Collectors.toList());
    }

    public ApartmentDTO getApartmentById(Long id) {
        Apartment apartment = apartmentRepository.findById(id);
        return apartmentMapper.getApartmentDTO(apartment);
    }

    public ApartmentDTO updateApartment(ApartmentDTO apartmentDTO) {
        Apartment apartment = apartmentRepository.findById(apartmentDTO.getId());
        apartment.setAddress(apartmentDTO.getAddress());
        Office office = new Office();
        office.setId(apartmentDTO.getOfficeId());
        apartment.setOffice(office);
        return apartmentMapper.getApartmentDTO(apartmentRepository.updateApartment(apartment));
    }


    public void deleteApartment(Long id) {
        apartmentRepository.deleteApartment(id);
    }


    public ApartmentDTO createApartment(ApartmentForm apartmentForm) {
        Apartment apartment_to_save = apartmentMapper.convertRegisteredOfficeToOffice(apartmentForm);
        return apartmentMapper.getApartmentDTO(apartmentRepository.createApartment(apartment_to_save));
    }

    public boolean checkIfApartmentExists(String address, Long officeId) {
        return apartmentRepository.existsByAddressAndOfficeId(address, officeId);
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
}
