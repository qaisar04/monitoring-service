package kz.baltabayev.dto;

/**
 * A record representing a request to submit a meter reading.
 * This record encapsulates the counter number and meter type ID associated with the meter reading.
 */
public record MeterReadingRequest(Integer counterNumber,
                                  Long meterTypeId) {
}
