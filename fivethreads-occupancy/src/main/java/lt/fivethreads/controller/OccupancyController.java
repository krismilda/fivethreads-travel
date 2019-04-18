package lt.fivethreads.controller;


import lt.fivethreads.entities.request.OccupancyDTO;
import lt.fivethreads.entities.request.OccupancyForm;
import lt.fivethreads.services.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OccupancyController {

    @Autowired
    OccupancyService occupancyService;

    @GetMapping("/occupancies")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getAllOccupancies() {
        return new ResponseEntity<>(occupancyService.getAllOccupancies(), HttpStatus.OK);
    }

    @PostMapping("/occupancy/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerOccupancy(@Validated @RequestBody OccupancyForm occupancyForm) {
        OccupancyDTO occupancyDTO= occupancyService.createOccupancy(occupancyForm);
        return new ResponseEntity<>(occupancyDTO, HttpStatus.CREATED);
    }
}
