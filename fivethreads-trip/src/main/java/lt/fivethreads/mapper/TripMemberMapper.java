package lt.fivethreads.mapper;

import lt.fivethreads.entities.CarTicket;
import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.CarTicketDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripMemberMapper {
    @Autowired
    UserService userService;

    @Autowired
    TripMemberRepository tripMemberRepository;

    public TripMemberDTO convertTripMemberToTripMemberDAO(TripMember tripMember) {
        TripMemberDTO tripMemberDTO = new TripMemberDTO();
        tripMemberDTO.setEmail(tripMember.getUser().getEmail());
        tripMemberDTO.setIsAccommodationNeeded(tripMember.getIsAccommodationNeeded());
        tripMemberDTO.setIsCarNeeded(tripMember.getIsCarNeeded());
        tripMemberDTO.setIsFlightTickedNeeded(tripMember.getIsFlightTickedNeeded());
        if (tripMember.getCarTicket() != null && tripMember.getIsCarNeeded() == true) {
            tripMemberDTO.setCarTicketDTO(convertCarTicketToCarTicketDAO(tripMember.getCarTicket()));
        }
        if (tripMember.getTripAccommodation() != null && tripMember.getIsAccommodationNeeded() == true) {
            tripMemberDTO.setAccommodationDTO(convertAccomodationToAccomodationDAO(tripMember.getTripAccommodation()));
        }
        return tripMemberDTO;
    }

    public TripMember convertTripMemberDAOtoTripMember(TripMemberDTO tripMemberDTO) {
        User user = userService.getUserByEmail(tripMemberDTO.getEmail());
        TripMember tripMember = new TripMember();
        tripMember.setUser(user);
        tripMember.setIsAccommodationNeeded(tripMemberDTO.getIsAccommodationNeeded());
        if (tripMemberDTO.getIsAccommodationNeeded() && tripMemberDTO.getAccommodationDTO() != null) {
            TripAccommodation tripAccommodation = convertAccommodationDAOToTripAccommodation(tripMemberDTO.getAccommodationDTO());
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        tripMember.setIsFlightTickedNeeded(tripMemberDTO.getIsFlightTickedNeeded());
        if (tripMemberDTO.getIsCarNeeded() && tripMemberDTO.getCarTicketDTO() != null) {
            CarTicket carTicket = convertCarTicketDaoToCarTicket(tripMemberDTO.getCarTicketDTO());
            carTicket.setTripMember(tripMember);
            tripMember.setCarTicket(carTicket);
        }
        tripMember.setIsCarNeeded(tripMemberDTO.getIsCarNeeded());
        return tripMember;
    }


    public CarTicket convertCarTicketDaoToCarTicket(CarTicketDTO carTicketDTO) {
        CarTicket carTicket = new CarTicket();
        carTicket.setCarRentStart(carTicketDTO.getCarRentStart());
        carTicket.setCarRentFinish(carTicketDTO.getCarRentFinish());
        return carTicket;
    }

    public TripAccommodation convertAccommodationDAOToTripAccommodation(TripAccommodationDTO accommodationDTO){
        TripAccommodation tripAccommodation = new TripAccommodation();
        tripAccommodation.setAccommodationStart(accommodationDTO.getAccommodationStart());
        tripAccommodation.setAccommodationFinish(accommodationDTO.getAccommodationFinish());
        return tripAccommodation;
    }

    public CarTicketDTO convertCarTicketToCarTicketDAO(CarTicket carTicket) {
        CarTicketDTO carTicketDTO = new CarTicketDTO();
        carTicketDTO.setCarRentFinish(carTicket.getCarRentFinish());
        carTicketDTO.setCarRentStart(carTicket.getCarRentStart());
        return carTicketDTO;
    }

    public TripAccommodationDTO convertAccomodationToAccomodationDAO(TripAccommodation tripAccommodation) {
        TripAccommodationDTO accommodationDTO = new TripAccommodationDTO();
        accommodationDTO.setAccommodationStart(tripAccommodation.getAccommodationStart());
        accommodationDTO.setAccommodationFinish(tripAccommodation.getAccommodationFinish());
        return accommodationDTO;
    }
}
