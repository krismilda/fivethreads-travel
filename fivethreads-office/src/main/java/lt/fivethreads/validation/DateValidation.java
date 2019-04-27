package lt.fivethreads.validation;


import lt.fivethreads.exception.WrongDateData;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateValidation {

    public void checkFinishStartDates(Date startDate, Date finishDate, String message) {
        if (finishDate.compareTo(startDate) < 0) {
            throw new WrongDateData(message);
        }
    }
    public void checkStartDateToday(Date startDate) {
        Date today = new Date();
        if (startDate.compareTo(today) < 0) {
            throw new WrongDateData("Start date is earlier than today.");
        }
    }
}
