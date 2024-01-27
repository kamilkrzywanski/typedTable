package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.annot.TableFilters;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.components.PopupDialog;
import org.krzywanski.table.providers.GenericSelectionListener;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.table.providers.TableDataProvider;
import org.krzywanski.table.utils.Page;
import org.krzywanski.test.TestFormatClass;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

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
        table.addCustomFormatter(TestFormatClass.class, new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                return new StringBuffer("FORMAT SUCCES");
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        filterDialog = new FilterDialog((e) -> firstPageAction(), table, this);
        table.addFistPageListener(e -> firstPageAction());
        table.addSearchPhaseSupplier(() -> popupDialog.getText());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow,push,wrap");
        firstPageAction();
    }

    void initButtons(){
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

        if(table.isPaginationEnabled()) {
            addButton(firstPageButton, "");
            addButton(prevPageButton, "");
        }
        add(pageLabel);
        if(table.isPaginationEnabled()) {
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
            System.out.println("export failed");
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

    private void firstPageAction() {
        setLabelText(table.firstPageAction());
    }

    private void setLabelText(Page page) {

        if(table.isPaginationEnabled())
            this.pageLabel.setText(page.getCurrentPage() + "/" + page.getTotalPages() + " (" + page.getTotalElements() + ")" );
        else
            this.pageLabel.setText("(" + page.getTotalElements() + ")" );
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

    public <E> E getSelectedValueOrDefault(Function<T, E> mapper, E defaultValue) {
        return table.getSelectedValueOrDefault(mapper, defaultValue);
    }

    public void addComuptedColumn(String columnC, Function<T, Object> o) {
        table.addComputedColumn(columnC, o);
    }
}
