package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    TripValidation tripValidation;

    @Autowired
    NotificationService notificationService;

    public void createTrip(CreateTripForm form) throws WrongTripData
    {
        tripValidation.checkFnishStartDates(form.getStartDate(),form.getFinishDate(),"Finish date is earlier than start date.");
        tripValidation.checkStartDateToday(form.getStartDate());
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form);
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);
        notificationService.createNotifications(trip, "New trip is waiting for your approval.");
    }

    public void addTripMember(){

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
}
