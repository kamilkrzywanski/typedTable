package org.krzywanski.table.validation;

import org.krzywanski.table.TypedTable;

import javax.swing.*;
import java.util.function.Function;

public class NumberEditor<T> extends GenericEditor<T> {

    public NumberEditor(JTextField textField, TypedTable<T> table, ValidatorDialog<T> validatorDialog, Function<String, Number> transformer) {
        super(textField, table, validatorDialog, transformer);
        ((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
    }
}