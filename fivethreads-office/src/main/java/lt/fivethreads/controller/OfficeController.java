package lt.fivethreads.controller;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.DateForm;
import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.request.OfficeForm;
import lt.fivethreads.mapper.OfficeMapper;
import lt.fivethreads.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OfficeController {

    @Autowired
    OfficeService officeService;

    @Autowired
    OfficeMapper officeMapper;

    @GetMapping("/offices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getAllOffices() {
        return new ResponseEntity<>(officeService.getAllOffices(), HttpStatus.OK);
    }

    @GetMapping("/offices/{officeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getOfficeById(@PathVariable("officeId") int officeId) {
        long id = officeId;
        Office office = officeService.getOfficeById(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag("\"" + office.getVersion() + "\"")
                .body(officeMapper.getOfficeDTO(office));
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
    public ResponseEntity<?> updateOffice(@Validated @RequestBody OfficeDTO officeDTO, WebRequest request) {
        String version = request.getHeader("If-Match");
        if(officeService.checkIfModified(officeDTO.getId(), version)){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        Office office = officeService.updateOffice(officeDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag("\"" + office.getVersion() + "\"")
                .body(officeMapper.getOfficeDTO(office));
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
        Office createdOffice = officeService.createOffice(registrationForm);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .eTag("\"" + createdOffice.getVersion() + "\"")
                .body(officeMapper.getOfficeDTO(createdOffice));
    }
    @GetMapping("/offices/unoccupied")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodationOffices(@Validated @RequestBody DateForm form){
        return new ResponseEntity<>(officeService.getAllUnoccupiedAccommodationOffices(
                form.getStartDate(), form.getFinishDate()), HttpStatus.OK);
    }
}
