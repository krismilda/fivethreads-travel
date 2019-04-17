package lt.fivethreads.mapper;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AccommodationDTO;
import lt.fivethreads.entities.request.CarTicketDTO;
import lt.fivethreads.entities.request.FlightTicketDTO;
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
        if (tripMember.getFlightTicket() != null && tripMember.getIsFlightTickedNeeded() == true) {
            tripMemberDTO.setFlightTicketDTO(convertFlightTicketToFlightTicketDAO(tripMember.getFlightTicket()));
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

    public TripAccommodation convertAccommodationDAOToTripAccommodation(AccommodationDTO accommodationDTO) {
        TripAccommodation tripAccommodation = new TripAccommodation();
        tripAccommodation.setAccommodationStart(accommodationDTO.getAccommodationStart());
        tripAccommodation.setAccommodationFinish(accommodationDTO.getAccommodationFinish());
        return tripAccommodation;
    }

    public CarTicketDTO convertCarTicketToCarTicketDAO(CarTicket carTicket) {
        CarTicketDTO carTicketDTO = new CarTicketDTO();
        carTicketDTO.setCarRentFinish(carTicket.getCarRentFinish());
        carTicketDTO.setCarRentStart(carTicket.getCarRentStart());
        if(carTicket.getFile()!=null){
            for (File file : carTicket.getFile()
            ) {
                carTicketDTO.getFileID().add(file.getId());
            }
        }

        return carTicketDTO;
    }

    public AccommodationDTO convertAccomodationToAccomodationDAO(TripAccommodation tripAccommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setAccommodationStart(tripAccommodation.getAccommodationStart());
        accommodationDTO.setAccommodationFinish(tripAccommodation.getAccommodationFinish());
        if(tripAccommodation.getFile()!=null){
            for (File file : tripAccommodation.getFile()
            ) {
                accommodationDTO.getFileID().add(file.getId());
            }
        }
        return accommodationDTO;
    }

    public FlightTicketDTO convertFlightTicketToFlightTicketDAO(FlightTicket flightTicket) {
        FlightTicketDTO flightTicketDTO = new FlightTicketDTO();
        if(flightTicket.getFile()!=null){
            for (File file : flightTicket.getFile()
            ) {
                flightTicketDTO.getFileID().add(file.getId());
            }
        }
        return flightTicketDTO;
    }
}
