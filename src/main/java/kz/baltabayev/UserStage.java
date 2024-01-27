package kz.baltabayev;

/**
 * Enum representing different stages or states in the user interaction flow within the application.
 * - {@code SECURITY}: Represents the security stage, where users can register or log in.
 * - {@code MAIN_MENU}: Represents the main menu stage, where users can perform various actions related to meter readings.
 * - {@code ADMIN_MENU}: Represents the admin menu stage, providing additional actions for administrators.
 * - {@code EXIT}: Represents the stage indicating the intention to exit the application.
 */
public enum UserStage {
    SECURITY,
    MAIN_MENU,
    ADMIN_MENU,
    EXIT
}
