package lt.fivethreads.services;

import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser();

    UserDTO getUserByID(Long id);

    void updateUser(UserDTO user);

    void deleteUser(Long id);

    void createUser(RegistrationForm user);

    boolean checkIfEmailExists(String email);
}
