package lt.fivethreads.services;

import lt.fivethreads.entities.request.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TripFilesService {
    FileDTO addFlightTicket(Long tripID, String memberEmail, MultipartFile file);
    FileDTO addCarTicket(Long tripID, String memberEmail, MultipartFile file);
    FileDTO addAccommodationTicket(Long tripID, String memberEmail, MultipartFile file);
    void deleteFlightTicket(Long fileID);
    void deleteCarTicket(Long fileID);
    void deleteAccommodationTicket(Long fileID);
    Boolean checkIfDocumentsExist(Long tripID);

}
