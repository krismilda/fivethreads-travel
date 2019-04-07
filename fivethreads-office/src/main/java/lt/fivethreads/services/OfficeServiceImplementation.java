package lt.fivethreads.services;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import lt.fivethreads.mapper.OfficeMapper;
import lt.fivethreads.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfficeServiceImplementation implements OfficeService {

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    OfficeMapper officeMapper;

    public List<OfficeDTO> getAllOffices() {
        List<Office> offices = officeRepository.findAll();
        return offices.stream()
                .map(e -> officeMapper.getOfficeDTO(e))
                .collect(Collectors.toList());
    }

    public OfficeDTO getOfficeById(Long id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong officeId"));
        return officeMapper.getOfficeDTO(office);
    }

    public OfficeDTO updateOffice(OfficeDTO officeDTO) {

        Office office = officeRepository.findById(officeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong officeId"));
        office.setAddress(officeDTO.getAddress());
        office.setName(officeDTO.getName());
        office.setId(officeDTO.getId());
        return officeMapper.getOfficeDTO(officeRepository.save(office));
    }

    public void deleteOffice(Long id) {
        officeRepository.deleteById(id);
    }

    public OfficeDTO createOffice(OfficeForm officeForm) {
        Office office_to_save = officeMapper.convertRegisteredOfficeToOffice(officeForm);
        return officeMapper.getOfficeDTO(officeRepository.save(office_to_save));
    }

    @Override
    public boolean checkIfOfficeExists(String name, String address) {

        return officeRepository.existsByAddressAndName(address, name);
    }

    @Override
    public void createOffices(List<OfficeDTO> officeDTOS) {
        List<Office> officeEntities = new ArrayList<>();
        for (OfficeDTO officeDTO: officeDTOS) {
            Office officeEntity = officeMapper.getOffice(officeDTO);
            officeEntities.add(officeEntity);
        }

        officeRepository.saveAll(officeEntities);
    }
}
