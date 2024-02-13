package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.krzywanski.panel_v1.FieldControllerElement;

import java.util.Set;

public class FieldValidator<T> {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Set<ConstraintViolation<T>> validateField(Class<T> dataClass, FieldControllerElement element) {
        return validator.validateValue(dataClass, element.getField().getName(), element.getFieldValueController().getValue());
    }

    public Set<ConstraintViolation<T>> validateValue(Class<T> dataClass, String propertyName, Object value) {
        return validator.validateValue(dataClass, propertyName, value);
    }


}
