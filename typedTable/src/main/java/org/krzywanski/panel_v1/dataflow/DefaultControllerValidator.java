package org.krzywanski.panel_v1.dataflow;

import java.util.function.Supplier;

public class DefaultControllerValidator implements ControllerValidator {
    private final Supplier<Boolean> validate;
    private final Supplier<String> message;

    public DefaultControllerValidator(Supplier<Boolean> validate, Supplier<String> message) {
        this.validate = validate;
        this.message = message;
    }

    @Override
    public Supplier<Boolean> validate() {
        return validate;
    }

    @Override
    public String getMessage() {
        return message.get();
    }
}
