package lt.fivethreads.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User ID not exists.")
public class UserIDNotExists extends RuntimeException {
}