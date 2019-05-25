package lt.fivethreads.controller;

import lt.fivethreads.services.TripExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/export/trips")
    public ResponseEntity<?>exportTrips(HttpServletResponse response){

        File file = tripExportService.exportEntities(SecurityContextHolder.getContext().getAuthentication().getName());

        setFileDownloadResponse(file, response);

        return new ResponseEntity("File successfully exported", HttpStatus.OK);
    }
}