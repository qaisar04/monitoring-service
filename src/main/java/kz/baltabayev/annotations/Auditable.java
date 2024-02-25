package kz.baltabayev.annotations;

import kz.baltabayev.model.types.ActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods as auditable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auditable {
    /**
     * The login of the user performing the audited action.
     * @return The login string.
     */
    String login() default "";

    /**
     * The user ID of the user performing the audited action.
     * @return The user ID string.
     */
    String userId() default "";

    /**
     * The type of action being audited.
     * @return The action type.
     */
    ActionType actionType();
}
