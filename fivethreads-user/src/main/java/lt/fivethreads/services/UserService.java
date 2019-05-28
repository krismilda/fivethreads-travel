package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ChangePasswordForm;
import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.exception.file.EmailNotExists;
import lt.fivethreads.exception.file.UserIDNotExists;

import java.util.List;

public interface UserService {
    List<ExtendedUserDTO> getAllUser();

    User getUserByID(Long id);

    User updateUser(ExtendedUserDTO user) throws UserIDNotExists;

    void deleteUser(Long id) throws UserIDNotExists;

    boolean checkIfEmailExists(String email);

    User getUserByEmail(String email) throws UserIDNotExists;

    void changePassword(ChangePasswordForm changePasswordForm, String email) throws EmailNotExists;

    void createUsers(List<ExtendedUserDTO> users);

    Boolean checkIfModified(Long userID, String version);
}
