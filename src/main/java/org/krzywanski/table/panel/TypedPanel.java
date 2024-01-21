package org.krzywanski.table.panel;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TypedPanel<T> extends JPanel {
    
    private final T data;
    
    public TypedPanel(T data) {
        this.data = data;
        setLayout(new MigLayout());
        initComponents();
    }

    private void initComponents() {
        PanelFieldCreator panelFieldCreator = new PanelFieldCreator(data.getClass());
        panelFieldCreator.getComponents().forEach(component -> add(component, "grow, wrap"));
    }


}
