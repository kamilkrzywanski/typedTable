package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.annot.TableFilters;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.components.PopupDialog;
import org.krzywanski.table.providers.GenericSelectionListener;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.table.providers.TableDataProvider;
import org.krzywanski.table.utils.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.Format;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container for table and pagination buttons
 *
 * @param <T>
 */
public class TypedTablePanel<T> extends JPanel {
    PopupDialog popupDialog;
    JButton filterButton;
    JButton exportExcelButton;
    JButton firstPageButton;
    JButton prevPageButton;
    JButton nextPageButton;
    JButton lastPageButton;
    JButton searchButton;
    JLabel pageLabel;

    public final TypedTable<T> table;
    final FilterDialog filterDialog;

    public static <T> TypedTablePanel<T> getTableWithData(List<T> dataList, Class<T> typeClass, int id) {
        return new TypedTablePanel<>(dataList, typeClass, null, id);
    }

    public static <T> TypedTablePanel<T> getTableWithProvider(TableDataProvider<T> provider, Class<T> typeClass, int id) {
        return new TypedTablePanel<>(null, typeClass, provider, id);
    }

    public static <T> TypedTablePanel<T> getTableWithData(List<T> dataList, Class<T> typeClass) {
        return new TypedTablePanel<>(dataList, typeClass, null, 1);
    }

    public static <T> TypedTablePanel<T> getTableWithProvider(TableDataProvider<T> provider, Class<T> typeClass) {
        return new TypedTablePanel<>(null, typeClass, provider, 1);
    }

    private TypedTablePanel(List<T> dataList, Class<? extends T> typeClass, TableDataProvider<T> provider, int id) {
        super(new MigLayout());
        table = new TypedTable<>(dataList, typeClass, provider, id);
        createButtons();
        filterDialog = new FilterDialog((e) -> firstPageAction(), table, this);
        table.addFistPageListener(e -> firstPageAction());
        table.addSearchPhaseSupplier(() -> popupDialog.getText());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow,push,wrap");
        firstPageAction();
    }

    void initButtons() {
        filterButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("filter-symbol.png")));
        exportExcelButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("export_excel.png")));
        nextPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("next.png")));
        prevPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("back.png")));
        lastPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-forward-button.png")));
        firstPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-backward.png")));
        searchButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("search.png")));
        pageLabel = new JLabel("");
        popupDialog = new PopupDialog(e -> firstPageAction());
    }

    private void createButtons() {
        initButtons();

        if (table.typeClass.isAnnotationPresent(TableFilters.class)) {
            addButton(filterButton, "split, al right");
            addButton(exportExcelButton, "");
        } else {
            addButton(exportExcelButton, "split, al right");
        }

        if (table.isPaginationEnabled()) {
            addButton(firstPageButton, "");
            addButton(prevPageButton, "");
        }
        add(pageLabel);
        if (table.isPaginationEnabled()) {
            addButton(nextPageButton, "");
            addButton(lastPageButton, "");
        }
        addButton(searchButton, "wrap");

        filterButton.addActionListener(e -> filterAction());
        exportExcelButton.addActionListener(e -> exportExcelAction());
        firstPageButton.addActionListener(e -> firstPageAction());
        prevPageButton.addActionListener(e -> prevPageAction());
        nextPageButton.addActionListener(e -> nextPageAction());
        lastPageButton.addActionListener(e -> lastPageAction());
        searchButton.addActionListener(e -> searchAction());
    }

    private void filterAction() {
        filterDialog.setVisible(true);
    }

    private void searchAction() {
        popupDialog.setVisible(true, searchButton);
    }

    private void exportExcelAction() {
        try {
            table.exportToExcel(Objects.requireNonNull(selectFiles()).toPath());
        } catch (Exception ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "export failed", ex);
        }
    }

    private void nextPageAction() {
        setLabelText(table.nextPageAction());
    }

    private void lastPageAction() {
        setLabelText(table.lastPageAction());
    }

    private void prevPageAction() {
        setLabelText(table.prevPageAction());
    }

    public void firstPageAction() {
        setLabelText(table.firstPageAction());
    }

    private void setLabelText(Page page) {

        if (table.isPaginationEnabled())
            this.pageLabel.setText(page.getCurrentPage() + "/" + page.getTotalPages() + " (" + page.getTotalElements() + ")");
        else
            this.pageLabel.setText("(" + page.getTotalElements() + ")");
    }

    private void addButton(JButton button, String constraints) {
        button.setFocusable(false);
        button.setBackground(null);
        button.setOpaque(true);
        button.setBackground(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        add(button, constraints);
    }


    private File selectFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel file", "xlsx"));
        int returnValue = chooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Get selected item from table
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return table.getSelectedItem();
    }

    /**
     * @return - returns list of selected indices
     */
    public List<T> getSelectedItems() {
        return table.getSelectedItems();
    }

    /**
     * Add custom filter component to filter dialog
     *
     * @param label            - label for component
     * @param filterName       - name of filter
     * @param iFilterComponent - component
     */
    public void addCustomFilterComponent(String label, String filterName, IFilterComponent iFilterComponent) {
        filterDialog.addCustomFilterComponent(label, filterName, iFilterComponent);
    }

    public void addGenericSelectionListener(GenericSelectionListener<T> listener) {
        table.addGenericSelectionListener(listener);
    }

    /**
     * Return value from selected row or default when row not found
     *
     * @param mapper       - mapper to map row to result
     * @param defaultValue - default value when row not found
     * @param <E>          - return type of result
     * @return - returns result of function
     */
    public <E> E getSelectedValueOrDefault(Function<T, E> mapper, E defaultValue) {
        return table.getSelectedValueOrDefault(mapper, defaultValue);
    }

    /**
     * Add function to table at runtime
     *
     * @param columnName        - name of column to add
     * @param columnClass       - class of column to add
     * @param computingFunction - function passed to result cell
     * @param <C>               - class of result column
     */
    public <C> void addComuptedColumn(String columnName, Class<C> columnClass, Function<T, C> computingFunction) {
        table.addComputedColumn(columnName, columnClass, computingFunction);
        firstPageAction();
    }

    /**
     * @param columnName - name of column to add
     * @param resultList - Tree set to in case of custom method to compare objects
     *                   TypeClass needs to implement Comparable interface if you use TreeSet without comparator
     */
    public void addMultiSelectColumn(String columnName, TreeSet<T> resultList) {
        table.addMultiSelectColumn(columnName, resultList);
        firstPageAction();
    }

    /**
     * Adds custom formatter for selected class
     *
     * @param classFormat - class to format
     * @param format      - format
     */
    public void addCustomFormatter(Class<?> classFormat, Format format) {
        table.addCustomFormatter(classFormat, format);
    }

    public void addTableMouseListener(MouseListener listener){
        table.addMouseListener(listener);
    }

    public void addTableKeyListener(KeyListener listener){
        table.addKeyListener(listener);
    }

    /**
     * Set select first row after data change
     *
     * @param selectFirstRow - true if first row should be selected
     */
    public void setSelectFirstRow(boolean selectFirstRow) {
        table.setSelectFirstRow(selectFirstRow);
    }

    public void setDataAt(int row, T data) {
        table.setDataAt(row, data);
    }

    public void refreshData() {
        setLabelText(table.refreshData());
    }
}
