package lt.fivethreads.exception.importing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason = "invalid data provided")
public class EventImportFailedException extends RuntimeException{
}
