package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripStatus;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleTripStatusCheckerImpl {

    @Autowired
    ITripStatusService tripStatusService;

    @Autowired
    TripRepository tripRepository;


    //@Scheduled(cron = "0 0 12 * * ?")
    @Scheduled(cron = "* 0/10 * * * ?")
    @Transactional
    public void create() {
        List<Trip> allTrips = tripRepository.getAll();
        for (Trip trip:allTrips
             ) {
            Boolean checked=Boolean.FALSE;
            if(!trip.getTripStatus().equals(TripStatus.COMPLETED)) {
                if (tripStatusService.checkIfCompleted(trip)) {
                    trip.setTripStatus(TripStatus.COMPLETED);
                    tripRepository.updateTrip(trip);
                    checked=Boolean.TRUE;
                }
                if (tripStatusService.checkIfStarted(trip) && !checked) {
                    trip.setTripStatus(TripStatus.ONGOING);
                    tripRepository.updateTrip(trip);
                    checked=Boolean.TRUE;
                }
                if (tripStatusService.checkIfPlanned(trip) && !checked) {
                    trip.setTripStatus(TripStatus.NOTSTARTED);
                    tripRepository.updateTrip(trip);
                    checked=Boolean.TRUE;
                }

                if (!tripStatusService.checkIfCompleted(trip) && !tripStatusService.checkIfStarted(trip) && !tripStatusService.checkIfPlanned(trip) && !checked) {
                    trip.setTripStatus(TripStatus.PLANNED);
                    tripRepository.updateTrip(trip);
                }
            }
        }
    }
}