package lt.fivethreads.export;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.exporting.csv.CsvExport;
import lt.fivethreads.entities.CsvTrip;
import lt.fivethreads.repositories.ApartmentRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvTripExport extends CsvExport {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    AddressService addressService;

    @Autowired
    AddressMapper addressMapper;
    
    public File exportEntities(String email) throws ClassNotFoundException, IOException {
        List<Trip> trips= tripRepository.getAll();
        List<CsvTrip> csvTrips = new ArrayList<>();
        for (Trip trip: trips){
            CsvTrip csvTrip= new CsvTrip();
            csvTrip.setId(trip.getId());
            csvTrip.setStartDate(trip.getStartDate());
            csvTrip.setFinishDate(trip.getFinishDate());
            csvTrip.setIsCombined(trip.getIsCombined());
            csvTrip.setIsFlexible(trip.getIsFlexible());
            csvTrip.setTripStatus(trip.getTripStatus());
            //Setting up arrival
            csvTrip.setArrivalCity(trip.getArrival().getCity());
            csvTrip.setArrivalCountry(trip.getArrival().getCountry());
            csvTrip.setArrivalHouseNumber(trip.getArrival().getHouseNumber());
            csvTrip.setArrivalLatitude(trip.getArrival().getLatitude());
            csvTrip.setArrivalLongitude(trip.getArrival().getLongitude());
            csvTrip.setArrivalStreet(trip.getArrival().getStreet());
            //Setting up departure
            csvTrip.setDepartureCity(trip.getDeparture().getCity());
            csvTrip.setDepartureCountry(trip.getDeparture().getCountry());
            csvTrip.setDepartureHouseNumber(trip.getDeparture().getHouseNumber());
            csvTrip.setDepartureLatitude(trip.getDeparture().getLatitude());
            csvTrip.setDepartureLongitude(trip.getDeparture().getLongitude());
            csvTrip.setDepartureStreet(trip.getDeparture().getStreet());

            csvTrip.setOrganizer(trip.getOrganizer().getEmail());
            csvTrip.setTripMemberList(new ArrayList<>());
            for (TripMember tripMember : trip.getTripMembers())

            csvTrip.getTripMemberList().add(tripMember.getUser().getEmail());

            csvTrips.add(csvTrip);
        }

        prepareMapper(CsvTrip.class);
        File file = write(email, csvTrips, "trip");
        return file;
    }
}
