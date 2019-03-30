package lt.fivethreads.mapper;

import lt.fivethreads.entities.File;
import lt.fivethreads.entities.request.FileDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileMapper {
    public File convertUploadFileToFile(MultipartFile uploadedFile) {
        File file = new File();
        file.setName(uploadedFile.getOriginalFilename());

        return file;
    }

    public FileDTO getFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getName());
        fileDTO.setId(file.getId());

        return fileDTO;
    }
}
