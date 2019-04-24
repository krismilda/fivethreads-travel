package lt.fivethreads.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongNotificationTypeOrID extends RuntimeException {
    public WrongNotificationTypeOrID(String reason){
        super(reason);
    }
}
