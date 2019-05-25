package lt.fivethreads.controller;

import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.services.TripExportService;
import lt.fivethreads.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExportTripController extends AbstractFileController {


    @Autowired
    TripExportService tripExportService;

    @GetMapping("/export/allTrips")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?>exportAllTrips(HttpServletResponse response){

        File file = tripExportService.exportAllEntities(SecurityContextHolder.getContext().getAuthentication().getName());

        setFileDownloadResponse(file, response);

        return new ResponseEntity("File successfully exported", HttpStatus.OK);
    }

    @GetMapping("/export/myTrips")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?>exportMyTrips(HttpServletResponse response){
        File file = tripExportService.exportMyEntities(SecurityContextHolder.getContext().getAuthentication().getName());

        setFileDownloadResponse(file, response);

        return new ResponseEntity("File successfully exported", HttpStatus.OK);
    }

    @GetMapping("/export/organizerTrips")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?>exportOrganizerTrips(HttpServletResponse response){
        File file = tripExportService.exportOrganizerEntities(SecurityContextHolder.getContext().getAuthentication().getName());

        setFileDownloadResponse(file, response);

        return new ResponseEntity("File successfully exported", HttpStatus.OK);
    }

}