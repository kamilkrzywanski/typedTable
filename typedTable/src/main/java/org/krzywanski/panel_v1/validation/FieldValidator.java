package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.panel_v1.FieldControllerElement;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldValidator<T> {

    final Validator validator = Validation.byDefaultProvider().configure().messageInterpolator(
            new ResourceBundleMessageInterpolator(
                    new AggregateResourceBundleLocator(TypedFrameworkConfiguration.resourceBundles))
    ).buildValidatorFactory().getValidator();
    public Set<String> validateField(Class<T> dataClass, FieldControllerElement element) {
        try {
            return validator.validateValue(dataClass, element.getField().getName(), element.getFieldValueController().getValueForValidation())
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            return Set.of(e.getMessage());
        }
    }

}
