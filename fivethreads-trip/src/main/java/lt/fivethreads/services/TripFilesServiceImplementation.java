package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.exception.TripIsNotEditable;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMemberMapper;
import lt.fivethreads.repositories.FileRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class TripFilesServiceImplementation implements TripFilesService {
    @Autowired
    TripRepository tripRepository;

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    CreateNotificationService createNotificationService;

    @Autowired
    TripMemberMapper tripMemberMapper;

    @Transactional
    public FileDTO addFlightTicket(Long tripID, String memberEmail, MultipartFile file, double price) {
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);

        if (tripMember.getTripAcceptance() != TripAcceptance.ACCEPTED) {
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add flight ticket.");
        }
        if (!tripMember.getIsFlightTickedNeeded()) {
            throw new TripIsNotEditable("Flight ticket is not needed.");
        }
        int size=0;
        if(tripMember.getFlightTicket()!=null && tripMember.getFlightTicket().getFile()!=null){
            size = tripMember.getFlightTicket().getFile().size();
        }
        String new_filename = FilenameUtils.getBaseName(file.getOriginalFilename())
                .concat("_"+tripMember.getId()+size+"F") + "."
                + FilenameUtils.getExtension(file.getOriginalFilename());
        FileDTO fileDTO = fileService.upload(file,new_filename);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Flight ticket does not exist.")
        );
        if (tripMember.getFlightTicket() == null) {
            FlightTicket flightTicket = new FlightTicket();
            flightTicket.setTripMember(tripMember);
            tripMember.setFlightTicket(flightTicket);
        }
        tripMember.getFlightTicket().setPrice(price);
        tripMemberRepository.saveFlightTicket(tripMember);
        String newID = trip.getId().toString() + tripMember.getId().toString() + "F" + tripMember.getFlightTicket().getId().toString();
        tripMember.getFlightTicket().setUniqueID(newID);
        if(tripMember.getFlightTicket().getFile()==null){
            List<File> files = new ArrayList<>();
            files.add(uploadedFile);
            tripMember.getFlightTicket().setFile(files);
        }
        else {
            tripMember.getFlightTicket().getFile().add(uploadedFile);
        }
        tripMemberRepository.saveFlightTicket(tripMember);
        createNotificationService.createNotificationInformationChanged(tripMember, "Information was changed.");
        return fileDTO;
    }


    @Transactional
    public FileDTO addCarTicket(Long tripID, String memberEmail, MultipartFile file,double price) {
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);
       if (tripMember.getTripAcceptance() != TripAcceptance.ACCEPTED) {
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add car ticket.");
        }
        if (!tripMember.getIsCarNeeded()) {
            throw new TripIsNotEditable("Car ticket is not needed.");
        }
       if(tripMember.getCarTicket().getFile()==null){
           CarTicket carTicket = new CarTicket();
           carTicket.setTripMember(tripMember);
           carTicket.setCarRentStart(tripMember.getCarTicket().getCarRentStart());
           carTicket.setCarRentFinish(tripMember.getCarTicket().getCarRentFinish());
           CarTicket toDelete = tripMember.getCarTicket();
           tripMember.setCarTicket(carTicket);
           toDelete.setTripMember(null);
           tripMemberRepository.removeCarTicket(toDelete);
           tripMemberRepository.saveCarTicket(tripMember);
       }

        int size=0;
        if(tripMember.getCarTicket()!=null && tripMember.getCarTicket().getFile()!=null){
            size = tripMember.getCarTicket().getFile().size();
        }
        String new_filename = FilenameUtils.getBaseName(file.getOriginalFilename())
                .concat("_"+tripMember.getId()+size+"C") + "."
                + FilenameUtils.getExtension(file.getOriginalFilename());
        FileDTO fileDTO = fileService.upload(file, new_filename);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Car ticket does not exist.")
        );
        tripMember.getCarTicket().setPrice(price);
        tripMemberRepository.saveCarTicket(tripMember);
        String newID = trip.getId().toString() + tripMember.getId().toString() + "C" + tripMember.getCarTicket().getId().toString();
        tripMember.getCarTicket().setUniqueID(newID);
        tripMember.getCarTicket().getFile().add(uploadedFile);
        tripMemberRepository.saveCarTicket(tripMember);
        createNotificationService.createNotificationInformationChanged(tripMember, "Information was changed.");
        return fileDTO;
    }

    @Transactional
    public FileDTO addAccommodationTicket(Long tripID, String memberEmail, MultipartFile file,double price) {
        Trip trip = tripRepository.findByID(tripID);
        if(trip==null){
            throw new WrongTripData("Trip does not exist.");
        }
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);

        if (tripMember.getTripAcceptance() != TripAcceptance.ACCEPTED) {
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add accommodation ticket.");
        }
        if (!tripMember.getIsAccommodationNeeded()) {
            throw new TripIsNotEditable("TripAccommodation ticket is not needed.");
        }
        if (tripMember.getTripAccommodation() == null) {
            TripAccommodation tripAccommodation = new TripAccommodation();
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        if(tripMember.getTripAccommodation().getFile()==null){
            TripAccommodation tripAccommodation = new TripAccommodation();
            tripAccommodation.setTripMember(tripMember);
            tripAccommodation.setAccommodationStart(tripMember.getTripAccommodation().getAccommodationStart());
            tripAccommodation.setAccommodationFinish(tripMember.getTripAccommodation().getAccommodationFinish());
            tripAccommodation.setAccommodationType(tripMember.getTripAccommodation().getAccommodationType());
            TripAccommodation toDelete = tripMember.getTripAccommodation();
            tripMember.setTripAccommodation(tripAccommodation);
            toDelete.setTripMember(null);
            tripMemberRepository.removeTripAccomodation(toDelete);
            tripMemberRepository.saveAccommodationTicket(tripMember);
        }
        int size=0;
        if(tripMember.getTripAccommodation()!=null && tripMember.getTripAccommodation().getFile()!=null){
            size = tripMember.getTripAccommodation().getFile().size();
        }
        String new_filename = FilenameUtils.getBaseName(file.getOriginalFilename())
                .concat("_"+tripMember.getId()+size+"A") + "."
                + FilenameUtils.getExtension(file.getOriginalFilename());
        FileDTO fileDTO = fileService.upload(file, new_filename);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Accommodation ticket does not exist.")
        );
        tripMember.getTripAccommodation().setPrice(price);
        tripMemberRepository.saveAccommodationTicket(tripMember);
        String newID = trip.getId().toString() + tripMember.getId().toString() + "A" + tripMember.getTripAccommodation().getId().toString();
        tripMember.getTripAccommodation().setUniqueID(newID);
        if(tripMember.getTripAccommodation().getFile()==null){
            List<File> files = new ArrayList<>();
            files.add(uploadedFile);
            tripMember.getTripAccommodation().setFile(files);
        }
        else{
            tripMember.getTripAccommodation().getFile().add(uploadedFile);
        }
        tripMember.getTripAccommodation().setPrice(price);
        tripMemberRepository.saveAccommodationTicket(tripMember);
        createNotificationService.createNotificationInformationChanged(tripMember, "Information was changed.");
        return fileDTO;
    }

    @Transactional
    public TripMemberDTO deleteFlightTicket(Long fileID) {
        TripMember tripMember = tripMemberRepository.findByFlightFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                        () -> new WrongTripData("Flight ticket does not exist.")
                );
        tripMember.getFlightTicket().getFile().remove(file);
        fileService.deleteFile(file.getName());
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    @Transactional
    public TripMemberDTO deleteCarTicket(Long fileID) {
        TripMember tripMember = tripMemberRepository.findByCarFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                        () -> new WrongTripData("Car ticket does not exist.")
                );
        tripMember.getCarTicket().getFile().remove(file);
        fileService.deleteFile(file.getName());
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    @Transactional
    public TripMemberDTO deleteAccommodationTicket(Long fileID) {
        TripMember tripMember = tripMemberRepository.findByAccommodationFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                        () -> new WrongTripData("Accommodation ticket does not exist.")
                );
        tripMember.getTripAccommodation().getFile().remove(file);
        fileService.deleteFile(file.getName());
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    public Boolean checkIfDocumentsExist(Long tripID) {
        Trip trip = tripRepository.findByID(tripID);
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            if (tripMember.getTripAccommodation() != null &&
                    tripMember.getTripAccommodation().getFile() != null &&
                    tripMember.getTripAccommodation().getFile().size()!=0
            ) {
                return true;
            }
            if (tripMember.getCarTicket() != null &&
                    tripMember.getCarTicket().getFile() != null &&
                    tripMember.getCarTicket().getFile().size()!=0
            ) {
                return true;
            }
            if (tripMember.getFlightTicket() != null &&
                    tripMember.getFlightTicket().getFile() != null &&
                    tripMember.getFlightTicket().getFile().size()!=0) {
                return true;
            }
        }
        return false;
    }
}
