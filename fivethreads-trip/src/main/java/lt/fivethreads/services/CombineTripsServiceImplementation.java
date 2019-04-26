package lt.fivethreads.services;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.CombineTwoTrips;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.exception.CannotCombineTrips;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        newTrip.setTripMembers(addTripMembersToList(trip1.getTripMembers(), trip2));
        List<TripMember> tripMembersSendNotification = new ArrayList<>();
        if(checkIfInfoChanged(newTrip, trip1)){
            for (TripMember tripMember:trip1.getTripMembers()
            ) {
                tripMembersSendNotification.add(tripMember);
            }
        }
        if(checkIfInfoChanged(newTrip, trip2)){
            for (TripMember tripMember:trip2.getTripMembers()
            ) {
                tripMembersSendNotification.add(tripMember);
            }
        }
        newTrip.setTripMembers(newTrip.getTripMembers().stream().distinct().collect(Collectors.toList()));
        tripRepository.combineTrips(newTrip, trip1, trip2);
        for (TripMember tripMember:tripMembersSendNotification.stream()
                .distinct()
                .collect(Collectors.toList())
             ) {
            createNotificationService.createNotificationForApprovalTripMember(tripMember, "Trips were combined. New trip's information is waiting your approval.");
        }
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
        return !(newTrip.getStartDate().compareTo(oldTrip.getStartDate()) == 0
                && newTrip.getFinishDate().compareTo(oldTrip.getFinishDate()) == 0
                && addressService.compareTwoAddress(newTrip.getArrival(), oldTrip.getArrival())
                && addressService.compareTwoAddress(newTrip.getDeparture(), oldTrip.getDeparture()));
    }

    public Boolean checkIfPossibleToCombine(Trip trip1, Trip trip2, Date start, Date finish) {
        return trip1.getDeparture().getCountry().equals(trip2.getDeparture().getCountry())
                && trip1.getArrival().getCountry().equals(trip2.getArrival().getCountry())
                && trip1.getArrival().getCity().equals(trip2.getArrival().getCity())
                && trip1.getDeparture().getCity().equals(trip2.getDeparture().getCity())
                && ((trip1.getIsFlexible() && compareDatesOneDayDifference(trip1, start, finish))
                || (!trip1.getIsFlexible()
                && start.compareTo(trip1.getStartDate()) == 0
                && finish.compareTo(trip1.getFinishDate()) == 0))
                && ((trip2.getIsFlexible() && compareDatesOneDayDifference(trip1, start, finish))
                || (!trip2.getIsFlexible()
                && start.compareTo(trip2.getStartDate()) == 0
                && finish.compareTo(trip2.getFinishDate()) == 0))
                && !tripFilesService.checkIfDocumentsExist(trip1.getId())
                && !tripFilesService.checkIfDocumentsExist(trip2.getId());
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
                        && trip.getStartDate().compareTo(e.getStartDate()) == 0
                        && trip.getFinishDate().compareTo(e.getFinishDate()) == 0))
                        && e.getId() != trip.getId()
                        && !tripFilesService.checkIfDocumentsExist(e.getId())
                )
                .collect(Collectors.toList());
        List<TripDTO> tripDTOList = new ArrayList<>();
        for (Trip tripToCombine : combineTripList
        ) {
            tripDTOList.add(tripMapper.converTripToTripDTO(tripToCombine));
        }
        return tripDTOList;
    }

    public Boolean compareDatesOneDayDifference(Trip trip, Date start, Date finish){
        return   start.compareTo(new Date(trip.getStartDate().getTime() + TimeUnit.DAYS.toMillis(1))) <= 0
                && start.compareTo(new Date(trip.getStartDate().getTime() + TimeUnit.DAYS.toMillis(-1))) >= 0
                && finish.compareTo(new Date(trip.getFinishDate().getTime() + TimeUnit.DAYS.toMillis(1))) <= 0
                && finish.compareTo(new Date(trip.getFinishDate().getTime() + TimeUnit.DAYS.toMillis(-1))) >= 0;
    }
}
