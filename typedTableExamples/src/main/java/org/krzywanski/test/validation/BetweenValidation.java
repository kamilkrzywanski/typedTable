package org.krzywanski.test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class BetweenValidation implements ConstraintValidator<BetweenValidator, Number> {
    String min;
    String max;

    @Override
    public void initialize(BetweenValidator constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return new BigDecimal(min).compareTo(new BigDecimal(value.toString())) <= 0
                && new BigDecimal(max).compareTo(new BigDecimal(value.toString())) >= 0;
    }
}
