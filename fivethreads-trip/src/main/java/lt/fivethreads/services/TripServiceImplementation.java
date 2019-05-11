package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.exception.AccessRightProblem;
import lt.fivethreads.exception.TripIsNotEditable;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.mapper.TripMemberMapper;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripAccommodationValidation;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripServiceImplementation implements TripService {
    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    TripValidation tripValidation;

    @Autowired
    CreateNotificationService createNotificationService;

    @Autowired
    TripMemberMapper tripMemberMapper;

    @Autowired
    TripFilesService tripFilesService;

    @Autowired
    UserService userService;

    @Autowired
    TripAccommodationValidation tripAccommodationValidation;

    public TripDTO createTrip(CreateTripForm form, String organizer_email) throws WrongTripData {
        tripValidation.checkFnishStartDates(form.getStartDate(), form.getFinishDate(), "Finish date is earlier than start date.");
        tripValidation.checkStartDateToday(form.getStartDate());
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form, organizer_email);
        for (TripMember tripMember : trip.getTripMembers()) {
            tripValidation.validateTripMember(tripMember);
            tripAccommodationValidation.checkFinishStartDates(tripMember.getTripAccommodation().getAccommodationStart(),
                    tripMember.getTripAccommodation().getAccommodationFinish(), "Finish date is earlier than start date.");
            tripAccommodationValidation.checkStartDateToday(tripMember.getTripAccommodation().getAccommodationStart());
            tripMember.setTripAcceptance(TripAcceptance.PENDING);
        }
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);
        createNotificationService.createNotificationsForApproval(trip, "New trip is waiting for your approval.");
        return tripMapper.converTripToTripDTO(trip);
    }

    public List<TripDTO> getAllTrips() {
        List<Trip> tripList = tripRepository.getAll();
        List<TripDTO> tripDTO = new ArrayList<>();
        for (Trip trip : tripList
        ) {
            tripDTO.add(tripMapper.converTripToTripDTO(trip));
        }
        return tripDTO;
    }

    public TripDTO getById(long id) {
        Trip trip = tripRepository.findByID(id);
        return (trip != null) ? tripMapper.converTripToTripDTO(trip) : null;
    }

    public List<TripDTO> getAllTripsByOrganizerEmail(String email) {
        List<Trip> tripList = tripRepository.getAllByOrganizerEmail(email);
        List<TripDTO> tripDTO = new ArrayList<>();
        for (Trip trip : tripList
        ) {
            tripDTO.add(tripMapper.converTripToTripDTO(trip));
        }
        return tripDTO;
    }

    public List<TripDTO> getAllTripsByUserEmail(String email) {
        List<Trip> tripList = tripRepository.getAllByUserEmail(email);
        List<TripDTO> tripDTO = new ArrayList<>();
        for (Trip trip : tripList
        ) {
            tripDTO.add(tripMapper.converTripToTripDTO(trip));
        }
        return tripDTO;
    }

    public TripMemberDTO addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable {
        TripMember tripMember = tripMemberMapper.convertTripMemberDTOtoTripMember(tripMemberDTO);
        Trip trip = tripRepository.findByID(tripID);
        if (!trip.getOrganizer().getEmail().equals(organizer_email)) {
            throw new AccessRightProblem("Only organizer can edit or delete the trip");
        }
        tripMember.setTripAcceptance(TripAcceptance.PENDING);

        tripMember.setTrip(trip);
        tripValidation.validateTripMember(tripMember);
        if (trip.getTripStatus() == TripStatus.COMPLETED) {
            throw new TripIsNotEditable("Trip is completed.");
        }
        tripMemberRepository.saveTripMember(tripMember);
        createNotificationService.createNotificationForApprovalTripMember(tripMember, "New trip is waiting for your approval.");
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    public void deleteTrip(Long tripID, String organizer_email) throws AccessRightProblem, TripIsNotEditable {
        if (tripFilesService.checkIfDocumentsExist(tripID)) {
            throw new TripIsNotEditable("Trip cannot be deleted because financial documents exist.");
        }
        Trip trip = tripRepository.findByID(tripID);
        if (!trip.getOrganizer().getEmail().equals(organizer_email)) {
            throw new AccessRightProblem("Only organizer can edit or delete the trip");
        }
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            createNotificationService.createNotificationDeleted(tripMember, "Trip was deleted.");
        }
        tripRepository.deleteTrip(trip);
    }


    public TripDTO editTripInformation(EditTripInformation editTripInformation, String organizer_email) throws AccessRightProblem, TripIsNotEditable {
        if (tripFilesService.checkIfDocumentsExist(editTripInformation.getId())) {
            throw new TripIsNotEditable("Trip cannot be deleted because financial documents exist.");
        }
        Trip trip = tripRepository.findByID(editTripInformation.getId());
        if (!trip.getOrganizer().getEmail().equals(organizer_email)) {
            throw new AccessRightProblem("Only organizer can edit or delete the trip");
        }
        if (!trip.getStartDate().equals(editTripInformation.getStartDate())
                || !trip.getFinishDate().equals(editTripInformation.getFinishDate())
                || trip.getArrival().getLongitude() != editTripInformation.getArrival().getLongitude()
                || trip.getArrival().getLatitude() != editTripInformation.getArrival().getLatitude()
                || trip.getDeparture().getLatitude() != editTripInformation.getDeparture().getLatitude()
                || trip.getDeparture().getLatitude() != editTripInformation.getDeparture().getLatitude()) {
            for (TripMember tripMember : trip.getTripMembers()
            ) {
                createNotificationService.createNotificationForApprovalTripMember(tripMember, "Trip's information was changed. Trip is waiting for your approval.");
            }
        }
        trip.setTripStatus(TripStatus.NOTSTARTED);
        trip.setStartDate(editTripInformation.getStartDate());
        trip.setFinishDate(editTripInformation.getFinishDate());
        Address arrival = new Address();
        arrival.setCity(editTripInformation.getArrival().getCity());
        arrival.setCountry(editTripInformation.getArrival().getCountry());
        arrival.setHouseNumber(editTripInformation.getArrival().getHouseNumber());
        arrival.setFlatNumber(editTripInformation.getArrival().getFlatNumber());
        arrival.setLatitude(editTripInformation.getArrival().getLatitude());
        arrival.setLongitude(editTripInformation.getArrival().getLongitude());
        arrival.setStreet(editTripInformation.getArrival().getStreet());
        Address departure = new Address();
        departure.setCity(editTripInformation.getDeparture().getCity());
        departure.setCountry(editTripInformation.getDeparture().getCountry());
        departure.setHouseNumber(editTripInformation.getDeparture().getHouseNumber());
        departure.setFlatNumber(editTripInformation.getDeparture().getFlatNumber());
        departure.setLatitude(editTripInformation.getDeparture().getLatitude());
        departure.setLongitude(editTripInformation.getDeparture().getLongitude());
        departure.setStreet(editTripInformation.getDeparture().getStreet());
        trip.setArrival(arrival);
        trip.setDeparture(departure);
        tripRepository.updateTrip(trip);
        return tripMapper.converTripToTripDTO(trip);
    }

    public TripDTO changeOrganizer(ChangeOrganizer changeOrganizer) {
        Trip trip = tripRepository.findByID(changeOrganizer.getId());
        User organizer = userService.getUserByEmail(changeOrganizer.getOrganizer_email());
        trip.setOrganizer(organizer);
        tripRepository.updateTrip(trip);
        return tripMapper.converTripToTripDTO(trip);
    }

    public UserTripDTO getUserTripById (String email, Long tripID){
        Trip trip = tripRepository.findByID(tripID);
        if(!trip.getTripMembers()
                .stream()
                .anyMatch(e->e.getUser().getEmail().equals(email)))
        {
            throw new AccessRightProblem("User is not the tripMember.");
        }
        return tripMemberMapper.convertTripToUserTripDTO(trip, email);
    }
}
