package lt.fivethreads.services;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.mapper.ApartmentMapper;
import lt.fivethreads.repositories.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ApartmentServiceImplementation implements ApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    ApartmentMapper apartmentMapper;


    @Override
    public List<ApartmentDTO> getAllApartments() {

        List<Apartment> apartments = apartmentRepository.findAll();
        return apartments.stream()
                .map(e -> apartmentMapper.getApartmentDTO(e))
                .collect(Collectors.toList());
    }

    @Override
    public ApartmentDTO getApartmentById(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong apartmentId"));
        return apartmentMapper.getApartmentDTO(apartment);
    }

    @Override
    public void updateApartment(ApartmentDTO apartmentDTO) {

        Apartment apartment = apartmentRepository.findById(apartmentDTO.getId())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong apartmentId"));
        apartment.setNumber(apartmentDTO.getNumber());
        Office office = new Office();
        office.setId(apartmentDTO.getOfficeId());
        apartment.setOffice(office);
        apartmentRepository.save(apartment);
    }


    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id); }


    public void createApartment(ApartmentForm apartmentForm) {
        Apartment apartment_to_save = apartmentMapper.convertRegisteredOfficeToOffice(apartmentForm);
        apartmentRepository.save(apartment_to_save);
    }

    @Override
    public boolean checkIfApartmentExists(Long number, Long officeId ) {
        return apartmentRepository.existsByNumberAndAndOfficeId(number, officeId);
    }
}
