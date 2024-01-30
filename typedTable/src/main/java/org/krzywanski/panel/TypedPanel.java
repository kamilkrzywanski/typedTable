package org.krzywanski.panel;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TypedPanel<T> extends JPanel {
    
    private final T data;
    
    public TypedPanel(T data) {
        this.data = data;
        setLayout(new MigLayout("debug"));
        initComponents();
    }

    private void initComponents() {
        PanelFieldCreator panelFieldCreator = new PanelFieldCreator(data.getClass());
        panelFieldCreator.getComponents().forEach((element) -> {
            add(element.getFirstComponent(), element.getSecondComponent() != null ? "grow" : "grow, span 2, wrap");
            if(element.getSecondComponent() != null)
                add(element.getSecondComponent(), "grow, wrap");
        });
    }


}
