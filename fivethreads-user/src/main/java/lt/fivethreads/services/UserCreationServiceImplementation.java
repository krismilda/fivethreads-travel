package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserCreationServiceImplementation implements UserCreationService {

    UserRepository userRepository;
    UserMapper userMapper;

    public UserCreationServiceImplementation(UserRepository userRepository, UserMapper userMapper){
        this.userRepository=userRepository;
        this.userMapper=userMapper;
    }

    public User createNewUser(RegistrationForm registrationForm) {
        User user =  userMapper.convertRegistrationUserToUser(registrationForm);
        User created_user = userRepository.save(user);
        return created_user;
    }
}
