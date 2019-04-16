package lt.fivethreads.controller;

import lt.fivethreads.entities.RoleName;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.services.FileService;
import lt.fivethreads.services.NotificationService;
import lt.fivethreads.services.TripService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

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

    @GetMapping("/allTrips")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public List<TripDTO> getAllTrips() {
            return tripService.getAllTrips();
    }

    @GetMapping("/organizer/myTrips/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripDTO> getAllOrganizerTrips() {
        Collection authorities= SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"))){
            tripService.getAllTrips();
        }
        if(authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))){
            tripService.getAllTripsByOrganizerEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        if(authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))){
            tripService.getAllTripsByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return null;
    }
}