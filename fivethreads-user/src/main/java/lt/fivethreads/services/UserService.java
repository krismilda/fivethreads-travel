package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ChangePasswordForm;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.exception.file.EmailNotExists;
import lt.fivethreads.exception.file.UserIDNotExists;

import java.util.List;

public interface UserService {
    List<ExtendedUserDTO> getAllUser();

    ExtendedUserDTO getUserByID(Long id);

    ExtendedUserDTO updateUser(ExtendedUserDTO user)throws UserIDNotExists;

    void deleteUser(Long id)throws UserIDNotExists;

    ExtendedUserDTO createUser(RegistrationForm user)throws  EmailAlreadyExists;

    boolean checkIfEmailExists(String email);

    User getUserByEmail(String email) throws UserIDNotExists;

    void changePassword(ChangePasswordForm changePasswordForm) throws EmailNotExists;

    void createUsers(List<ExtendedUserDTO> users);
}
