package lt.fivethreads.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class OfficeDataWasModified extends RuntimeException {
    public OfficeDataWasModified(String reason){
        super(reason);
    }
}