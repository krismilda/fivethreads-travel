package lt.fivethreads.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TripIsNotEditable extends RuntimeException {
    public TripIsNotEditable(String reason){
        super(reason);
    }
}
