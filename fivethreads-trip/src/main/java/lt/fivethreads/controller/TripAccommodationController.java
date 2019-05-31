package lt.fivethreads.controller;

import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.TripAccommodationForm;
import lt.fivethreads.services.TripAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TripAccommodationController {

    @Autowired
    TripAccommodationService tripAccommodationService;

    @GetMapping("/trip/accommodations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') ")
    public ResponseEntity<?> getAllTripAccommodations(){
        return new ResponseEntity<>(tripAccommodationService.getAllTripAccommodations(), HttpStatus.OK);
    }
    @PostMapping("/trip/accommodation/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> createTripAccommodation(@Validated @RequestBody TripAccommodationForm tripAccommodationForm)
    {

        TripAccommodationDTO createdTripAccommodation = tripAccommodationService
                .createTripAccommodation(tripAccommodationForm);
        return new ResponseEntity<>(createdTripAccommodation, HttpStatus.CREATED);
    }

    @PutMapping("/trip/accommodations/accommodation/")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> updateTripAccommodation(@Validated @RequestBody TripAccommodationDTO tripAccommodationDTO){
        TripAccommodationDTO updatedAccommodation = tripAccommodationService.updateTripAccommodation(tripAccommodationDTO);
        return new ResponseEntity<>(updatedAccommodation, HttpStatus.OK);
    }

    @DeleteMapping("trip/accommodations/{accommodationId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteTripAccommodation(@PathVariable("accommodationId") int accommodationId) {
        long id = accommodationId;
        tripAccommodationService.deleteTripAccommodation(id);
        return new ResponseEntity<>("Trip accommodation deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/room/reservations/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> getTripReservations(@PathVariable("id") long id){
        //Long roomid = Long.parseLong(id);
        return new ResponseEntity<>(tripAccommodationService.getAllReservations(id), HttpStatus.OK);
    }
    @GetMapping("/trip/accommodations/tripMember={id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> getTripAccommodationsByTripMemberID(@PathVariable("id") long id){
        return new ResponseEntity<>(tripAccommodationService.getTripAccommodationsByTrioMember(id), HttpStatus.OK);
    }
}
