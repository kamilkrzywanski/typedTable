package org.krzywanski.table.components;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.BundleTranslator;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.table.TypedTable;
import org.krzywanski.table.TypedTablePanel;
import org.krzywanski.table.annot.TableFilter;
import org.krzywanski.table.annot.TableFilters;
import org.krzywanski.table.providers.IFilterComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class FilterDialog extends JDialog {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("TableBundle", Locale.getDefault());
    BundleTranslator bundleTranslator = new BundleTranslator(Locale.getDefault(), TypedFrameworkConfiguration.resourceBundles);
    public static final Map<Class<?>, IFilterComponent> customFilterComponents = new HashMap<>();
    final ActionListener firstPageAction;
    final Class<?> typeClass;
    final TypedTablePanel<?> parentPanel;
    final TypedTable<?> table;

    JPanel filterPanel = new JPanel(new MigLayout());

    public FilterDialog(ActionListener firstPageAction, TypedTable<?> typedTable, TypedTablePanel<?> parentPanel) {
        super(SwingUtilities.getWindowAncestor(parentPanel));
        setTitle(resourceBundle.getString("filters.dialog.title"));
        setModal(true);
        getRootPane().registerKeyboardAction(this::cancelAction, KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(this::okAction, KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.firstPageAction = firstPageAction;
        this.table = typedTable;
        this.typeClass = typedTable.getTypeClass();
        this.parentPanel = parentPanel;
        setLayout(new MigLayout());
        collectAndAddFilters();
        add(filterPanel, "span, center, wrap");
        add(createButtonsPanel(), "span, center, wrap");
        pack();
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new MigLayout());
        JButton okButton = new JButton(resourceBundle.getString("filters.dialog.ok"));
        JButton cancelButton = new JButton(resourceBundle.getString("filters.dialog.cancel"));
        JButton clearButton = new JButton(resourceBundle.getString("filters.dialog.reset"));

        clearButton.addActionListener(this::clearAction);
        cancelButton.addActionListener(this::cancelAction);
        okButton.addActionListener(this::okAction);

        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        return buttonPanel;
    }

    void clearAction(ActionEvent e){
        filterComponents.keySet().forEach(table::removeExtraParam);
        filterComponents.values().forEach(component -> {
            if(component instanceof IFilterComponent){
                ((IFilterComponent) component).clear();
            }
            if (component instanceof JFormattedTextField) {
                ((JFormattedTextField) component).setValue(null);
            }
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            }
            if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setSelectedItem(null);
            }
        });
    }

    void okAction(ActionEvent e) {
        filterComponents.keySet().forEach(table::removeExtraParam);
        filterComponents.entrySet().stream().filter(pair -> !getComponentValue(pair.getValue()).isEmpty()).forEach(pair -> {
            table.addExtraParam(pair.getKey(), getComponentValue(pair.getValue()));
        });
        firstPageAction.actionPerformed(e);
        setVisible(false);
    }

    String getComponentValue(Object component) {
        if (component instanceof IFilterComponent) return ((IFilterComponent) component).getFilterValue();
        if (component instanceof JTextField) return ((JTextField) component).getText();
        if (component instanceof BooleanCombobox) return ((BooleanCombobox) component).getSelectedValue();

        if (component instanceof JComboBox) {
            Object obj = ((JComboBox<?>) component).getSelectedItem();
            if (obj == null) return "";

            if (obj instanceof Enum) {
                return ((Enum<?>) obj).name();
            }
            return obj.toString();
        }
        throw new RuntimeException("Unknown component type" + component.getClass().getSimpleName());
    }
    void cancelAction(ActionEvent e){
        setVisible(false);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(parentPanel);
        }
        super.setVisible(b);
    }

    private void collectAndAddFilters() {
        if (typeClass.isAnnotationPresent(TableFilters.class)) {
            TableFilter[] filters = typeClass.getAnnotation(TableFilters.class).value();
            for (TableFilter filter : filters) {
                Component component = null;
                if (customFilterComponents.containsKey(filter.type())) {
                    addCustomFilter(filter, customFilterComponents.get(filter.type()));
                    filterPanel.add(customFilterComponents.get(filter.type()).getComponent(), "growx, wrap");
                    installLiseners(customFilterComponents.get(filter.type()).getComponent());
                    return;
                } else if (filter.type().isEnum())
                    component = addEnumFilter(filter);
                else component = addBasicFilter(filter);
                installLiseners(component);
                filterComponents.put(filter.name(), component);
            }
        }
    }

    private void addCustomFilter(TableFilter filter, IFilterComponent component) {
        basicAdd(getFilterLabel(filter), component.getComponent());
        filterComponents.put(filter.name(), component);
    }

    private void installLiseners(Component component) {
        component.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                    okAction(new ActionEvent(this, 0, "applyFilters"));
            }
        });
    }
    String getFilterLabel(TableFilter filter){
        return filter.label().isEmpty() ? filter.name() : bundleTranslator.getTranslation(filter.label());
    }
    private JComboBox<?> addEnumFilter(TableFilter filter) {
        JLabel label = new JLabel(getFilterLabel(filter));
        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.addItem(null);
        for(Object enumValue : filter.type().getEnumConstants()){
            comboBox.addItem(enumValue);
        }
        basicAdd(getFilterLabel(filter), comboBox);
        return comboBox;
    }

    private Component addBasicFilter(TableFilter filter) {
        switch (filter.type().getSimpleName()){
            case "String":
                return addTextFilter(filter);
            case "Integer":
                return addIntegerFilter(filter);
            case "BigDecimal":
            case "Float":
            case "Double":
                return addDecimalFilter(filter);
            case "Boolean":
                return addBooleanFilter(filter);
            default:
                throw new RuntimeException("Unknown filter type" + filter.type().getSimpleName());
        }
    }

    private BooleanCombobox addBooleanFilter(TableFilter filter) {
        BooleanCombobox comboBox = new BooleanCombobox();
        basicAdd(getFilterLabel(filter), comboBox);
        return comboBox;
    }

    private JFormattedTextField addDecimalFilter(TableFilter filter) {
        JFormattedTextField textField = new JFormattedTextField(NumberFormat.getNumberInstance());
        textField.setPreferredSize(new Dimension(100, 20));
        textField.setToolTipText(resourceBundle.getString("filters.decimal.column.tooltip"));
        basicAdd(getFilterLabel(filter), textField);
        return textField;
    }

    private JFormattedTextField addIntegerFilter(TableFilter filter) {
        JFormattedTextField textField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        textField.setPreferredSize(new Dimension(100, 20));
        textField.setToolTipText(resourceBundle.getString("filters.integer.column.tooltip"));
        basicAdd(getFilterLabel(filter), textField);
        return textField;
    }

    private JFormattedTextField addTextFilter(TableFilter filter) {
        JFormattedTextField textField = new JFormattedTextField();
        textField.setPreferredSize(new Dimension(100, 20));
        textField.setToolTipText(resourceBundle.getString("filters.string.column.tooltip"));
        basicAdd(getFilterLabel(filter), textField);
        return textField;
    }
    private void basicAdd(String label, Component component){
        filterPanel.add(new JLabel(label));
        filterPanel.add(component, "growx, wrap");
    }
    /**
     * Map of filter components
     */
    Map<String, Object> filterComponents = new HashMap<>();

    /**
     * Add custom filter component for current instance
     * @param label - label for filter
     * @param iFilterComponent - component to add with interface
     */
    public void addCustomFilterComponent(String label,String filterName, IFilterComponent iFilterComponent){
        basicAdd(label, iFilterComponent.getComponent());
        filterComponents.put(filterName, iFilterComponent);
        pack();
    }

    /**
    ** Add custom filter - global for all instances
     * @param typeClass - type of column
     * @param component - component to add
     */
    public static void registerCustomFilterComponent(Class<?> typeClass, IFilterComponent component){
        customFilterComponents.put(typeClass, component);
    }
}
