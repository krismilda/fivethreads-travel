package lt.fivethreads.web;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.repositories.RoleRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.repositories.UserRepository;
import lt.fivethreads.services.OfficeService;
import lt.fivethreads.services.TripService;
import lt.fivethreads.services.UserCreationService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.spi.RegisterableService;
import java.util.*;

@Component
public class DataSeeder {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    OfficeService officeService;

    @Autowired
    TripService tripService;

    @Autowired
    UserCreationService userCreationService;

    @PostConstruct
    public void loadData() {
        /*Role admin_role = new Role();
        admin_role.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(admin_role);

        Role organizer_role = new Role();
        organizer_role.setName(RoleName.ROLE_ORGANIZER);
        roleRepository.save(organizer_role);

        Role user_role = new Role();
        user_role.setName(RoleName.ROLE_USER);
        roleRepository.save(user_role);

        OfficeForm officeForm = new OfficeForm();
        FullAddressDTO fullAddressDTO = new FullAddressDTO();
        fullAddressDTO.setCity("Siauliai");
        fullAddressDTO.setCountry("Lietuva");
        fullAddressDTO.setFlatNumber("50");
        fullAddressDTO.setHouseNumber("50");
        fullAddressDTO.setLatitude(555555);
        fullAddressDTO.setLatitude(88888888);
        fullAddressDTO.setStreet("Tilzes");
        officeForm.setAddress(fullAddressDTO);
        officeForm.setName("Office nr.1");

        FullAddressDTO fullAddressDTO2 = new FullAddressDTO();
        fullAddressDTO2.setCity("Vilnius");
        fullAddressDTO2.setCountry("Lietuva");
        fullAddressDTO2.setFlatNumber("20");
        fullAddressDTO2.setHouseNumber("20");
        fullAddressDTO2.setLatitude(555522255);
        fullAddressDTO2.setLatitude(88822888);
        fullAddressDTO2.setStreet("Ukmerges");
        officeService.createOffice(officeForm);

        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("admin@gmail.com");
        registrationForm.setFirstname("Admin_firstname");
        registrationForm.setLastname("Admin_lastname");
        registrationForm.setOfficeId(Long.parseLong("1"));
        registrationForm.setPassword("admin123");
        registrationForm.setPhone("852369000");
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_ADMIN");
        registrationForm.setRole(roles);
        userCreationService.createNewUser(registrationForm);

        RegistrationForm registrationForm2 = new RegistrationForm();
        registrationForm2.setEmail("organizer@gmail.com");
        registrationForm2.setFirstname("Organizer_firstname");
        registrationForm2.setLastname("Organizer_lastname");
        registrationForm2.setOfficeId(Long.parseLong("1"));
        registrationForm2.setPassword("organizer123");
        registrationForm2.setPhone("852369000");
        Set<String> roles2 = new HashSet<String>();
        roles2.add("ROLE_ORGANIZER");
        registrationForm2.setRole(roles2);
        userCreationService.createNewUser(registrationForm2);

        RegistrationForm registrationForm3= new RegistrationForm();
        registrationForm3.setEmail("user@gmail.com");
        registrationForm3.setFirstname("User_firstname");
        registrationForm3.setLastname("User_lastname");
        registrationForm3.setOfficeId(Long.parseLong("1"));
        registrationForm3.setPassword("user123");
        registrationForm3.setPhone("852369000");
        Set<String> roles3 = new HashSet<String>();
        roles3.add("ROLE_USER");
        registrationForm3.setRole(roles3);
        userCreationService.createNewUser(registrationForm3);

        CreateTripForm createTripForm = new CreateTripForm();
        createTripForm.setArrival(fullAddressDTO2);
        createTripForm.setDeparture(fullAddressDTO);
        TripMemberDTO tripMemberDTO = new TripMemberDTO();
        tripMemberDTO.setEmail("user@gmail.com");
        tripMemberDTO.setIsAccommodationNeeded(false);
        tripMemberDTO.setIsFlightTickedNeeded(false);
        tripMemberDTO.setIsCarNeeded(false);
        List<TripMemberDTO> tripMembers = new ArrayList<>();
        tripMembers.add(tripMemberDTO);
        createTripForm.setTripMembers(tripMembers);
        createTripForm.setStartDate(new Date(new Date().getTime()+2*(24*60*60*1000)));
        createTripForm.setFinishDate(new Date(new Date().getTime()+6*(24*60*60*1000)));
        createTripForm.setIsFlexible(false);
        tripService.createTrip(createTripForm, "organizer@gmail.com");*/
    }
}
