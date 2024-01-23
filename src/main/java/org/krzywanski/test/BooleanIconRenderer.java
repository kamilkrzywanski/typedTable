package org.krzywanski.test;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class BooleanIconRenderer extends DefaultTableCellRenderer {
    static final Icon trueIcon =  new ImageIcon(ClassLoader.getSystemResource("fast-forward-button.png"));
    static final Icon falseIcon =  new ImageIcon(ClassLoader.getSystemResource("fast-backward.png"));
    static final Icon nullIcon = new ImageIcon(ClassLoader.getSystemResource("search.png"));
        public BooleanIconRenderer() {
            super();
            setHorizontalAlignment(CENTER);
        }

        @Override
        protected void setValue(final Object value) {
            if (value instanceof Boolean) {
                final Boolean b = (Boolean) value;
                if (b) {
                    setIcon(trueIcon);
                } else {
                    setIcon(falseIcon);
                }
            } else {
                setIcon(nullIcon);
            }
        }
}