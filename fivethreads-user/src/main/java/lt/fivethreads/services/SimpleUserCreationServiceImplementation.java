package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class SimpleUserCreationServiceImplementation implements UserCreationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    public User createNewUser(RegistrationForm registrationForm) {
        User user =  userMapper.convertRegistrationUserToUser(registrationForm);
        if (userService.checkIfEmailExists(user.getEmail())) {
            throw new EmailAlreadyExists();
        }
        User created_user = userRepository.save(user);
        return created_user;
    }
}
