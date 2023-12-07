package org.krzywanski.table.table;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple combobox with yes/no values
 */
public class BooleanCombobox extends JComboBox<String> {

    public static final String YES_STRING = ResourceBundle.getBundle("Bundle", Locale.getDefault()).getString("yes.string");
    public static final String NO_STRING = ResourceBundle.getBundle("Bundle", Locale.getDefault()).getString("no.string");

    public BooleanCombobox() {
        super(new String[]{null, YES_STRING, NO_STRING});
    }

    String getSelectedValue() {
        Object selectedItem = getSelectedItem();
        if (selectedItem == null) {
            return "";
        }
        if(YES_STRING.equals(selectedItem)) {
            return "true";
        }
        if(NO_STRING.equals(selectedItem)) {
            return "false";
        }
        return "";
    }

}
