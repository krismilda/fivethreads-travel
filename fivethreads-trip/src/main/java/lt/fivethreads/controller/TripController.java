package lt.fivethreads.controller;

import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TripController {
    @Autowired
    TripService tripService;

    @PostMapping("/trip/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> createTrip(@Validated @RequestBody CreateTripForm form) {
        tripService.createTrip(form);
        return new ResponseEntity<>("Trip created successfully!", HttpStatus.OK);
    }
}
