package lt.fivethreads.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "")
public class FileDownloadFailedException extends RuntimeException {
}