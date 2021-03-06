package lt.fivethreads.controller;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ChangePasswordForm;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.exception.user.UserNotFoundException;
import lt.fivethreads.exception.user.UserWasModified;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.services.UserCreationService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCreationService userCreationService;

    @GetMapping("/admin/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExtendedUserDTO> getUserByID(@PathVariable("userId") int userId) {
        long id = userId;
        User user = userService.getUserByID(id);
        return ResponseEntity
                .ok()
                .body(userMapper.getUserDTO(user));
    }

    @GetMapping("/loggedUser/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('USER')")
    public ResponseEntity<ExtendedUserDTO> getloggedUser() {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity
                .ok()
                .body(userMapper.getUserDTO(user));
    }

    @PutMapping("/loggedUser/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('USER')")
    public ResponseEntity<ExtendedUserDTO> updateloggedUser(@Validated @RequestBody ExtendedUserDTO user) {
        if(userService.checkIfModified(user.getId(), user.getVersion().toString())){
            throw new UserWasModified();
        }
        User updatedUserDTO = userService.updateLoggedInUser(user,SecurityContextHolder.getContext().getAuthentication().getName() );
        return ResponseEntity
                .ok()
                .body(userMapper.getUserDTO(updatedUserDTO));
    }

    @PutMapping("/admin/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Validated @RequestBody ExtendedUserDTO user){
        if(userService.checkIfModified(user.getId(), user.getVersion().toString())){
            throw new UserWasModified();
        }
        User updatedUserDTO = userService.updateUser(user);
        return ResponseEntity
                .ok()
                .body(userMapper.getUserDTO(updatedUserDTO));
    }

    @PostMapping("/admin/user/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegistrationForm registrationForm) {
        User createdUser = userCreationService.createNewUser(registrationForm);
        return ResponseEntity
                .ok()
                .body(userMapper.getUserDTO(createdUser));
    }

    @PutMapping("/loggedUser/changePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody @Validated ChangePasswordForm changePasswordForm) {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.changePassword(changePasswordForm, SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>("Password changed successfully!", HttpStatus.OK);
    }
}
