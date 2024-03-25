package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ToManualPanelConverter {
    public <T> void convert(TypedAutoPanel<T> autoPanel, int rows) {
        autoPanel.fieldController.getComponents().forEach(fieldControllerElement -> {

            if (fieldControllerElement.getType().isEnum())
                System.out.printf("JComboBox<%s>", fieldControllerElement.getType().getSimpleName());
            else
                System.out.print(fieldControllerElement.getEditorComponent().getClass().getSimpleName());
            System.out.print(" ");

            System.out.print(fieldControllerElement.getField().getName());

            System.out.print(" = ");

            if (fieldControllerElement.getEditorComponent().getClass() == JCheckBox.class)
                System.out.print("new JCheckBox(\"" + ((JCheckBox) fieldControllerElement.getEditorComponent()).getText() + "\");");
            else if (fieldControllerElement.getEditorComponent().getClass() == JComboBox.class)
                System.out.printf("new JComboBox<>(%s.values());", fieldControllerElement.getType().getSimpleName());
            else
                System.out.printf("new %s();", fieldControllerElement.getEditorComponent().getClass().getSimpleName());
            System.out.println();
        });

        System.out.println();
        System.out.println();

        autoPanel.fieldController.getComponents().forEach(fieldControllerElement -> {
            System.out.println("manualPanel.connectFieldWithPanel(\"" + fieldControllerElement.getField().getName() + "\", " + fieldControllerElement.getField().getName() + ");");
        });
        System.out.println();
        System.out.println();

        autoPanel.fieldController.getComponents().forEach(fieldControllerElement -> {
            if (fieldControllerElement.getSecondComponent() != null)
                System.out.println("JLabel " + fieldControllerElement.getField().getName() + "Label = new JLabel(\"" + ((JLabel) fieldControllerElement.getFirstComponent()).getText() + "\");");
        });
        System.out.println();
        System.out.println();

        System.out.println("JPanel panel = new JPanel(new MigLayout());");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        autoPanel.fieldController.getComponents().forEach(fieldControllerElement -> {
            final boolean isWrap = atomicInteger.getAndIncrement() % rows == 0;

            if (fieldControllerElement.getSecondComponent() != null) {
                System.out.println("panel.add(" + fieldControllerElement.getField().getName() + "Label, \"align right\");");
                System.out.println("panel.add(" + fieldControllerElement.getField().getName() + ", \"pushx, grow " + (isWrap ? ",wrap" : "") + "\");");
            } else {
                System.out.println("panel.add(" + fieldControllerElement.getField().getName() + ", \"span 2" + (isWrap ? ",wrap" : "") + "\");");

            }


        });
    }
}
