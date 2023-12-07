package org.krzywanski.table.table;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class BooleanCombobox extends JComboBox<String> {

    public static final String YES_STRING = ResourceBundle.getBundle("Bundle", Locale.getDefault()).getString("yes.string");
    public static final String NO_STRING = ResourceBundle.getBundle("Bundle", Locale.getDefault()).getString("no.string");

    public BooleanCombobox() {
        super(new String[]{null, "Tak", "Nie"});
    }

    String getSelectedValue() {
        Object selectedItem = getSelectedItem();
        if (selectedItem == null) {
            return "";
        }
        switch (selectedItem.toString()) {
            case "Tak":
                return "true";
            case "Nie":
                return "false";
            default:
                return "";
        }
    }

}
