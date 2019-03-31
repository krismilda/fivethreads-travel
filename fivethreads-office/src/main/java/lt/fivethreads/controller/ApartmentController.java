package lt.fivethreads.controller;

import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.services.ApartmentService;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ApartmentController {

    @Autowired
    ApartmentService apartmentService;

    @GetMapping("/apartments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public List<ApartmentDTO> getAllApartments() {
        return apartmentService.getAllApartments();
    }

    @GetMapping("/apartments/{apartmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ApartmentDTO getApartmentById(@PathVariable("apartmentId") int apartmentId) {
        long id = apartmentId;
        return apartmentService.getApartmentById(id);
    }

    @DeleteMapping("/admin/apartments/{apartmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteApartment(@PathVariable("apartmentId") int apartmentId) {
        long id = apartmentId;
        apartmentService.deleteApartment(id);
        return new ResponseEntity<>("Apartment deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/apartments/apartment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> updateApartment(@Validated @RequestBody ApartmentDTO apartmentDTO) {
        apartmentService.updateApartment(apartmentDTO);
        return new ResponseEntity<>("Apartment updated successfully!", HttpStatus.OK);
    }

    @PostMapping("/admin/apartments/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerApartment(@Validated @RequestBody ApartmentForm apartmentForm) {
        if (apartmentForm == null) {
            return new ResponseEntity<>("Fail -> ApartmentForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (apartmentService.checkIfApartmentExists(apartmentForm.getAddress(),
                apartmentForm.getOfficeId())) {
            return new ResponseEntity<>("Fail -> Apartment is already created!",
                    HttpStatus.BAD_REQUEST);
        }

        apartmentService.createApartment(apartmentForm);
        return new ResponseEntity<>("Apartment created successfully!", HttpStatus.OK);
    }
}
