package lt.fivethreads.controller;

import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public UserDTO getUserDTOByID(@PathVariable("userId") int userId) {
        long id = userId;
        return userService.getUserDTOByID(id);
    }

    @DeleteMapping("/admin/user/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("userID") int userId) {
        long id = userId;
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/admin/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Validated @RequestBody UserDTO user) {
        userService.updateUser(user);
        return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
    }

    @PostMapping("/admin/user/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegistrationForm registrationForm) {
        userService.createUser(registrationForm);
        return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
    }
}
