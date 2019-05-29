package lt.fivethreads.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User cannot be deleted.")
public class UserCannotBeDeleted extends RuntimeException {
}
