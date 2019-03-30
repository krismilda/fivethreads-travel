package lt.fivethreads.controller;

import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class OfficeController {

    @Autowired
    OfficeService officeService;

    @GetMapping("/offices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public List<OfficeDTO> getAllOffices() {
        return officeService.getAllOffices();
    }

    @GetMapping("/offices/{officeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public OfficeDTO getOfficeById(@PathVariable("officeId") int officeId) {
        long id = officeId;
        return officeService.getOfficeById(id);
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
        officeService.updateOffice(officeDTO);
        return new ResponseEntity<>("Office updated successfully!", HttpStatus.OK);
    }

    @PostMapping("/admin/offices/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerOffice(@Validated @RequestBody OfficeForm registrationForm) {
        if (registrationForm == null) {
            return new ResponseEntity<>("Fail -> OfficeForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (officeService.checkIfOfficeExists(registrationForm.getName(),
                registrationForm.getAddress())) {
            return new ResponseEntity<>("Fail -> Office is already created!",
                    HttpStatus.BAD_REQUEST);
        }
        officeService.createOffice(registrationForm);
        return new ResponseEntity<>("Office created successfully!", HttpStatus.OK);
    }
}
