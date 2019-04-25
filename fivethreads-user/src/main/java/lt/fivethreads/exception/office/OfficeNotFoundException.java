package lt.fivethreads.exception.office;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,  reason= "Office not found")
public class OfficeNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Office not found";
    }
}
