package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ChangePasswordForm;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.exception.file.EmailNotExists;
import lt.fivethreads.exception.file.UserIDNotExists;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(e -> userMapper.getUserDTO(e))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserByID(Long id) throws UserIDNotExists {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIDNotExists());
        return user;
    }

    @Override
    public UserDTO getUserDTOByID(Long id) throws UserIDNotExists {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIDNotExists());
        return userMapper.getUserDTO(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) throws UserIDNotExists
    {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserIDNotExists());
        if (this.checkIfEmailExists(userDTO.getEmail()) && !userDTO.getEmail().equals(user.getEmail()) ) {
            throw new EmailAlreadyExists();
        }
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setId(userDTO.getId());
        user.setPhone(userDTO.getPhone());
        user.setRoles(userMapper.getRoles(userDTO.getRole()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createUser(RegistrationForm user) throws  EmailAlreadyExists
    {
        if (this.checkIfEmailExists(user.getEmail())) {
                throw new EmailAlreadyExists();
            }
        User user_to_create = userMapper.convertRegistrationUserToUser(user);
        userRepository.save(user_to_create);
    }

    @Override
    public void changePassword(ChangePasswordForm changePasswordForm) throws EmailNotExists
    {
        User user = userRepository.findByEmail(changePasswordForm.getEmail())
                .orElseThrow(() -> new EmailNotExists());
        user.setPassword(encoder.encode(changePasswordForm.getPassword()));
    }

}
