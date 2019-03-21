package lt.fivethreads.repositories;

import lt.fivethreads.entities.Role;
import lt.fivethreads.entities.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataSeeder {

@Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void loadData() {
      /* Role admin_role = new Role ();
        admin_role.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(admin_role);
        Role organizer_role = new Role ();
        organizer_role.setName(RoleName.ROLE_ORGANIZER);
        roleRepository.save(organizer_role);
        Role user_role = new Role ();
        user_role.setName(RoleName.ROLE_USER);
        roleRepository.save(user_role);*/
    }
}
