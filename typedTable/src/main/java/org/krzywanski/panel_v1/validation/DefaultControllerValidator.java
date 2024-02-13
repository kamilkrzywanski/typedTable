package org.krzywanski.panel_v1.validation;

import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultControllerValidator<T> implements ControllerValidator<T> {
    private final Function<T, Boolean> validate;
    private final Supplier<String> message;

    public DefaultControllerValidator(Function<T, Boolean> validate, Supplier<String> message) {
        this.validate = validate;
        this.message = message;
    }

    @Override
    public Function<T, Boolean> validate() {
        return validate;
    }

    @Override
    public String getMessage() {
        return message.get();
    }
}
