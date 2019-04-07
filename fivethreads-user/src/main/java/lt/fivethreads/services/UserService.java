package lt.fivethreads.services;

import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser();

    UserDTO getUserByID(Long id);

    UserDTO updateUser(UserDTO user);

    void deleteUser(Long id);

    UserDTO createUser(RegistrationForm user);

    boolean checkIfEmailExists(String email);

    void createUsers(List<UserDTO> users);
}
