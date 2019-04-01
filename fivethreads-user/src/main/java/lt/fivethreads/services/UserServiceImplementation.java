package lt.fivethreads.services;

import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.exception.file.EmailAlreadyExists;
import lt.fivethreads.exception.file.UserIDNotExists;
import lt.fivethreads.exception.file.WrongUserData;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(e -> userMapper.getUserDTO(e))
                .collect(Collectors.toList());
    }
    public User getUserByID(Long id) throws UserIDNotExists {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIDNotExists());
        return user;
    }

    public UserDTO getUserDTOByID(Long id) throws UserIDNotExists {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIDNotExists());
        return userMapper.getUserDTO(user);
    }

    public void updateUser(UserDTO userDTO) throws UserIDNotExists
    {

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserIDNotExists());
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setId(userDTO.getId());
        user.setPhone(userDTO.getPhone());
        user.setRoles(userMapper.getRoles(userDTO.getRole()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void createUser(RegistrationForm user) throws WrongUserData, EmailAlreadyExists
    {
        if (user == null) {
            throw new WrongUserData();
        }

        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getFirstname() == null ||
                user.getLastname() == null ||
                user.getRole() ==null
        ){
            throw  new WrongUserData();
        }

        if (this.checkIfEmailExists(user.getEmail())) {
                throw new EmailAlreadyExists();
            }
        User user_to_create = userMapper.convertRegistrationUserToUser(user);
        userRepository.save(user_to_create);
    }
}
