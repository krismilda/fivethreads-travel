package lt.fivethreads.services;

import lt.fivethreads.entities.Occupancy;
import lt.fivethreads.entities.request.OccupancyDTO;
import lt.fivethreads.entities.request.OccupancyForm;
import lt.fivethreads.exception.WrongOccupancyData;
import lt.fivethreads.mapper.OccupancyMapper;
import lt.fivethreads.repositories.OccupancyRepository;
import lt.fivethreads.validation.OccupancyValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OccupancyServiceImplementation implements OccupancyService {

    @Autowired
    OccupancyMapper occupancyMapper;

    @Autowired
    OccupancyRepository occupancyRepository;

    @Autowired
    OccupancyValidation occupancyValidation;


    public OccupancyDTO createOccupancy(OccupancyForm occupancyForm) throws WrongOccupancyData {

        Occupancy occupancy = occupancyMapper.convertCreateOccupancyFormToOccupancy(occupancyForm);
        return occupancyMapper.getOccupancyDTO(occupancyRepository.createOccupancy(occupancy));

    }


    public List<OccupancyDTO> getAllOccupancies() {
        List<Occupancy> occupancyList = occupancyRepository.getAll();
        List<OccupancyDTO> occupancyDTOList = new ArrayList<>();
        for (Occupancy occupancy : occupancyList){
            occupancyDTOList.add(occupancyMapper.getOccupancyDTO(occupancy));
        }
        return occupancyDTOList;
    }


    public List<OccupancyDTO> getAllOccupanciesByUser(long id) {
        List<Occupancy> occupancyList = occupancyRepository.getOccupancyByUser(id);
        List<OccupancyDTO> occupancyDTOList = new ArrayList<>();
        for (Occupancy occupancy : occupancyList){
            occupancyDTOList.add(occupancyMapper.getOccupancyDTO(occupancy));
        }
        return occupancyDTOList;
    }


    public List<OccupancyDTO> getAllOccupanciesByTrip(long id) {
        List<Occupancy> occupancyList = occupancyRepository.getOccupancyByTrip(id);
        List<OccupancyDTO> occupancyDTOList = new ArrayList<>();
        for (Occupancy occupancy : occupancyList){
            occupancyDTOList.add(occupancyMapper.getOccupancyDTO(occupancy));
        }
        return occupancyDTOList;
    }


    public List<OccupancyDTO> getAllOccupanciesByApartment(long id) {
        List<Occupancy> occupancyList = occupancyRepository.getOccupancyByApartment(id);
        List<OccupancyDTO> occupancyDTOList = new ArrayList<>();
        for (Occupancy occupancy : occupancyList){
            occupancyDTOList.add(occupancyMapper.getOccupancyDTO(occupancy));
        }
        return occupancyDTOList;
    }
}
