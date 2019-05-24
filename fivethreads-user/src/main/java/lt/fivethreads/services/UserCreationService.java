package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;

public interface UserCreationService {
    User createNewUser(RegistrationForm user);
}
