package lt.fivethreads.exporting.csv;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.exporting.objects.CsvApartment;
import lt.fivethreads.importing.csv.object.CsvOffice;
import lt.fivethreads.repositories.ApartmentRepository;
import lt.fivethreads.repositories.OfficeRepository;
import lt.fivethreads.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvOfficeExport extends CsvExport {
    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    AddressService addressService;
    
    public File exportEntities(String email) throws ClassNotFoundException, IOException {
        List<Office> offices= officeRepository.getAll();
        List<CsvOffice> csvOffices= new ArrayList<>();
        for (Office office: offices){
            CsvOffice csvOffice = new CsvOffice();
            csvOffice.setCity(office.getAddress().getCity());
            csvOffice.setCountry(office.getAddress().getCountry());
            csvOffice.setHouseNumber(office.getAddress().getHouseNumber());
            csvOffice.setLatitude(office.getAddress().getLatitude());
            csvOffice.setLongitude(office.getAddress().getLongitude());
            csvOffice.setStreet(office.getAddress().getStreet());
            csvOffice.setName(office.getName());
            csvOffices.add(csvOffice);
        }

        prepareMapper(CsvOffice.class);
        return write(email, csvOffices, "offices");
    }
}
