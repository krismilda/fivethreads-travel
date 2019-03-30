package lt.fivethreads.mapper;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import org.springframework.stereotype.Component;

@Component
public class OfficeMapper {
    public Office convertRegisteredOfficeToOffice(OfficeForm officeForm){
        Office office = new Office();

        office.setAddress(officeForm.getAddress());
        office.setName(officeForm.getName());
        return office;
    }

    public OfficeDTO getOfficeDTO (Office office){
        OfficeDTO officeDTO = new OfficeDTO ();

        officeDTO.setAddress(office.getAddress());
        officeDTO.setName(office.getName());
        officeDTO.setId(office.getId());

        return officeDTO;
    }
}
