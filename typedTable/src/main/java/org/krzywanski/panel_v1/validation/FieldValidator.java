package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.panel_v1.fields.FieldControllerElement;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldValidator<T> {

    final Validator validator = Validation.byDefaultProvider().configure().messageInterpolator(
            new ResourceBundleMessageInterpolator(
                    new AggregateResourceBundleLocator(TypedFrameworkConfiguration.resourceBundles))
    ).buildValidatorFactory().getValidator();
    public Set<String> validateField(Class<T> dataClass, FieldControllerElement element) {
        return validateField(dataClass, element.getField().getName(), element.getFieldValueController().getValueForValidation());
    }

    public Set<String> validateField(Class<T> beanClass, String propertyName, Object value) {
        try {
            return validator.validateValue(beanClass, propertyName, value).stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            return Set.of(e.getMessage());
        }
    }


    public Set<String> validateBean(T bean) {
        return validator.validate(bean).stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
    }

}
