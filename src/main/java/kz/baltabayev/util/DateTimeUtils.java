package kz.baltabayev.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

@UtilityClass
public class DateTimeUtils {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    public LocalDateTime parseDateTimeFromString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public static boolean isSameMonth(LocalDateTime date1, LocalDateTime date2) {
        return date1.getYear() == date2.getYear() &&
               date1.getMonth() == date2.getMonth();
    }
}
