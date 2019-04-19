package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.TripAccommodationForm;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripAccommodationMapper;
import lt.fivethreads.repositories.TripAccommodationRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.validation.TripAccommodationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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


    public TripAccommodationDTO getTripAccommodation(long tripAccommodationId){
        TripAccommodation tripAccommodation = tripAccommodationRepository.findByID(tripAccommodationId);
        TripAccommodationDTO tripAccommodationDTO = tripAccommodationMapper.getTripAccommodationDTO(tripAccommodation);
        return  tripAccommodationDTO;
    }

    public TripAccommodationDTO createTripAccommodation(TripAccommodationForm tripAccommodationForm) throws WrongTripData {

        tripAccommodationValidation.checkFinishStartDates(tripAccommodationForm.getAccommodationStart(),
                tripAccommodationForm.getAccommodationFinish(), "Finish date is earlier than start date.");
        tripAccommodationValidation.checkStartDateToday(tripAccommodationForm.getAccommodationStart());
        tripAccommodationValidation.checkPrice(tripAccommodationForm.getPrice());

        Trip trip = tripMemberRepository.findById(tripAccommodationForm.getTripMemberId()).getTrip();
        tripAccommodationValidation.checkTripAccommodationDatesAgainstTripDates(tripAccommodationForm.getAccommodationStart(),
                tripAccommodationForm.getAccommodationFinish(), trip);
        TripAccommodationDTO tripAccommodationDTO =
                tripAccommodationMapper
                        .getTripAccommodationDTO(tripAccommodationMapper
                                .convertCreateTripAccommodationFormToTripAccommodation(tripAccommodationForm));

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
