package lt.fivethreads.controller;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.exception.TripWasModified;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.services.NotificationService;
import lt.fivethreads.services.TripFilesService;
import lt.fivethreads.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RestController
public class TripController {
    @Autowired
    TripService tripService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TripFilesService tripFilesService;

    @Autowired
    TripMapper tripMapper;

    @PostMapping("/trip/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<TripDTO> createTrip(@Validated @RequestBody CreateTripForm form) {
        Trip trip = tripService.createTrip(form, SecurityContextHolder.getContext().getAuthentication().getName());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "eTag");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .eTag("\"" + trip.getVersion() + "\"")
                .body(tripMapper.converTripToTripDTO(trip));
    }

    @PostMapping("/trip/accept")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<TripMemberDTO> acceptTrip(@Validated @RequestBody AcceptedTrip acceptedTrip) {
        TripMemberDTO tripMemberDTO = notificationService.tripAccepted(acceptedTrip, SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO, HttpStatus.OK);
    }

    @PostMapping("/trip/cancelled")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<TripMemberDTO> cancelledTrip(@Validated @RequestBody CancelledTrip cancelledTrip) {
        TripMemberDTO tripMemberDTO = notificationService.tripCancelled(cancelledTrip, SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO, HttpStatus.OK);
    }

    @GetMapping("/allTrips")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public List<TripDTO> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/trip/{tripID}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<TripDTO> getTripById(@PathVariable("tripID") long tripID) {
        Trip trip =  tripService.getById(tripID);
        TripDTO tripDTO =  (trip != null) ? tripMapper.converTripToTripDTO(trip) : null;
        String version = trip.getVersion().toString();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "eTag");
        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .eTag(version)
                .body(tripDTO);
    }

    @GetMapping("/myTrips/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripDTO> getAllMyTrips() {
        return tripService.getAllTripsByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/organizedTrips/")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<TripDTO> getOrganizerTrips() {
        return tripService.getAllTripsByOrganizerEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/tripMember/flight/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadFlightTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file, @RequestParam(value="price", defaultValue = "0") double price) throws Exception {
        return tripFilesService.addFlightTicket(tripID, email, file, price);
    }

    @PostMapping("/tripMember/car/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadCarTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file, @RequestParam(value="price", defaultValue = "0") double price) {
        return tripFilesService.addCarTicket(tripID, email, file, price);
    }

    @PostMapping("/tripMember/accommodation/{trip_id}/{email}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public FileDTO uploadAccommodationTicket(@PathVariable("trip_id") Long tripID, @PathVariable("email") String email, @RequestParam("file") MultipartFile file, @RequestParam(value="price", defaultValue = "0") double price) {
        return tripFilesService.addAccommodationTicket(tripID, email, file, price);
    }

    @DeleteMapping("/tripMember/flight/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TripMemberDTO> deleteFlightTicket(@PathVariable("file_id") Long fileID) {
        TripMemberDTO tripMemberDTO = tripFilesService.deleteFlightTicket(fileID);
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO, HttpStatus.OK);
    }

    @DeleteMapping("/tripMember/car/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TripMemberDTO> deleteCarTicket(@PathVariable("file_id") Long fileID) {
        TripMemberDTO tripMemberDTO = tripFilesService.deleteCarTicket(fileID);
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO, HttpStatus.OK);
    }

    @DeleteMapping("/tripMember/accommodation/{file_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TripMemberDTO> deleteAccommodationTicket(@PathVariable("file_id") Long fileID) {
        TripMemberDTO tripMemberDTO = tripFilesService.deleteAccommodationTicket(fileID);
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO, HttpStatus.OK);
    }

    @PostMapping("/tripMember/{tripID}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TripMemberDTO> addNewTripMember(@Validated @RequestBody TripMemberDTO tripMemberDTO, @PathVariable("tripID") Long tripID) {
        TripMemberDTO tripMemberDTO1 = tripService.addNewTripMember(tripMemberDTO, tripID, SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<TripMemberDTO>(tripMemberDTO1, HttpStatus.OK);
    }

    @DeleteMapping("/trips/{tripID}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteTrip(@PathVariable("tripID") Long tripID) {
        tripService.deleteTrip(tripID, SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>("Trip deleted successfully!", HttpStatus.OK);
    }


    @PutMapping("/editTrip")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TripDTO> editTripInformation(@Validated @RequestBody EditTripInformation editTripInformation, WebRequest request) {
        Long version = Long.parseLong(request.getHeader("If-Match"));
        Trip tripDTO = tripService.editTripInformation(editTripInformation, SecurityContextHolder.getContext().getAuthentication().getName(), version);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "eTag");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .eTag("\"" + tripDTO.getVersion() + "\"")
                .body(tripMapper.converTripToTripDTO(tripDTO));
    }

    @PutMapping("/changeOrganizer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TripDTO> changeOrganizer(@Validated @RequestBody ChangeOrganizer changeOrganizer) {
        TripDTO tripDTO = tripService.changeOrganizer(changeOrganizer);
        return new ResponseEntity<TripDTO>(tripDTO, HttpStatus.OK);
    }

    @GetMapping("/myTrips/{tripID}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public UserTripDTO getUserTripByID(@PathVariable("tripID") Long tripID) {
        return tripService.getUserTripById(SecurityContextHolder.getContext().getAuthentication().getName(), tripID);
    }

    @DeleteMapping("/admin/user/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("userID") int userId) {
        long id = userId;
        tripService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}