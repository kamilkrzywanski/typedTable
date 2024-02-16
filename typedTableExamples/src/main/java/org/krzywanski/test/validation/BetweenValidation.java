package org.krzywanski.test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class BetweenValidation implements ConstraintValidator<BetweenValidator, BigDecimal> {
    String min;
    String max;

    @Override
    public void initialize(BetweenValidator constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return new BigDecimal(min).compareTo(value) <= 0
                && new BigDecimal(max).compareTo(value) >= 0;
    }
}
