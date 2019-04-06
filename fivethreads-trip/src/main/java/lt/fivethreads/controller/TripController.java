package lt.fivethreads.controller;

import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.services.NotificationService;
import lt.fivethreads.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TripController {
    @Autowired
    TripService tripService;

    @Autowired
    NotificationService notificationService;

    @PostMapping("/trip/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> createTrip(@Validated @RequestBody CreateTripForm form) {
        tripService.createTrip(form);
        return new ResponseEntity<>("Trip created successfully!", HttpStatus.OK);
    }

    @PostMapping("/trip/accept")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> acceptTrip(@Validated @RequestBody AcceptedTrip acceptedTrip) {
        notificationService.tripAccepted(acceptedTrip);
        return new ResponseEntity<>("Trip accepted successfully!", HttpStatus.OK);
    }

    @PostMapping("/trip/cancelled")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelledTrip(@Validated @RequestBody CancelledTrip cancelledTrip) {
        notificationService.tripCancelled(cancelledTrip);
        return new ResponseEntity<>("Trip cancelled successfully!", HttpStatus.OK);
    }
}
