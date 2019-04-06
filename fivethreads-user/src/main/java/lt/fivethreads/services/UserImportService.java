package lt.fivethreads.services;

import org.springframework.web.multipart.MultipartFile;

public interface UserImportService {
    void importUsers(MultipartFile multipartFile);
}
