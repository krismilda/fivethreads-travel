package lt.fivethreads.importing.csv;

import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.importing.OfficeImportService;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;

@Component
public class CsvOfficeImport extends CsvImport implements OfficeImportService {

    @Autowired
    OfficeService officeService;

    @Override
    protected void saveEntities(File file) {
        List<OfficeDTO> offices = csvFileProcessor.loadObjectList(OfficeDTO.class, file);
        officeService.createOffices(offices);
    }
}
