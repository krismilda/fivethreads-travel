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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;

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
        FileDTO fileDTO = fileService.upload(file);
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
            tripMember.getFlightTicket().setFile(new ArrayList<>());
        }
        tripMember.getFlightTicket().getFile().add(uploadedFile);
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
        FileDTO fileDTO = fileService.upload(file);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Car ticket does not exist.")
        );
        if (tripMember.getCarTicket() == null) {
            CarTicket carTicket = new CarTicket();
            carTicket.setTripMember(tripMember);
            tripMember.setCarTicket(carTicket);
        }

        tripMember.getCarTicket().setPrice(price);
        tripMemberRepository.saveCarTicket(tripMember);
        String newID = trip.getId().toString() + tripMember.getId().toString() + "C" + tripMember.getCarTicket().getId().toString();
        tripMember.getCarTicket().setUniqueID(newID);
        if(tripMember.getCarTicket().getFile()==null){
            tripMember.getCarTicket().setFile(new ArrayList<>());
        }
        tripMember.getCarTicket().getFile().add(uploadedFile);
        tripMemberRepository.saveCarTicket(tripMember);
        createNotificationService.createNotificationInformationChanged(tripMember, "Information was changed.");
        return fileDTO;
    }

    @Transactional
    public FileDTO addAccommodationTicket(Long tripID, String memberEmail, MultipartFile file,double price) {
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);

        if (tripMember.getTripAcceptance() != TripAcceptance.ACCEPTED) {
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add accommodation ticket.");
        }
        if (!tripMember.getIsAccommodationNeeded()) {
            throw new TripIsNotEditable("Car ticket is not needed.");
        }
        FileDTO fileDTO = fileService.upload(file);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Accommodation ticket does not exist.")
        );
        if (tripMember.getTripAccommodation() == null) {
            TripAccommodation tripAccommodation = new TripAccommodation();
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        tripMember.getTripAccommodation().setPrice(price);
        tripMemberRepository.saveAccommodationTicket(tripMember);
        String newID = trip.getId().toString() + tripMember.getId().toString() + "A" + tripMember.getTripAccommodation().getId().toString();
        tripMember.getTripAccommodation().setUniqueID(newID);
        if(tripMember.getTripAccommodation().getFile()==null){
            tripMember.getTripAccommodation().setFile(new ArrayList<>());
        }
        tripMember.getTripAccommodation().getFile().add(uploadedFile);
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
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    public Boolean checkIfDocumentsExist(Long tripID) {
        Trip trip = tripRepository.findByID(tripID);
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            if (tripMember.getTripAccommodation() != null &&
                    tripMember.getTripAccommodation().getFile() != null) {
                return true;
            }
            if (tripMember.getCarTicket() != null &&
                    tripMember.getCarTicket().getFile() != null) {
                return true;
            }
            if (tripMember.getFlightTicket() != null &&
                    tripMember.getFlightTicket().getFile() != null) {
                return true;
            }
        }
        return false;
    }
}
