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
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserByID(@PathVariable("userId") int userId) {
        long id = userId;
        return userService.getUserByID(id);
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
        if (registrationForm == null) {
            return new ResponseEntity<>("Fail -> RegistrationForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.checkIfEmailExists(registrationForm.getEmail())) {
            return new ResponseEntity<>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }
        userService.createUser(registrationForm);
        return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
    }
}
