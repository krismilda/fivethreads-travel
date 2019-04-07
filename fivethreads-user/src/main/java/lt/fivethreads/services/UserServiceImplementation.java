package lt.fivethreads.services;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.RegistrationForm;
import lt.fivethreads.entities.request.UserDTO;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public UserDTO getUserByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong userid"));
        return userMapper.getUserDTO(user);
    }

    public UserDTO updateUser(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong userid"));
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        user.setId(userDTO.getId());
        user.setPhone(userDTO.getPhone());
        user.setRoles(userMapper.getRoles(userDTO.getRole()));

        if(!(userDTO.getOfficeId() == null)){
            Office office = new Office();
            office.setId(userDTO.getOfficeId());
            user.setOffice(office);
        }

        return userMapper.getUserDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createUsers(List<UserDTO> users) {
        List<User> userEntities = new ArrayList<>();
        for (UserDTO user: users) {
            User userEntity = userMapper.getUser(user);
            userEntities.add(userEntity);
        }

        userRepository.saveAll(userEntities);
    }

    public UserDTO createUser(RegistrationForm user) {
        User user_to_create = userMapper.convertRegistrationUserToUser(user);
        return userMapper.getUserDTO(userRepository.save(user_to_create));
    }
}
