package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.exception.TripIsNotEditable;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.repositories.FileRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TripFilesServiceImplementation implements TripFilesService
{
    @Autowired
    TripRepository tripRepository;

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TripMemberRepository tripMemberRepository;

    public FileDTO addFlightTicket(Long tripID, String memberEmail, MultipartFile file){
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);
        if(tripMember.getTripAcceptance()!= TripAcceptance.ACCEPTED){
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add flight ticket.");
        }
        if(!tripMember.getIsFlightTickedNeeded()){
            throw new TripIsNotEditable("Flight ticket is not needed.");
        }
        FileDTO fileDTO = fileService.upload(file);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Flight ticket does not exist.")
        );
        if(tripMember.getFlightTicket()==null){
            FlightTicket flightTicket = new FlightTicket();
            flightTicket.setTripMember(tripMember);
            tripMember.setFlightTicket(flightTicket);
        }
        String newID = trip.getId().toString()+tripMember.getId().toString()+"F";
        tripMember.getFlightTicket().setUniqueID(newID);
        tripMember.getFlightTicket().getFile().add(uploadedFile);
        tripMemberRepository.saveFlightTicket(tripMember);
        return fileDTO;
    }



    public FileDTO addCarTicket(Long tripID, String memberEmail, MultipartFile file){
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);

        if(tripMember.getTripAcceptance()!= TripAcceptance.ACCEPTED){
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add car ticket.");
        }
        if(!tripMember.getIsCarNeeded()){
            throw new TripIsNotEditable("Car ticket is not needed.");
        }
        FileDTO fileDTO = fileService.upload(file);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Car ticket does not exist.")
        );
        if(tripMember.getCarTicket()==null){
            CarTicket carTicket = new CarTicket();
            carTicket.setTripMember(tripMember);
            tripMember.setCarTicket(carTicket);
        }
        String newID = trip.getId().toString()+tripMember.getId().toString()+"C";
        tripMember.getCarTicket().setUniqueID(newID);
        tripMember.getCarTicket().getFile().add(uploadedFile);
        tripMemberRepository.saveCarTicket(tripMember);
        return fileDTO;
    }
    public FileDTO addAccommodationTicket(Long tripID, String memberEmail, MultipartFile file){
        Trip trip = tripRepository.findByID(tripID);
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(tripID, memberEmail);

        if(tripMember.getTripAcceptance()!= TripAcceptance.ACCEPTED){
            throw new TripIsNotEditable("The trip is not accepted. Not possible to add accommodation ticket.");
        }
        if(!tripMember.getIsAccommodationNeeded()){
            throw new TripIsNotEditable("Car ticket is not needed.");
        }
        FileDTO fileDTO = fileService.upload(file);
        File uploadedFile = fileRepository.findById(fileDTO.getId()).orElseThrow(
                () -> new WrongTripData("Accommodation ticket does not exist.")
        );
        if(tripMember.getTripAccommodation()==null){
            TripAccommodation tripAccommodation = new TripAccommodation();
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        String newID = trip.getId().toString()+tripMember.getId().toString()+"A";
        tripMember.getTripAccommodation().setUniqueID(newID);
        tripMember.getCarTicket().getFile().add(uploadedFile);
        tripMemberRepository.saveCarTicket(tripMember);
        return fileDTO;
    }
    public void deleteFlightTicket(Long fileID){
        TripMember tripMember = tripMemberRepository.findByFlightFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                () -> new WrongTripData("Flight ticket does not exist.")
        );
        tripMember.getFlightTicket().getFile().remove(file);
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
    }
    public void deleteCarTicket(Long fileID){
        TripMember tripMember = tripMemberRepository.findByCarFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                        () -> new WrongTripData("Car ticket does not exist.")
                );
        tripMember.getCarTicket().getFile().remove(file);
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
    }
    public void deleteAccommodationTicket(Long fileID){
        TripMember tripMember = tripMemberRepository.findByAccommodationFileID(fileID);
        File file = fileRepository.findById(fileID)
                .orElseThrow(
                        () -> new WrongTripData("Accommodation ticket does not exist.")
                );
        tripMember.getTripAccommodation().getFile().remove(file);
        fileRepository.delete(file);
        tripMemberRepository.updateTripMember(tripMember);
    }
    public Boolean checkIfDocumentsExist(Long tripID){
        Trip trip = tripRepository.findByID(tripID);
        for (TripMember tripMember:trip.getTripMembers()
        ) {
            if(tripMember.getTripAccommodation()!=null &&
                    tripMember.getTripAccommodation().getFile()!=null){
                return true;
            }
            if(tripMember.getCarTicket()!=null &&
                    tripMember.getCarTicket().getFile()!=null){
                return true;
            }
            if(tripMember.getFlightTicket()!=null &&
                    tripMember.getFlightTicket().getFile()!=null){
                return true;
            }
        }
        return false;
    }
}
