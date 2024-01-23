package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.annot.TableFilters;
import org.krzywanski.table.providers.GenericSelectionListener;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.table.providers.TableDataProvider;
import org.krzywanski.table.utils.Pair;
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
    JLabel page;

    public final TypedTable<T> table;
    final FilterDialog filterDialog;

    public static <T> TypedTablePanel<T> getTableWithData(List<T> dataList, Class<T> typeClass) {
        return new TypedTablePanel<>(dataList, typeClass, null);
    }

    public static <T> TypedTablePanel<T> getTableWithProvider(TableDataProvider<T> provider, Class<T> typeClass) {
        return new TypedTablePanel<>(null, typeClass, provider);
    }

    private TypedTablePanel(List<T> dataList, Class<? extends T> typeClass, TableDataProvider<T> provider) {
        super(new MigLayout());
        table = new TypedTable<>(dataList, typeClass, provider, 1);
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
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow,push,wrap");
        firstPageAction();
    }

    private void createButtons() {
        filterButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("filter-symbol.png")));
        exportExcelButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("export_excel.png")));
        nextPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("next.png")));
        prevPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("back.png")));
        lastPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-forward-button.png")));
        firstPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-backward.png")));
        searchButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("search.png")));
        page = new JLabel("");
        popupDialog = new PopupDialog(e -> firstPageAction());

        if (table.typeClass.isAnnotationPresent(TableFilters.class)) {
            addButton(filterButton, "split, al right");
            addButton(exportExcelButton, "");
        } else {
            addButton(exportExcelButton, "split, al right");
        }
        addButton(firstPageButton, "");
        addButton(prevPageButton, "");
        add(page);
        addButton(nextPageButton, "");
        addButton(lastPageButton, "");
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
        Pair<Integer, Integer> pair = table.nextPageAction();
        page.setText(pair.getFirst() + "/" + pair.getSecond());
    }

    private void lastPageAction() {
        Pair<Integer, Integer> pair = table.lastPageAction();
        page.setText(pair.getFirst() + "/" + pair.getSecond());
    }

    private void prevPageAction() {
        Pair<Integer, Integer> pair = table.prevPageAction();
        page.setText(pair.getFirst() + "/" + pair.getSecond());
    }

    private void firstPageAction() {
        Pair<Integer, Integer> pair = table.firstPageAction();
        page.setText(pair.getFirst() + "/" + pair.getSecond());
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
}