package lt.fivethreads.exporting.csv;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.exporting.objects.CsvApartment;
import lt.fivethreads.repositories.ApartmentRepository;
import lt.fivethreads.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvApartmentExport extends CsvExport {
    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    AddressService addressService;
    
    public File exportEntities(String email) throws ClassNotFoundException, IOException {
        List<Apartment> apartments= apartmentRepository.getAll();
        List<CsvApartment> csvApartments = new ArrayList<>();
        for (Apartment apartment: apartments){
            CsvApartment csvApartment = new CsvApartment();
            csvApartment.setCity(apartment.getAddress().getCity());
            csvApartment.setCountry(apartment.getAddress().getCountry());
            csvApartment.setHouseNumber(apartment.getAddress().getHouseNumber());
            csvApartment.setLatitude(apartment.getAddress().getLatitude());
            csvApartment.setLongitude(apartment.getAddress().getLongitude());
            csvApartment.setStreet(apartment.getAddress().getStreet());
            csvApartment.setId(apartment.getId());
            csvApartment.setOfficeId(apartment.getOffice().getId());
            csvApartment.setOfficeName(apartment.getOffice().getName());
            csvApartments.add(csvApartment);
        }

        prepareMapper(CsvApartment.class);
        return write(email, csvApartments, "apartments");
    }
}
