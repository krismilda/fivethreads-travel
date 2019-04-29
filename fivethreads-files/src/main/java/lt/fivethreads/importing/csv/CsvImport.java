package lt.fivethreads.importing.csv;

import lt.fivethreads.exception.importing.ImportFailedException;
import lt.fivethreads.importing.file.FileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

abstract public class CsvImport {
    @Autowired
    CsvFileProcessor csvFileProcessor;

    @Autowired
    FileConverter fileConverter;

    public void importEntities(MultipartFile multipartFile) {
        File file = null;
        try {
            file = fileConverter.convert(multipartFile);
        } catch (IOException e) {
            throw new ImportFailedException();
        }

        saveEntities(file);
        file.delete();
    }

    abstract protected void saveEntities(File file);
}
