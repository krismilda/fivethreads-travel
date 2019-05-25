package lt.fivethreads.controller;


import lt.fivethreads.exception.file.FileDownloadFailedException;
import lt.fivethreads.exporting.ApartmentExportService;
import lt.fivethreads.exporting.OfficeExportService;
import lt.fivethreads.exporting.UserExportService;
import lt.fivethreads.exporting.csv.CsvApartmentExport;
import lt.fivethreads.exporting.csv.CsvUserExport;
import lt.fivethreads.importing.csv.object.CsvOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.httpserver.HttpsServerImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExportController extends AbstractFileController {

    @Autowired
    UserExportService userExportService;

    @Autowired
    ApartmentExportService apartmentExportService;

    @Autowired
    OfficeExportService officeExportService;

    @GetMapping("/export/users")
    public ResponseEntity<?>exportUsers(HttpServletResponse response){

        File file = userExportService.exportEntities(SecurityContextHolder.getContext().getAuthentication().getName());

        setFileDownloadResponse(file, response);

        return new ResponseEntity("File successfully exported", HttpStatus.OK);
    }
    @GetMapping("/export/apartments")
    public ResponseEntity<?>exportApartments(HttpServletResponse response){

            File file = apartmentExportService.exportEntities(SecurityContextHolder.getContext().getAuthentication().getName());
            setFileDownloadResponse(file, response);

        return new ResponseEntity("It works!", HttpStatus.OK);
    }
    @GetMapping("/export/offices")
    public ResponseEntity<?>exportOffices(HttpServletResponse response){
        File file = officeExportService.exportEntities(SecurityContextHolder.getContext().getAuthentication().getName());
        setFileDownloadResponse(file, response);

        return new ResponseEntity("It works!", HttpStatus.OK);
    }

}