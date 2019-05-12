package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SimpleUserCreationServiceImplementation implements UserCreationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public User createNewUser(User user) {
        if (userService.checkIfEmailExists(user.getEmail())) {
            throw new EmailAlreadyExists();
        }
        User created_user = userRepository.save(user);
        return created_user;
    }
}
