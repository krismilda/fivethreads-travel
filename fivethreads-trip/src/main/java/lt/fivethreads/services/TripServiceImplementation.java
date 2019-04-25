package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.EditTripInformation;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.exception.TripIsNotEditable;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.mapper.TripMemberMapper;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
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

    public void createTrip(CreateTripForm form) throws WrongTripData {
        tripValidation.checkFnishStartDates(form.getStartDate(), form.getFinishDate(), "Finish date is earlier than start date.");
        tripValidation.checkStartDateToday(form.getStartDate());
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form);
        for (TripMember tripMember : trip.getTripMembers()) {
            tripValidation.validateTripMember(tripMember);
        }
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);
        createNotificationService.createNotificationsForApproval(trip, "New trip is waiting for your approval.");
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

    public void addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID) {
        TripMember tripMember = tripMemberMapper.convertTripMemberDTOtoTripMember(tripMemberDTO);
        Trip trip = tripRepository.findByID(tripID);
        tripMember.setTrip(trip);
        tripValidation.validateTripMember(tripMember);
        if (trip.getTripStatus() == TripStatus.COMPLETED) {
            throw new TripIsNotEditable("Trip is completed.");
        }
        tripMemberRepository.saveTripMember(tripMember);
        createNotificationService.createNotificationForApprovalTripMember(tripMember, "New trip is waiting for your approval.");
    }

    public void deleteTrip(Long tripID) {
        if (tripFilesService.checkIfDocumentsExist(tripID)) {
            throw new TripIsNotEditable("Trip cannot be deleted because financial documents exist.");
        }
        Trip trip = tripRepository.findByID(tripID);
        for (TripMember tripMember: trip.getTripMembers()
             ) {
            createNotificationService.createNotificaitonDeleted(tripMember, "Trip was deleted.");
        }
        tripRepository.deleteTrip(trip);
    }


    public void editTripInformation(EditTripInformation editTripInformation) {
        if (tripFilesService.checkIfDocumentsExist(editTripInformation.getId())) {
            throw new TripIsNotEditable("Trip cannot be deleted because financial documents exist.");
        }
        Trip trip = tripRepository.findByID(editTripInformation.getId());
        if (!trip.getStartDate().equals(editTripInformation.getStartDate())
                || !trip.getFinishDate().equals(editTripInformation.getFinishDate())
                || !trip.getArrival().equals(editTripInformation.getArrival())
                || !trip.getDeparture().equals(editTripInformation.getDeparture())) {
            for (TripMember tripMember: trip.getTripMembers()
            ) {
                createNotificationService.createNotificationForApproval(tripMember, "Trip's information was changed. Trip is waiting for your approval.");
            }
        }
        trip.setTripStatus(TripStatus.NOTSTARTED);
        trip.setStartDate(editTripInformation.getStartDate());
        trip.setFinishDate(editTripInformation.getFinishDate());
        trip.setArrival(editTripInformation.getArrival());
        trip.setDeparture(editTripInformation.getDeparture());
        tripRepository.updateTrip(trip);
    }
}
