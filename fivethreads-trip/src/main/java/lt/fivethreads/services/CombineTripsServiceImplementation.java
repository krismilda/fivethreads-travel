package lt.fivethreads.services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.CombineTwoTrips;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.exception.CannotCombineTrips;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CombineTripsServiceImplementation implements CombineTripsService {
    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripFilesService tripFilesService;

    @Autowired
    UserService userService;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    CreateNotificationService createNotificationService;

    @Autowired
    AddressService addressService;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Transactional
    public TripDTO combineTwoTrips(CombineTwoTrips combineTwoTrips, String organizer_email) {
        //TODO check users' calendars
        //TODO check approved trips
        User organizer = userService.getUserByEmail(organizer_email);
        Trip trip1 = tripRepository.findByID(combineTwoTrips.getTripID1());
        Trip trip2 = tripRepository.findByID(combineTwoTrips.getTripID2());
        if (!checkIfPossibleToCombine(trip1, trip2, combineTwoTrips.getStartDate(), combineTwoTrips.getFinishDate())) {
            throw new CannotCombineTrips("Trips cannot be combined.");
        }
        Trip newTrip = new Trip();
        newTrip.setIsCombined(true);
        newTrip.setOrganizer(organizer);
        newTrip.setIsFlexible(trip1.getIsFlexible() || trip2.getIsFlexible());
        newTrip.setFinishDate(combineTwoTrips.getFinishDate());
        newTrip.setStartDate(combineTwoTrips.getStartDate());
        newTrip.setTripStatus(TripStatus.NOTSTARTED);
        newTrip.setArrival(addressMapper.convertFullAddressToAddress(combineTwoTrips.getArrival()));
        newTrip.setDeparture(addressMapper.convertFullAddressToAddress(combineTwoTrips.getDeparture()));
        List<TripMember> tripMembers = addTripMembersToList(trip1.getTripMembers(), trip2);
        List<TripMember> tripMembersInfoChanged = new ArrayList<>();
        if(checkIfInfoChanged(newTrip, trip1)){
            for (TripMember tripMember:trip1.getTripMembers()
            ) {
                tripMembersInfoChanged.add(tripMember);
            }
        }
        if(checkIfInfoChanged(newTrip, trip2)){
            for (TripMember tripMember:trip2.getTripMembers()
            ) {
                tripMembersInfoChanged.add(tripMember);
            }
        }
        List<TripMember> distinctTripMembers = new ArrayList<>();
        for (TripMember tripMember:tripMembersInfoChanged
             ) {
            if(!distinctTripMembers.stream().anyMatch(e->e.getUser().getEmail().equals(tripMember.getUser().getEmail())))
            {
//                createNotificationService.createNotificationForApprovalTripMember(tripMember, "Trips were combined. New trip's information is waiting your approval.");
                distinctTripMembers.add(tripMember);
                tripMember.setTrip(newTrip);
            }
        }
        for (TripMember tripMember2:tripMembers
             ) {
            if(!distinctTripMembers.stream().anyMatch(e->e.getUser().getEmail().equals(tripMember2.getUser().getEmail()))){
                distinctTripMembers.add(tripMember2);
                tripMember2.setTrip(newTrip);
            }
        }
        newTrip.setTripMembers(distinctTripMembers);
        trip1.getTripMembers().clear();
        trip2.getTripMembers().clear();
        tripRepository.createTrip(newTrip);
        List<TripMember> check = tripMemberRepository.getAll();
        List<TripMember> listTripMembersDelete = check.stream()
                .filter(e->(e.getTrip().getId().equals(trip1.getId())|e.getTrip().getId().equals(trip2.getId())))
                .collect(Collectors.toList());
        for (TripMember tripMemberTODelete:listTripMembersDelete
        ) {
            tripMemberTODelete.getTrip().getTripMembers().remove(tripMemberTODelete);
            tripMemberTODelete.setTrip(null);
            tripMemberTODelete.setUser(null);
            tripMemberRepository.deleteTripMember(tripMemberTODelete);
        }
        tripRepository.deleteTrip(trip1);
        tripRepository.deleteTrip(trip2);
        return tripMapper.converTripToTripDTO(newTrip);
    }

    public List<TripMember> addTripMembersToList(List<TripMember> tripMembers, Trip trip) {
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            tripMembers.add(tripMember);
        }
        return tripMembers;
    }

    public Boolean checkIfInfoChanged(Trip newTrip, Trip oldTrip) {
        return !(compareIfDatesEqual(oldTrip.getStartDate(), newTrip.getStartDate())
                && compareIfDatesEqual(oldTrip.getFinishDate(), newTrip.getFinishDate())
                && addressService.compareTwoAddress(newTrip.getArrival(), oldTrip.getArrival())
                && addressService.compareTwoAddress(newTrip.getDeparture(), oldTrip.getDeparture()));
    }

    public Boolean checkIfPossibleToCombine(Trip trip1, Trip trip2, Date start, Date finish) {
        Boolean departure_difference = trip1.getDeparture().getCountry().equals(trip2.getDeparture().getCountry()) &&
                                trip1.getDeparture().getCity().equals(trip2.getDeparture().getCity());
        Boolean arrival_difference = trip1.getArrival().getCountry().equals(trip2.getArrival().getCountry()) &&
                trip1.getDeparture().getCity().equals(trip2.getDeparture().getCity());
        Boolean check_dates1 = ((trip1.getIsFlexible() && compareDatesOneDayDifference(trip1, start, finish))
                || (!trip1.getIsFlexible()
                && compareIfDatesEqual(start, trip1.getStartDate())
                && compareIfDatesEqual(finish, trip1.getFinishDate())));
        Boolean check_dates2 = ((trip2.getIsFlexible() && compareDatesOneDayDifference(trip2, start, finish))
                || (!trip2.getIsFlexible()
                && compareIfDatesEqual(start, trip2.getStartDate())
                && compareIfDatesEqual(finish, trip2.getFinishDate())));
        Boolean check_documents_not_exists = !tripFilesService.checkIfDocumentsExist(trip1.getId())
                && !tripFilesService.checkIfDocumentsExist(trip2.getId());
        Boolean document_exist_trip1_start = compareIfDatesEqual(trip1.getStartDate(), start);
        Boolean document_exist_trip1_finish = compareIfDatesEqual(trip1.getFinishDate(), finish);
        Boolean document_exist_trip2_start = compareIfDatesEqual(trip1.getStartDate(), start);
        Boolean document_exist_trip2_finish = compareIfDatesEqual(trip1.getFinishDate(), finish);
        return departure_difference && arrival_difference &&
                ((check_dates1 && !tripFilesService.checkIfDocumentsExist(trip1.getId())) ||
                        document_exist_trip1_start && document_exist_trip1_finish &&
                                tripFilesService.checkIfDocumentsExist(trip1.getId())) &&
                ((check_dates2 && !tripFilesService.checkIfDocumentsExist(trip2.getId())) ||
                        document_exist_trip2_start && document_exist_trip2_finish &&
                                tripFilesService.checkIfDocumentsExist(trip2.getId()));
    }

    public List<TripDTO> getListForCombination(Long tripID) {
            List<Trip> tripList = tripRepository.getAll();
            Trip trip = tripRepository.findByID(tripID);
            List<Trip> combineTripList = tripList.stream()
                    .filter(e -> e.getDeparture().getCountry().equals(trip.getDeparture().getCountry())
                            && e.getArrival().getCountry().equals(trip.getArrival().getCountry())
                            && e.getArrival().getCity().equals(trip.getArrival().getCity())
                            && e.getDeparture().getCity().equals(trip.getDeparture().getCity())
                            && ((e.getIsFlexible() && compareDatesOneDayDifference(e, trip.getStartDate(), trip.getFinishDate()))
                            || (!e.getIsFlexible()
                            && compareIfDatesEqual(trip.getStartDate(), e.getStartDate())
                            && compareIfDatesEqual(trip.getFinishDate(), e.getFinishDate())))
                            && e.getId() != trip.getId()
                            && !tripFilesService.checkIfDocumentsExist(e.getId())
                    )
                    .collect(Collectors.toList());
        List<Trip> combineTripListFixed = tripList.stream()
                .filter(e -> e.getDeparture().getCountry().equals(trip.getDeparture().getCountry())
                        && e.getArrival().getCountry().equals(trip.getArrival().getCountry())
                        && e.getArrival().getCity().equals(trip.getArrival().getCity())
                        && e.getDeparture().getCity().equals(trip.getDeparture().getCity())
                        && tripFilesService.checkIfDocumentsExist(e.getId())
                        && compareIfDatesEqual(e.getStartDate(), trip.getStartDate())
                        && compareIfDatesEqual(e.getFinishDate(), trip.getFinishDate())
                         )
                .collect(Collectors.toList());

            List<TripDTO> tripDTOList = new ArrayList<>();
            for (Trip tripToCombine : combineTripList
            ) {
                tripDTOList.add(tripMapper.converTripToTripDTO(tripToCombine));
            }
        for (Trip tripToCombine : combineTripListFixed
        ) {
            tripDTOList.add(tripMapper.converTripToTripDTO(tripToCombine));
        }

        return tripDTOList;
    }

    public Boolean compareDatesOneDayDifference(Trip trip, Date start, Date finish){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date trip_start = formatter.parse(formatter.format(trip.getStartDate()));
            Date trip_finish = formatter.parse(formatter.format(trip.getFinishDate()));
            Date new_start = formatter.parse(formatter.format(start));
            Date new_finish = formatter.parse(formatter.format(finish));
            Boolean start1_check =  new_start.compareTo(new Date(trip_start.getTime() + TimeUnit.DAYS.toMillis(1))) <= 0;
            Boolean start2_check = new_start.compareTo(new Date(trip_start.getTime() + TimeUnit.DAYS.toMillis(-1))) >= 0;
            Boolean finish1_check = new_finish.compareTo(new Date(trip_finish.getTime() + TimeUnit.DAYS.toMillis(1))) <= 0;
            Boolean finish2_check = new_finish.compareTo(new Date(trip_finish.getTime() + TimeUnit.DAYS.toMillis(-1))) >= 0;
            return  start1_check && start2_check && finish1_check && finish2_check;
        }
        catch (ParseException e){
            System.out.println(e);
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }

    public Boolean compareIfDatesEqual(Date date1, Date date2){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date1_new = formatter.parse(formatter.format(date1));
            Date date2_new = formatter.parse(formatter.format(date2));
            return  date1_new.compareTo(date2_new) == 0;
        }
        catch (ParseException e){
            System.out.println(e);
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }
}
