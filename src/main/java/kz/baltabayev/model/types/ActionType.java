package kz.baltabayev.model.types;

/**
 * Enumeration representing different actions that can be performed in the system.
 * Each action type corresponds to a specific operation or functionality.
 */
public enum ActionType {
    /**
     * Represents the action of user registration.
     */
    REGISTRATION,

    /**
     * Represents the action of user authorization.
     */
    AUTHORIZATION,

    /**
     * Represents the action of submitting meter readings.
     */
    SUBMIT_METER,

    /**
     * Represents the action of getting the history of meter readings.
     */
    GETTING_HISTORY_OF_METER_READINGS
}
