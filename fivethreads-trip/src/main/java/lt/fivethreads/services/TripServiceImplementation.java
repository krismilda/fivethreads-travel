package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.exception.TripIsNotEditable;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.mapper.TripMemberMapper;
import lt.fivethreads.repositories.FileRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripServiceImplementation implements TripService
{
    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    TripValidation tripValidation;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TripMemberMapper tripMemberMapper;

    public void createTrip(CreateTripForm form) throws WrongTripData
    {
        tripValidation.checkFnishStartDates(form.getStartDate(),form.getFinishDate(),"Finish date is earlier than start date.");
        tripValidation.checkStartDateToday(form.getStartDate());
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form);
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);
        notificationService.createNotifications(trip, "New trip is waiting for your approval.");
    }

    public List<TripDTO> getAllTrips(){
        List<Trip> tripList=tripRepository.getAll();
        List<TripDTO> tripDTO =new ArrayList<>();
        for (Trip trip:tripList
             ) {
            tripDTO.add(tripMapper.converTripToTripDAO(trip));
        }
        return tripDTO;
    }

    public List<TripDTO> getAllTripsByOrganizerEmail(String email){
        List<Trip> tripList=tripRepository.getAllByOrganizerEmail(email);
        List<TripDTO> tripDTO =new ArrayList<>();
        for (Trip trip:tripList
        ) {
            tripDTO.add(tripMapper.converTripToTripDAO(trip));
        }
        return tripDTO;
    }

    public List<TripDTO> getAllTripsByUserEmail(String email){
        List<Trip> tripList=tripRepository.getAllByUserEmail(email);
        List<TripDTO> tripDTO =new ArrayList<>();
        for (Trip trip:tripList
        ) {
            tripDTO.add(tripMapper.converTripToTripDAO(trip));
        }
        return tripDTO;
    }

    public void addNewTripMember(TripMemberDTO tripMemberDTO, Long tripID){
        TripMember tripMember = tripMemberMapper.convertTripMemberDAOtoTripMember(tripMemberDTO);
        Trip trip = tripRepository.findByID(tripID);
        tripMember.setTrip(trip);
        tripValidation.validateTripMember(tripMember);
        if(trip.getTripStatus()==TripStatus.COMPLETED){
            throw  new TripIsNotEditable("Trip is completed.");
        }
        tripMemberRepository.saveTripMember(tripMember);
        notificationService.createNotificationForTripMember(tripMember, "New trip is waiting for your approval.");
    }

    public void deleteTrip(String tripID){

    }
}
