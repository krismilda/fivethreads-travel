package lt.fivethreads.controller;

import lt.fivethreads.exception.file.FileDownloadFailedException;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

public abstract class AbstractFileController {

    protected void  setFileDownloadResponse(File file, HttpServletResponse response){
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
