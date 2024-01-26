package kz.baltabayev.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

@UtilityClass
public class DateTimeUtils {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String parseDate(LocalDateTime dateTime) {
        return dateTime.format(dateFormatter);
    }

    public String parseTime(LocalDateTime dateTime) {
        return dateTime.format(timeFormatter);
    }

    public String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    public LocalDate parseDateFromString(String dateString) {
        return LocalDate.parse(dateString, dateFormatter);
    }

    public LocalDateTime parseTimeFromString(String timeString) {
        return LocalDateTime.parse(timeString, timeFormatter);
    }

    public LocalDateTime parseDateTimeFromString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public static boolean isSameMonth(LocalDateTime date1, LocalDateTime date2) {
        int year1 = date1.getYear();
        int month1 = date1.getMonthValue();
        int year2 = date2.getYear();
        int month2 = date2.getMonthValue();

        return year1 == year2 && month1 == month2;
    }
}
