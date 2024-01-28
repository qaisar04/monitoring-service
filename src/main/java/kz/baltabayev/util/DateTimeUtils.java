package kz.baltabayev.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * Utility class for handling date and time operations.
 */
@UtilityClass
public class DateTimeUtils {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Formats a {@link LocalDateTime} object to a string using the default date-time formatter.
     *
     * @param dateTime the LocalDateTime object to be formatted
     * @return the formatted date-time string
     */
    public String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    /**
     * Parses a date-time string into a {@link LocalDateTime} object using the default date-time formatter.
     *
     * @param dateTimeString the date-time string to be parsed
     * @return the parsed LocalDateTime object
     */
    public LocalDateTime parseDateTimeFromString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    /**
     * Checks if two LocalDateTime objects are in the same month.
     *
     * @param date1 the first LocalDateTime object
     * @param date2 the second LocalDateTime object
     * @return true if both dates are in the same month, false otherwise
     */
    public static boolean isSameMonth(LocalDateTime date1, LocalDateTime date2) {
        return date1.getYear() == date2.getYear() &&
               date1.getMonth() == date2.getMonth();
    }
}
