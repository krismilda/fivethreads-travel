package lt.fivethreads.controller;

import lt.fivethreads.entities.request.DateForm;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OfficeController {

    @Autowired
    OfficeService officeService;

    @GetMapping("/offices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getAllOffices() {
        return new ResponseEntity<>(officeService.getAllOffices(), HttpStatus.OK);
    }

    @GetMapping("/offices/{officeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getOfficeById(@PathVariable("officeId") int officeId) {
        long id = officeId;
        return new ResponseEntity<>(officeService.getOfficeById(id), HttpStatus.OK);
    }

    @DeleteMapping("/admin/offices/{officeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOffice(@PathVariable("officeId") int officeId) {
        long id = officeId;
        officeService.deleteOffice(id);
        return new ResponseEntity<>("Office deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/offices/office")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> updateOffice(@Validated @RequestBody OfficeDTO officeDTO) {
        OfficeDTO updatedOffice = officeService.updateOffice(officeDTO);
        return new ResponseEntity<>(updatedOffice, HttpStatus.OK);
    }

    @PostMapping("/admin/offices/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerOffice(@Validated @RequestBody OfficeForm registrationForm) {
        if (registrationForm == null) {
            return new ResponseEntity<>("Fail -> OfficeForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (officeService.checkIfOfficeExists(registrationForm.getAddress().getLatitude(),
                registrationForm.getAddress().getLongitude(), registrationForm.getName())) {
            return new ResponseEntity<>("Fail -> Office is already created!",
                    HttpStatus.BAD_REQUEST);
        }
        OfficeDTO createdOffice = officeService.createOffice(registrationForm);
        return new ResponseEntity<>(createdOffice, HttpStatus.CREATED);
    }
    @GetMapping("/offices/unoccupied")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodationOffices(@Validated @RequestBody DateForm form){
        return new ResponseEntity<>(officeService.getAllUnoccupiedAccommodationOffices(
                form.getStartDate(), form.getFinishDate()), HttpStatus.OK);
    }
}
