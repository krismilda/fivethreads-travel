package lt.fivethreads.services;

import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TripFilesService {
    FileDTO addFlightTicket(Long tripID, String memberEmail, MultipartFile file, double price);
    FileDTO addCarTicket(Long tripID, String memberEmail, MultipartFile file,double price);
    FileDTO addAccommodationTicket(Long tripID, String memberEmail, MultipartFile file,double price);
    TripMemberDTO deleteFlightTicket(Long fileID);
    TripMemberDTO deleteCarTicket(Long fileID);
    TripMemberDTO deleteAccommodationTicket(Long fileID);
    Boolean checkIfDocumentsExist(Long tripID);

}
