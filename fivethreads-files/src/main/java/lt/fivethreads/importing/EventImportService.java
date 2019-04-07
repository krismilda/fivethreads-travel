package lt.fivethreads.importing;

import org.springframework.web.multipart.MultipartFile;

public interface EventImportService {
    void importEvents(MultipartFile multipartFile);
}
