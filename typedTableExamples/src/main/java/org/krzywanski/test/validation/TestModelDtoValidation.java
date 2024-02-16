package org.krzywanski.test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.krzywanski.test.dto.TestModelDto;

public class TestModelDtoValidation implements ConstraintValidator<TestModelDtoValidator, TestModelDto> {

    @Override
    public boolean isValid(TestModelDto value, ConstraintValidatorContext context) {
        return Boolean.TRUE.equals(value.getBooleanValue());
    }
}
