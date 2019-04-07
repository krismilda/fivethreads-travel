package lt.fivethreads.importing;

import org.springframework.web.multipart.MultipartFile;

public interface OfficeImportService {
    void importEntities(MultipartFile multipartFile);
}
