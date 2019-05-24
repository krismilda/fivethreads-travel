package lt.fivethreads.mapper;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Address;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfficeMapper {

    @Autowired
    AddressMapper addressMapper;

    public Office convertRegisteredOfficeToOffice(OfficeForm officeForm) {

        Office office = new Office();

        Address address = addressMapper.convertFullAddressToAddress(officeForm.getAddress());

        office.setAddress(address);
        office.setName(officeForm.getName());
        return office;
    }

    public OfficeDTO getOfficeDTO(Office office) {
        OfficeDTO officeDTO = new OfficeDTO();

        officeDTO.setAddress(addressMapper.convertAddressToFullAddress(office.getAddress()));
        officeDTO.setName(office.getName());
        officeDTO.setId(office.getId());
        officeDTO.setVersion(office.getVersion());
        return officeDTO;
    }

    public Office getOffice(OfficeDTO officeDTO) {
        Office office = new Office();


        office.setAddress(addressMapper.convertFullAddressToAddress(officeDTO.getAddress()));
        office.setName(officeDTO.getName());
        office.setId(officeDTO.getId());

        return office;
    }
}
