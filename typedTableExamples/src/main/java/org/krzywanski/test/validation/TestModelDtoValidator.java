package org.krzywanski.test.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

;

@Target({FIELD, PARAMETER, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {TestModelDtoValidation.class})
public @interface TestModelDtoValidator {
    String message() default "Boolean value must be true.(Bean validation on save)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // We can have additional members to configure the check.
}