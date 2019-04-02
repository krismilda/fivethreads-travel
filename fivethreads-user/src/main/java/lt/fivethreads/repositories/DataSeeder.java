package lt.fivethreads.repositories;

import lt.fivethreads.entities.Role;
import lt.fivethreads.entities.RoleName;
import lt.fivethreads.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Component
public class DataSeeder {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void loadData() {
//        Role admin_role = new Role();
//        admin_role.setName(RoleName.ROLE_ADMIN);
//        roleRepository.save(admin_role);
//
//        Role organizer_role = new Role();
//        organizer_role.setName(RoleName.ROLE_ORGANIZER);
//        roleRepository.save(organizer_role);
//
//        Role user_role = new Role();
//        user_role.setName(RoleName.ROLE_USER);
//        roleRepository.save(user_role);
//
//        HashSet<Role> userRoles = new HashSet<>();
//        userRoles.add(admin_role);
//        User user = new User("Admin", "Admin", "admin@email.com", encoder.encode("admin"), "860000000");
//        user.setRoles(userRoles);
//        userRepository.save(user);
    }
}
