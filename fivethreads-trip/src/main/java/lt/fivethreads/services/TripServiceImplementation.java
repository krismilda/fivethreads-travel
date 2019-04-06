package lt.fivethreads.services;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class TripServiceImplementation implements TripService
{
    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripValidation tripValidation;

    public void createTrip(CreateTripForm form) throws WrongTripData
    {
        tripValidation.checkFnishStartDates(form.getStartDate(),form.getFinishDate(),"Finish date is earlier than start date.");
        tripValidation.checkStartDateToday(form.getStartDate());
        Trip trip = tripMapper.ConvertCreateTripFormToTrip(form);
        trip.setTripStatus(TripStatus.PLANNED);
        tripRepository.createTrip(trip);

        //TODO: send notifications
    }

    public void addTripMember(){

    }
}
