package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.ApartmentMapper;
import lt.fivethreads.mapper.OfficeMapper;
import lt.fivethreads.mapper.RoomMapper;
import lt.fivethreads.mapper.TripAccommodationMapper;
import lt.fivethreads.repositories.RoomRepository;
import lt.fivethreads.repositories.TripAccommodationRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.validation.TripAccommodationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TripAccommodationServiceImplementation implements TripAccommodationService {

    @Autowired
    TripAccommodationMapper tripAccommodationMapper;

    @Autowired
    TripAccommodationRepository tripAccommodationRepository;

    @Autowired
    TripAccommodationValidation tripAccommodationValidation;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    OfficeMapper officeMapper;


    public TripAccommodationDTO getTripAccommodation(long tripAccommodationId){
        TripAccommodation tripAccommodation = tripAccommodationRepository.findByID(tripAccommodationId);
        TripAccommodationDTO tripAccommodationDTO = tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation);
        return  tripAccommodationDTO;
    }

    public TripAccommodationDTO createTripAccommodation(TripAccommodationForm tripAccommodationForm) throws WrongTripData {

        tripAccommodationValidation.checkFinishStartDates(tripAccommodationForm.getAccommodationStart(),
                tripAccommodationForm.getAccommodationFinish(), "Finish date is earlier than start date.");
        tripAccommodationValidation.checkStartDateToday(tripAccommodationForm.getAccommodationStart());


        if(tripAccommodationForm.getAccommodationType() == AccommodationType.HOTEL)
        tripAccommodationValidation.hotelRequiredFields(tripAccommodationForm.getHotelName(),
                tripAccommodationForm.getHotelAddress(), tripAccommodationForm.getPrice());

        TripMember tripMember = tripMemberRepository.findById(tripAccommodationForm.getTripMemberId());
        Trip trip = tripMember.getTrip();

        tripAccommodationValidation.checkTripAccommodationDatesAgainstTripDates(tripAccommodationForm.getAccommodationStart(),
                tripAccommodationForm.getAccommodationFinish(), trip);
        TripAccommodation created = tripAccommodationMapper
                .convertCreateTripAccommodationFormToTripAccommodation(tripAccommodationForm);
        TripAccommodation tripAccommodation = tripAccommodationRepository.saveTripAccommodation(created);
        tripMember.setTripAccommodation(created);
        tripMemberRepository.updateTripMember(tripMember);

        TripAccommodationDTO tripAccommodationDTO =
                tripAccommodationMapper
                        .getTripAccommodationDTO(tripAccommodation);

        return tripAccommodationDTO;
    }

    public List<TripAccommodationDTO> getAllTripAccommodations() {
        List<TripAccommodation> tripAccommodationList = tripAccommodationRepository.getAll();
        List<TripAccommodationDTO> tripAccommodationDTOList = new ArrayList<>();
        for (TripAccommodation tripAccommodation : tripAccommodationList){
            tripAccommodationDTOList.add(tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation));
        }
        return tripAccommodationDTOList;
    }

    public List<TripAccommodationDTO> getAllTripAccommodationsByUser(long userId) {
        List<TripAccommodation> tripAccommodationList = tripAccommodationRepository.getAllByUser(userId);
        List<TripAccommodationDTO> tripAccommodationDTOList = new ArrayList<>();
        for (TripAccommodation tripAccommodation : tripAccommodationList){
            tripAccommodationDTOList.add(tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation));
        }
        return tripAccommodationDTOList;
    }

    public List<TripAccommodationDTO> getAllTripAccommodationsByTrip(long tripId) {
        List<TripAccommodation> tripAccommodationList = tripAccommodationRepository.getAllByTrip(tripId);
        List<TripAccommodationDTO> tripAccommodationDTOList = new ArrayList<>();
        for (TripAccommodation tripAccommodation : tripAccommodationList){
            tripAccommodationDTOList.add(tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation));
        }
        return tripAccommodationDTOList;

    }

    public List<TripAccommodationDTO> getAllTripAccommodationsByApartment(long apartmentId) {
        List<TripAccommodation> tripAccommodationList = tripAccommodationRepository.getAllByApartment(apartmentId);
        List<TripAccommodationDTO> tripAccommodationDTOList = new ArrayList<>();
        for (TripAccommodation tripAccommodation : tripAccommodationList){
            tripAccommodationDTOList.add(tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation));
        }
        return tripAccommodationDTOList;
    }
}
