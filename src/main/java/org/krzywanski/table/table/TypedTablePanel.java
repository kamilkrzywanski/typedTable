package org.krzywanski.table.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.test.TestFormatClass;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.List;
import java.util.Objects;

/**
 * Container for table and pagination buttons
 *
 * @param <T>
 */
public class TypedTablePanel<T> extends JPanel {

    JButton nextPageButton;
    JButton exportExcelButton;
    JButton prevPageButton;
    JButton lastPageButton;
    JButton firstPageButton;
    JLabel page;

    final TypedTable<T> table;

    public static <T> TypedTablePanel<T> getTableWithData(List<T> dataList, Class<T> typeClass) {
        return new TypedTablePanel<>(dataList, typeClass, null);
    }

    public static <T> TypedTablePanel<T> getTableWithProvider(DataProvider<T> provider, Class<T> typeClass) {
        return new TypedTablePanel<>(null, typeClass, provider);
    }


    private TypedTablePanel(List<T> dataList, Class<? extends T> typeClass, DataProvider<T> provider) {
        super(new MigLayout());
        createButtons();
        table = new TypedTable<>(dataList, typeClass, provider);
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
        table.addFistPageListener(e -> firstPageAction());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow,push,wrap");
        firstPageAction();
    }

    void createButtons() {
        exportExcelButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("export_excel.png")));
        nextPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("next.png")));
        prevPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("back.png")));
        lastPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-forward-button.png")));
        firstPageButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-backward.png")));
        page = new JLabel("");

        addButton(exportExcelButton, "split, al right");
        addButton(firstPageButton, "");
        addButton(prevPageButton, "");
        add(page);
        addButton(nextPageButton, "");
        addButton(lastPageButton, "wrap");

        firstPageButton.addActionListener(e -> firstPageAction());
        prevPageButton.addActionListener(e -> prevPageAction());
        lastPageButton.addActionListener(e -> lastPageAction());
        nextPageButton.addActionListener(e -> nextPageAction());
        exportExcelButton.addActionListener(e -> {
            try {
                table.exportToExcel(Objects.requireNonNull(selectFiles()).toPath());
            } catch (Exception ex) {
             System.out.println("export failed");
            }
        });

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

    void addButton(JButton button, String constraints) {
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
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int returnValue = chooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}



