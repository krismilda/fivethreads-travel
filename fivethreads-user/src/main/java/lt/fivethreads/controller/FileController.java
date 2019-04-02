package lt.fivethreads.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

import lt.fivethreads.entities.request.FileDTO;
import lt.fivethreads.exception.file.FileDownloadFailedException;
import lt.fivethreads.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("files")
public class FileController {

    @Autowired
    FileService fileService;

    /* TODO: set creator and user for who the file belongs?
        Example: Organizer uploads tickers for a user */
    /* TODO: posting file with the same name fails. See what can be done */
    /* TODO: set file travel trip relation */
    @PostMapping("")
    public FileDTO upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(file);
    }

    @GetMapping("/{fileId}")
    public FileDTO getFile(@PathVariable("fileId") long fileId) {
        return fileService.getFileById(fileId);
    }

    @GetMapping("/{fileId}/download")
    public void downloadFile(HttpServletResponse response, @PathVariable("fileId") long fileId) {
        File file = fileService.getFileDownload(fileId);

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());

        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new FileDownloadFailedException();
        }
    }
}