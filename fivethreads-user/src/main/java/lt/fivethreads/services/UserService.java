package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ChangePasswordForm;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.exception.file.EmailNotExists;
import lt.fivethreads.exception.file.UserIDNotExists;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser();

    User getUserByID(Long id)throws UserIDNotExists;

    UserDTO updateUser(UserDTO user)throws UserIDNotExists;

    void deleteUser(Long id)throws UserIDNotExists;

    UserDTO createUser(RegistrationForm user)throws  EmailAlreadyExists;

    boolean checkIfEmailExists(String email);

    User getUserByEmail(String email) throws UserIDNotExists;

    void changePassword(ChangePasswordForm changePasswordForm) throws EmailNotExists;

    void createUsers(List<UserDTO> users);
}
