package lt.fivethreads.services;

import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.ExtendedUserDTO;

import java.util.List;

public interface UserService {
    List<ExtendedUserDTO> getAllUser();

    ExtendedUserDTO getUserByID(Long id);

    ExtendedUserDTO updateUser(ExtendedUserDTO user);

    void deleteUser(Long id);

    ExtendedUserDTO createUser(RegistrationForm user);

    boolean checkIfEmailExists(String email);

    void createUsers(List<ExtendedUserDTO> users);
}
