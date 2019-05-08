package lt.fivethreads.services;

import lt.fivethreads.Mapper.AddressMapper;
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
    RoomRepository roomRepository;

    @Autowired
    OfficeMapper officeMapper;

    @Autowired
    AddressMapper addressMapper;

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

    public TripAccommodationDTO updateTripAccommodation(TripAccommodationDTO tripAccommodationDTO) {
        tripAccommodationValidation.checkFinishStartDates(tripAccommodationDTO.getAccommodationStart(),
                tripAccommodationDTO.getAccommodationFinish(), "Finish date is earlier than start date.");
        tripAccommodationValidation.checkStartDateToday(tripAccommodationDTO.getAccommodationStart());

        TripMember tripMember = tripMemberRepository.findById(tripAccommodationDTO.getTripMemberId());
        Trip trip = tripMember.getTrip();

        tripAccommodationValidation.checkTripAccommodationDatesAgainstTripDates(tripAccommodationDTO.getAccommodationStart(),
                tripAccommodationDTO.getAccommodationFinish(), trip);

        TripAccommodation accommodation_to_update = tripAccommodationRepository.findByID(tripAccommodationDTO.getId());
       if(tripAccommodationDTO.getAccommodationType() == AccommodationType.HOTEL) {

            tripAccommodationValidation.hotelRequiredFields(tripAccommodationDTO.getHotelName(),
                    tripAccommodationDTO.getHotelAddress(), tripAccommodationDTO.getPrice());
            Address address = addressMapper.convertFullAddressToAddress(tripAccommodationDTO.getHotelAddress());
            if (accommodation_to_update.getHotelAddress() != null
            && accommodation_to_update.getAccommodationType() == AccommodationType.HOTEL) address.setId(accommodation_to_update.getHotelAddress().getId());
            accommodation_to_update.setHotelAddress(address);
            accommodation_to_update.setHotelName(tripAccommodationDTO.getHotelName());
            accommodation_to_update.setPrice(tripAccommodationDTO.getPrice());
        }else{

            Room room = roomRepository.findById(tripAccommodationDTO.getRoomId());
            accommodation_to_update.setRoom(room);
            accommodation_to_update.setHotelName(room.getApartment().getOffice().getName());
            accommodation_to_update.setHotelAddress(room.getApartment().getAddress());
            accommodation_to_update.setPrice(null);
        }
        accommodation_to_update.setAccommodationType(tripAccommodationDTO.getAccommodationType());
        accommodation_to_update.setAccommodationFinish(tripAccommodationDTO.getAccommodationFinish());
        accommodation_to_update.setAccommodationStart(tripAccommodationDTO.getAccommodationStart());

        tripAccommodationRepository.updateTripAccommodation(accommodation_to_update);

        return tripAccommodationMapper.getTripAccommodationDTO(tripAccommodationRepository.updateTripAccommodation(accommodation_to_update));
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

    public void deleteTripAccommodation(Long id) {
        tripAccommodationRepository.deleteTripAccommodation(id); }

}
