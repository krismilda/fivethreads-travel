package lt.fivethreads.importing;

import org.springframework.web.multipart.MultipartFile;

public interface UserImportService {
    void importEntities(MultipartFile multipartFile);
}
