package kz.baltabayev.annotations;

import kz.baltabayev.model.types.ActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Audit {
    String login() default "";
    String userId() default "";
    ActionType actionType();
}
