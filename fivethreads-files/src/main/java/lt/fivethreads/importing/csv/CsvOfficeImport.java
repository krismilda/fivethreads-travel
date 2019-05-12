package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.FullAddressDTO;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.importing.OfficeImportService;
import lt.fivethreads.importing.csv.object.CsvOffice;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvOfficeImport extends CsvImport implements OfficeImportService {

    @Autowired
    OfficeService officeService;

    @Override
    protected void saveEntities(File file) {
        List<CsvOffice> csvOffices = csvFileProcessor.loadObjectList(CsvOffice.class, file);

        List<OfficeDTO> officeDTOS = new ArrayList<>();
        for(CsvOffice csvOffice : csvOffices) {
            FullAddressDTO fullAddressDTO = new FullAddressDTO();
            fullAddressDTO.setCity(csvOffice.getCity());
            fullAddressDTO.setCountry(csvOffice.getCountry());
            fullAddressDTO.setHouseNumber(csvOffice.getHouseNumber());
            fullAddressDTO.setLatitude(csvOffice.getLatitude());
            fullAddressDTO.setLongitude(csvOffice.getLongitude());
            fullAddressDTO.setStreet(csvOffice.getStreet());

            OfficeDTO officeDTO = new OfficeDTO();
            officeDTO.setAddress(fullAddressDTO);
            officeDTO.setName(csvOffice.getName());

            officeDTOS.add(officeDTO);
        }

        officeService.createOffices(officeDTOS);
    }
}
