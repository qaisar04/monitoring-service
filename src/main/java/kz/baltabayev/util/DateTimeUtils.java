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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
     * Formats a {@link LocalDate} object to a string using the default date formatter.
     *
     * @param date the LocalDate object to be formatted
     * @return the formatted date string
     */
    public String parseDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    /**
     * Parses a date string into a {@link LocalDate} object using the default date formatter.
     *
     * @param dateString the date string to be parsed
     * @return the parsed LocalDate object
     */
    public LocalDate parseDateFromString(String dateString) {
        return LocalDate.parse(dateString, dateFormatter);
    }

    /**
     * Checks if two LocalDateTime objects are in the same month.
     *
     * @param date1 the first LocalDateTime object
     * @param date2 the second LocalDateTime object
     * @return true if both dates are in the same month, false otherwise
     */
    public static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() &&
               date1.getMonth() == date2.getMonth();
    }
}
