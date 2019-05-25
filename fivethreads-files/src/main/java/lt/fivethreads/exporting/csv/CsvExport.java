package lt.fivethreads.exporting.csv;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.List;

public abstract class CsvExport {

    private CsvMapper mapper;
    private CsvSchema schema;
    private ObjectWriter myObjectWriter;
    private File tempFile;
    private FileOutputStream tempFileOutputStream;
    private BufferedOutputStream bufferedOutputStream;
    private OutputStreamWriter writerOutputStream;
    private String fileName;


    public <T> void prepareMapper(Class<T> object) throws ClassNotFoundException {
        mapper = new CsvMapper();
        Class className = Class.forName(object.getCanonicalName());
        schema = mapper.schemaFor(className).withColumnSeparator(',').withHeader();
        myObjectWriter = mapper.writer(schema);

    }

    public <T> File write(String email, List<T> objectList, String entityFolderName)throws IOException{
        String filePath = "export-dir/"+entityFolderName;
        tempFile = new File(filePath);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }

        String name = objectList.get(0).getClass().getSimpleName();
        fileName = name+"-"+email+".csv";
        filePath = filePath+"/"+fileName;
        tempFile = new File(filePath);

        tempFileOutputStream = new FileOutputStream(tempFile);
        bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");


        myObjectWriter.writeValue(writerOutputStream, objectList);

        return tempFile;
    }
}
