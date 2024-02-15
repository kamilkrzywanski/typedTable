package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.krzywanski.panel_v1.FieldControllerElement;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldValidator<T> {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Set<String> validateField(Class<T> dataClass, FieldControllerElement element) {
        return validator.validateValue(dataClass, element.getField().getName(), element.getFieldValueController().getValue())
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

}
