package lt.fivethreads.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String reason){
        super(reason);
    }
}