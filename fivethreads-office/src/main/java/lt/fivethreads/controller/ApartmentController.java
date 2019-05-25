package lt.fivethreads.controller;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.entities.request.DateForm;
import lt.fivethreads.mapper.ApartmentMapper;
import lt.fivethreads.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ApartmentController {

    @Autowired
    ApartmentService apartmentService;

    @Autowired
    ApartmentMapper apartmentMapper;

    @GetMapping("/apartments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getAllApartments() {
        return new ResponseEntity<>(apartmentService.getAllApartments(),
                HttpStatus.OK);
    }

    @GetMapping("/apartments/{apartmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getApartmentById(@PathVariable("apartmentId") int apartmentId) {
        long id = apartmentId;
        Apartment apartment = apartmentService.getApartmentById(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apartmentMapper.getApartmentDTO(apartment));
    }

    @DeleteMapping("/admin/apartments/{apartmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteApartment(@PathVariable("apartmentId") int apartmentId) {
        long id = apartmentId;
        apartmentService.deleteApartment(id);
        return new ResponseEntity<>("Apartment deleted successfully!",
                HttpStatus.OK);
    }

    @PutMapping("/apartments/apartment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> updateApartment(@Validated @RequestBody ApartmentDTO apartmentDTO) {
        Apartment updatedApartment = apartmentService.updateApartment(apartmentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apartmentMapper.getApartmentDTO(updatedApartment));
    }

    @PostMapping("/admin/apartments/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerApartment(@Validated @RequestBody ApartmentForm apartmentForm) {
        if (apartmentForm == null) {
            return new ResponseEntity<>("Fail -> ApartmentForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (apartmentService.checkIfApartmentExists(apartmentForm.getAddress().getLatitude(),
                apartmentForm.getAddress().getLongitude(),
                apartmentForm.getOfficeId())) {
            return new ResponseEntity<>("Fail -> Apartment is already created!",
                    HttpStatus.BAD_REQUEST);
        }

        Apartment createdApartment = apartmentService.createApartment(apartmentForm);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apartmentMapper.getApartmentDTO(createdApartment));
    }

    @GetMapping("/apartments/unoccupied")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodationApartments(@Validated @RequestBody DateForm form) {
        return new ResponseEntity<>(apartmentService.getAllUnoccupiedAccommodationApartments(
                form.getStartDate(), form.getFinishDate()), HttpStatus.OK);
    }

    @GetMapping("/apartments/unoccupied/{officeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedApartmentsByOfficeId(@Validated @RequestBody DateForm form,
                                                            @PathVariable("officeId") int officeId) {
        long id = officeId;
        return new ResponseEntity<>(apartmentService.getAllUnoccupiedApartmentsByOfficeId(
                form.getStartDate(), form.getFinishDate(), id), HttpStatus.OK);
    }

}
