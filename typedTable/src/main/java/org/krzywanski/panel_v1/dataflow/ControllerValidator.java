package org.krzywanski.panel_v1.dataflow;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This interface is used to validate do buttons should be enabled or not
 */
public interface ControllerValidator<T> {
    Function<T, Boolean> validate();

    String getMessage();
}
