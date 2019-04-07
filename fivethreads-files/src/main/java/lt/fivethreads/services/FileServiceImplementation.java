package lt.fivethreads.services;

import lt.fivethreads.entities.File;
import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.exception.file.FileDownloadFailedException;
import lt.fivethreads.exception.file.FileNotFoundException;
import lt.fivethreads.exception.file.FileUploadFailedException;
import lt.fivethreads.mapper.FileMapper;
import lt.fivethreads.repositories.FileRepository;
import lt.fivethreads.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileServiceImplementation implements FileService {

    @Autowired
    StorageService storageService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileMapper fileMapper;

    public FileDTO upload(MultipartFile uploadedFile) {
        File file = fileMapper.convertUploadFileToFileEntity(uploadedFile);
        fileRepository.save(file);

        try {
            storageService.store(uploadedFile);
        } catch (Exception e) {
            fileRepository.delete(file);

            throw new FileUploadFailedException();
        }

        return fileMapper.getFileDTO(file);
    }

    public FileDTO getFileById(long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);

        return fileMapper.getFileDTO(file);
    }

    @Override
    public java.io.File getFileDownload(long fileId) {
        FileDTO fileDTO = this.getFileById(fileId);

        try {
            Resource resource = storageService.loadFile(fileDTO.getFileName());
            return resource.getFile();
        } catch (Exception e) {
            throw new FileDownloadFailedException();
        }
    }
}
