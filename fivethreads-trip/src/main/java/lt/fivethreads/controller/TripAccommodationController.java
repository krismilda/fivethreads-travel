package lt.fivethreads.controller;

import lt.fivethreads.entities.request.DateIntervalForm;
import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.TripAccommodationForm;
import lt.fivethreads.services.TripAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TripAccommodationController {

    @Autowired
    TripAccommodationService tripAccommodationService;

    @GetMapping("/trip/accommodations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> getAllTripAccommodations(){
        return new ResponseEntity<>(tripAccommodationService.getAllTripAccommodations(), HttpStatus.OK);
    }
    @PostMapping("/trip/accommodation/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createTripAccommodation(@Validated @RequestBody TripAccommodationForm tripAccommodationForm)
    {

        TripAccommodationDTO createdTripAccommodation = tripAccommodationService
                .createTripAccommodation(tripAccommodationForm);
        return new ResponseEntity<>(createdTripAccommodation, HttpStatus.CREATED);
    }

    //reserve room only with start date, end date and user id
    //get all unoccupied rooms with start date, end date and possibly office id

    @GetMapping("/trip/rooms/unoccupied")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodations(@Validated @RequestBody DateIntervalForm form){

        return new ResponseEntity<>(tripAccommodationService.getAllUnoccupiedAccommodations(form.getStartDate(), form.getFinishDate()), HttpStatus.OK);
    }
}
