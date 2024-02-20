package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.panel_v1.AbstractTypedPanel;
import org.krzywanski.panel_v1.autopanel.buttons.AutoPanelButtons;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Basic implementation of panel for data with auto generated fields
 * @param <T> - type of data
 */
public class TypedAutoPanel<T> extends AbstractTypedPanel<T> {

    static {
        TypedFrameworkConfiguration.addFieldResolver(new PanelFieldAnnotationResolver()::resolveField);
        TypedFrameworkConfiguration.addFieldResolver(new MyTableColumnAnnotationResolver()::resolveField);
    }

    final JPanel fieldsPanel = new JPanel(new MigLayout());

    /**
     * @param dataSupplier - supplier of data for panel
     * @param dataClass    - class of data
     */
    public TypedAutoPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        super(dataSupplier, dataClass);
        super.autoPanelButtons = new AutoPanelButtons<>(this, () -> insertRepository, () -> removeRepository, () -> updateRepository);
        this.fieldController = new AutoPanelFieldCreator<>(dataClass, this);
        setLayout(new MigLayout("fill"));
    }

    public TypedAutoPanel<T> buildPanel(){
        return buildPanel(1);
    }

    /**
     * Builds panel with fields
     *
     * @param rows - number of rows in panel
     * @return - this
     */
    public TypedAutoPanel<T> buildPanel(int rows) {
        addFields(rows);
        add(fieldsPanel, "grow, wrap");
        add(autoPanelButtons, "right");
        fillWithData();
        return this;
    }

    /**
     * Adds fields to panel
     */
    private void addFields(int rows) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        fieldController.getComponents().forEach((element) -> {
            final boolean isWrap = atomicInteger.getAndIncrement() % rows == 0;

            fieldsPanel.add(element.getFirstComponent(), element.getSecondComponent() != null ? "" : "span 2" + (isWrap ? ",wrap" : ""));
            if(element.getSecondComponent() != null)
                fieldsPanel.add(element.getSecondComponent(), "pushx, grow" + (isWrap ? ",wrap" : ""));
        });
    }
}
