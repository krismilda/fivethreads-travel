package lt.fivethreads.services;

import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.ExtendedUserDTO;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.entities.rest.DateRangeDTO;
import lt.fivethreads.entities.rest.TripCount;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.entities.rest.*;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.OfficeMapper;
import lt.fivethreads.mapper.UserMapper;
import lt.fivethreads.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StatisticServiceImplementation implements StatisticService{

    @Autowired
    TripService tripService;
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    OfficeMapper officeMapper;

    public List<TripCount> countTripList(DateRangeDTO dateRangeDTO, String role, String email){
        List<TripDTO> tripList;
        if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ORGANIZER") ){
            tripList=tripService.getAllTrips();
        }
        else{
            tripList=tripService.getAllTripsByUserEmail(email);
        }
        List<Date> allDates = this.getDatesListBetweenDates(dateRangeDTO.getStart(), dateRangeDTO.getFinish());
        List<TripCount> tripCountList = new ArrayList<>();
        for (Date day: allDates
        ) {
            int result = this.countTripInDay(tripList, day);
            TripCount tripCount = new TripCount();
            tripCount.setCount(result);
            tripCount.setDate(day);
            tripCountList.add(tripCount);
        }
        return tripCountList;
    }

    public int countTripInDay(List<TripDTO> trips, Date day){
        int count = 0;
        for (TripDTO trip: trips
        ) {
            if((trip.getStartDate().compareTo(day)<=0 && trip.getFinishDate().compareTo(day)>=0)){
                count++;
            }
        }
        return count;
    }

    public List<Date> getDatesListBetweenDates(Date start, Date finish){
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(start.getTime()));
        while(start.compareTo(finish)<0)
        {
            start = new Date(start.getTime() + 86400000);
            dates.add(new Date(start.getTime()));
        }
        return dates;
    }

    public List<UserTripCountDTO> countTripByUser(IDList IDList){
        List<UserTripCountDTO> userTripCountDTOList = new ArrayList<>();
        for (Long id: IDList.getIdList()
        ) {
            User user = userService.getUserByID(id);
            int count = tripService.getAllTripsByUserEmail(user.getEmail()).size();
            UserTripCountDTO userTripDTO = new UserTripCountDTO();
            ExtendedUserDTO userDTO = userMapper.getUserDTO(user);
            userTripDTO.setCount(count);
            userTripDTO.setUser(userDTO);
            userTripCountDTOList.add(userTripDTO);
        }
        return userTripCountDTOList;
    }
    public List<TripsByPrice> getTripsByPrice(String role, String email){
        List<TripsByPrice> tripsByPrices;
        if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ORGANIZER") ){
            tripsByPrices=this.getAllTripsByPrice();
        }
        else{
            tripsByPrices=this.getAllTripByUserEmailPrice(email);
        }
        return tripsByPrices;
    }

    public List<TripByDuration> getTripByDuration(String role, String email){
        List<TripByDuration> tripByDurations = new ArrayList<>();
        List<TripDTO> trips = new ArrayList<>();
        if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ORGANIZER") ){
            trips = tripService.getAllTrips();
        }
        else{
            trips = tripService.getAllTripsByUserEmail(email);        }
        for (TripDTO tripDTO:trips
        ) {
            long diff = tripDTO.getFinishDate().getTime() - tripDTO.getStartDate().getTime()+ TimeUnit.DAYS.toMillis( 1 );
            long days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            TripByDuration tripByDuration = new TripByDuration();
            tripByDuration.setDuration_days(days);
            tripByDuration.setTripInfo(tripDTO);
            tripByDurations.add(tripByDuration);
        }
        return tripByDurations;
    }



    public List<TripsByPrice> getAllTripsByPrice(){
        List<TripsByPrice> tripsByPrices = new ArrayList<>();
        List<TripDTO> trips = tripService.getAllTrips();
        for (TripDTO tripDTO:trips
        ) {
            double price  = this.calculatePrice(tripDTO);
            TripsByPrice tripsByPrice = new TripsByPrice();
            tripsByPrice.setTotalPrice(price);
            tripsByPrice.setTripInfo(tripDTO);
            tripsByPrices.add(tripsByPrice);
        }
        return tripsByPrices;
    }

    public List<TripsByPrice> getAllTripByUserEmailPrice(String email){
        List<TripsByPrice> tripsByPrices = new ArrayList<>();
        List<TripDTO> trips = tripService.getAllTripsByUserEmail(email);
        for (TripDTO tripDTO:trips
        ) {
            TripMemberDTO tripMember = tripDTO.getTripMembers()
                    .stream().filter(e ->e.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(()-> new WrongTripData("TripMember does not exist"));
            double price  = this.calculateTripMemberPrice(tripMember);
            TripsByPrice tripsByPrice = new TripsByPrice();
            tripsByPrice.setTotalPrice(price);
            tripsByPrice.setTripInfo(tripDTO);
            tripsByPrices.add(tripsByPrice);
        }
        return tripsByPrices;
    }

    public double calculateTripMemberPrice(TripMemberDTO tripMemberDTO){
        int price=0;
        if(tripMemberDTO.getAccommodationDTO()!=null){
            price+=tripMemberDTO.getAccommodationDTO().getPrice();
        }
        if(tripMemberDTO.getCarTicketDTO()!=null){
            price+=tripMemberDTO.getCarTicketDTO().getPrice();
        }
        if(tripMemberDTO.getFlightTicketDTO()!=null){
            price+=tripMemberDTO.getFlightTicketDTO().getPrice();
        }
        return price;
    }

    public double calculatePrice(TripDTO trip){
        int price=0;
        for (TripMemberDTO tripMember:trip.getTripMembers()
        ) {
            price+=this.calculateTripMemberPrice(tripMember);
        }
        return price;
    }

    public List<TripCountByOfficeDTO> getTripCountByOffice(String role, String email){
        List<Office> allOffices = officeRepository.getAll();
        List<TripCountByOfficeDTO> tripByOffices = new ArrayList<>();
        List<TripDTO> trips = new ArrayList<>();
        if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ORGANIZER") ){
            trips = tripService.getAllTrips();
        }
        else{
            trips = tripService.getAllTripsByUserEmail(email);
        }

        for (Office office:allOffices
             ) {
          long count = trips.stream()
                  .filter(e->e.getArrival().getFullAddress()
                          .equals(addressService.getCombinedAddress(office.getAddress())))
                  .count();
          TripCountByOfficeDTO tripCountByOfficeDTO = new TripCountByOfficeDTO();
          tripCountByOfficeDTO.setCount(count);
          tripCountByOfficeDTO.setOffice(officeMapper.getOfficeDTO(office));
          tripByOffices.add(tripCountByOfficeDTO);
        }
        return tripByOffices;
    }
}