package lt.fivethreads.services;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Address;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import lt.fivethreads.mapper.OfficeMapper;
import lt.fivethreads.repositories.OfficeRepository;
import lt.fivethreads.validation.DateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfficeServiceImplementation implements OfficeService {

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    OfficeMapper officeMapper;

    @Autowired
    DateValidation dateValidation;

    @Autowired
    AddressMapper addressMapper;

    public List<OfficeDTO> getAllOffices() {
        List<Office> offices = officeRepository.getAll();
        return offices.stream()
                .map(e -> officeMapper.getOfficeDTO(e))
                .collect(Collectors.toList());
    }

    public Office getOfficeById(Long id) {
        Office office = officeRepository.findById(id);
        return office;
    }

    public Office updateOffice(OfficeDTO officeDTO) {
        Office office = officeRepository.findById(officeDTO.getId());
        Address address = office.getAddress();
        address.setCity(officeDTO.getAddress().getCity());
        address.setCountry(officeDTO.getAddress().getCountry());
        address.setHouseNumber(officeDTO.getAddress().getHouseNumber());
        address.setFlatNumber(officeDTO.getAddress().getFlatNumber());
        address.setLatitude(officeDTO.getAddress().getLatitude());
        address.setLongitude(officeDTO.getAddress().getLongitude());
        address.setStreet(officeDTO.getAddress().getStreet());
        office.setAddress(address);
        office.setName(officeDTO.getName());
        office.setId(officeDTO.getId());
        office.setVersion(officeDTO.getVersion());
        return officeRepository.updateOffice(office);
    }

    public void deleteOffice(Long id) {
        officeRepository.deleteOffice(id);
    }

    public Office createOffice(OfficeForm officeForm) {
        Office office_to_save = officeMapper.convertRegisteredOfficeToOffice(officeForm);
        return officeRepository.createOffice(office_to_save);
    }

    public boolean checkIfOfficeExists(double latitude, double longitude, String name) {

        return officeRepository.existsByAddressAndName(latitude, longitude, name);
    }

    public void createOffices(List<OfficeDTO> officeDTOS) {
        List<Office> officeEntities = new ArrayList<>();
        for (OfficeDTO officeDTO : officeDTOS) {
            Office officeEntity = officeMapper.getOffice(officeDTO);
            officeEntities.add(officeEntity);
        }

        for (Office office : officeEntities) {
            officeRepository.createOffice(office);
        }
    }

    public List<OfficeDTO> getAllUnoccupiedAccommodationOffices(Date startDate, Date finishDate) {
        dateValidation.checkFinishStartDates(startDate,
                finishDate, "Finish date is earlier than start date.");
        dateValidation.checkStartDateToday(startDate);
        List<Office> officeList = officeRepository.getOfficesWithUnoccupiedRooms(
                startDate, finishDate);
        List<OfficeDTO> officeDTOList= new ArrayList<>();
        for(Office office: officeList){
            officeDTOList.add(officeMapper.getOfficeDTO(office));
        }
        return officeDTOList;
    }

    public Boolean checkIfModified(Long officeID, String version){
        Office office = officeRepository.findById(officeID);
        String current_version = office.getVersion().toString();
        return !version.equals(current_version);
    }
}
