package org.krzywanski.panel_v1.autopanel.buttons;

import java.util.function.Function;

/**
 * This interface is used to validate do buttons should be enabled or not
 */
public interface ControllerValidator<T> {
    Function<T, Boolean> validate();

    String getMessage();
}
