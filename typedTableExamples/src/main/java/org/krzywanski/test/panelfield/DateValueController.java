package org.krzywanski.test.panelfield;

import org.jdesktop.swingx.JXDatePicker;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.fields.FieldValueSupplier;

import java.util.Date;
import java.util.function.Supplier;

public class DateValueController implements FieldValueController<Date, JXDatePicker> {

    JXDatePicker datePicker;

    public DateValueController(JXDatePicker datePicker) {
        this.datePicker = datePicker;
    }

    @Override
    public Date getValue() {
        return datePicker.getDate();
    }

    @Override
    public void setValue(FieldValueSupplier<Date> value) {
        datePicker.setDate(value.getValue());
    }

    @Override
    public void setEditable(boolean enabled) {
    getComponent().setEditable(enabled);
    }

    @Override
    public JXDatePicker getComponent() {
        return datePicker;
    }
}
