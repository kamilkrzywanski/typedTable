package org.krzywanski.test.panelfield;

import org.jdesktop.swingx.JXDatePicker;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.fields.FieldValueSupplier;

import javax.swing.border.Border;
import java.util.Date;

public class DateValueController implements FieldValueController<Date, JXDatePicker> {

    JXDatePicker datePicker;
    private Border originalBorder;

    public DateValueController(JXDatePicker datePicker) {
        this.datePicker = datePicker;
        this.originalBorder = datePicker.getBorder();
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

    @Override
    public void setBorder(Border border) {
        datePicker.setBorder(border);
    }

    @Override
    public void resetBorder() {
        getComponent().setBorder(originalBorder);
    }
}
