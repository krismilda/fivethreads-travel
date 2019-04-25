package lt.fivethreads.controller;

import lt.fivethreads.entities.request.*;
import lt.fivethreads.services.NotificationService;
import lt.fivethreads.services.TripFilesService;
import lt.fivethreads.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TripController {
    @Autowired
    TripService tripService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TripFilesService tripFilesService;

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

    @GetMapping("/myTrips/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripDTO> getAllOrganizerTrips() {
        Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"))) {
            return tripService.getAllTrips();
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))) {
            return tripService.getAllTripsByOrganizerEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))) {
            return tripService.getAllTripsByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return null;
    }

    @PostMapping("/tripMember/flight/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadFlightTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file) throws Exception {
        return tripFilesService.addFlightTicket(tripID, email, file);
    }

    @PostMapping("/tripMember/car/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadCarTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file){
        return tripFilesService.addCarTicket(tripID, email, file);
    }

    @PostMapping("/tripMember/accommodation/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadAccommodationTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file){
        return tripFilesService.addAccommodationTicket(tripID, email, file);
    }

    @DeleteMapping("/tripMember/flight/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteFlightTicket(@PathVariable("file_id") Long fileID){
        tripFilesService.deleteFlightTicket(fileID);
        return new ResponseEntity<>("File deleted successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/tripMember/car/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteCarTicket(@PathVariable("file_id") Long fileID){
        tripFilesService.deleteCarTicket(fileID);
        return new ResponseEntity<>("File deleted successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/tripMember/accommodation/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteAccommodationTicket(@PathVariable("file_id") Long fileID){
        tripFilesService.deleteAccommodationTicket(fileID);
        return new ResponseEntity<>("File deleted successfully!", HttpStatus.OK);
    }

    @PostMapping("/tripMember/{tripID}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> addNewTripMember(@Validated @RequestBody TripMemberDTO tripMemberDTO, @PathVariable("tripID") Long tripID) {
        tripService.addNewTripMember(tripMemberDTO, tripID);
        return new ResponseEntity<>("Trip member added successfully!", HttpStatus.OK);
    }

    @DeleteMapping("{tripID}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteTrip(@PathVariable("tripID") Long tripID) {
        tripService.deleteTrip(tripID);
        return new ResponseEntity<>("Trip deleted successfully!", HttpStatus.OK);
    }


    @PutMapping("/editTrip")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> editTripInformation(@Validated @RequestBody EditTripInformation editTripInformation) {
        tripService.editTripInformation(editTripInformation);
        return new ResponseEntity<>("Trip edited successfully!", HttpStatus.OK);
    }
}