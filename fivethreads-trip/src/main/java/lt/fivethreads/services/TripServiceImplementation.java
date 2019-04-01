package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripServiceImplementation implements TripService
{
    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripRepository tripRepository;

    public void createTrip(CreateTripForm form) throws WrongTripData
    {
        if(form==null || form.getStartDate()==null ||form.getArrival()==null ||form.getDeparture()==null){
            throw  new WrongTripData("Trip data is empty.");
        }
        if(form.getFinishDate().compareTo(form.getStartDate())<0){
            throw  new WrongTripData("Finish date is earlier than start date.");
        }
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form);
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);
    }
}
