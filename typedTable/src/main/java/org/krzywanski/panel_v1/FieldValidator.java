package org.krzywanski.panel_v1;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class FieldValidator<T> {

    public Set<ConstraintViolation<T>> validateField(FieldControllerElement element, Validator validator, Class<T> dataClass) {
        Set<ConstraintViolation<T>> result = validator.validateValue(dataClass, element.getField().getName(), element.getFieldValueController().getValue());
        System.out.println("Validation result for field: " + element.getField().getName());
        result.forEach(v -> System.out.println(v.getMessage()));
        return result;
    }
}
