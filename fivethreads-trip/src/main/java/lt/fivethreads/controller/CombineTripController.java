package lt.fivethreads.controller;

import lt.fivethreads.entities.request.CombineTwoTrips;
import lt.fivethreads.entities.request.Notifications.NotificationApproved;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.services.CombineTripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CombineTripController {

    @Autowired
    CombineTripsService combineTripsService;

    @GetMapping("/tripsToCombine/{tripID}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<TripDTO> getListForCombination(@PathVariable("tripID") Long tripID){
        return combineTripsService.getListForCombination(tripID);
    }

    @PostMapping("/combine")
    @PreAuthorize("hasRole('ORGANIZER')")
    public TripDTO combineTwoTrips(@Validated @RequestBody CombineTwoTrips combineTwoTrips){
        return combineTripsService.combineTwoTrips(combineTwoTrips, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
