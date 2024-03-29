package org.krzywanski.test;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;
import org.krzywanski.panel_v1.PanelTableController;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.panel_v1.autopanel.buttons.DefaultControllerValidator;
import org.krzywanski.panel_v1.fields.TableValueController;
import org.krzywanski.panel_v1.fields.TextFieldWithTableSelect;
import org.krzywanski.panel_v1.manualPanel.ManualPanel;
import org.krzywanski.table.TypedTablePanel;
import org.krzywanski.table.providers.DefaultDataPrivder;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.model.TestFormatClass;
import org.krzywanski.test.service.TestModelService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TreeSet;

public class ExampleUI extends JFrame {

    public ExampleUI() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Typed Framework Example");
        setLayout(new MigLayout());

        buildLeftPanel();
        buildRightPanel();

    }

    public void buildLeftPanel() {
        JPanel leftPanel = new JPanel(new MigLayout());


        TypedTablePanel<TestModelDto> table = TypedTablePanel.getTableWithProvider(new DefaultDataPrivder<>(10, Main::getData, Main::getSize), TestModelDto.class);
        table.addComuptedColumn("Computed column", String.class, value -> value.getColumnA() + " " + value.getColumnB());
        table.addGenericSelectionListener(element -> {
            if (element != null)
                System.out.println(element.getColumnA());
        });
        TreeSet<TestModelDto> collection = new TreeSet<>();
        table.addMultiSelectColumn("Multi select column", collection);

        TypedAutoPanel<TestModelDto> autoPanel = new TypedAutoPanel<>(() -> table.getSelectedItem(), TestModelDto.class);
        autoPanel.setDataFlowAdapter(new TestModelService());

        TypedTablePanel<TestFormatClass> selectPanel = TypedTablePanel.getTableWithData(List.of(new TestFormatClass("A"), new TestFormatClass("B")), TestFormatClass.class);
        autoPanel.addDataEditor("testFormatClass", TestFormatClass.class, new TableValueController<>(selectPanel, "Select format class"));

        autoPanel.addInsertValidator(new DefaultControllerValidator<>((r) -> !"D".equals(r.getColumnA()), () -> "ERROR"));
        autoPanel.setPreferredSize(new Dimension(600, 300));

        new PanelTableController<>(table, autoPanel);
        table.updateRow(4, testModelDto -> {
            testModelDto.setColumnA("TestChange");
            return testModelDto;
        });
        leftPanel.add(autoPanel.buildPanel(2), "wrap");
        leftPanel.add(table, "grow,push");
        add(leftPanel, "grow,push");

    }

    private void buildRightPanel() {
        JPanel rightPanel = new JPanel(new MigLayout());
        JPanel controllPanel = new JPanel(new MigLayout());
        JTextField textField = new JTextField();
        JTextField textField3 = new JTextField();
        JSpinner spinner = new JSpinner();
        JXDatePicker datePicker = new JXDatePicker();
        TextFieldWithTableSelect<TestFormatClass> selectPanel = TextFieldWithTableSelect.getTextWithTableSelect(List.of(new TestFormatClass("A"), new TestFormatClass("B")), "TestFormatClass.class");

        TypedTablePanel<TestModelDto> table = TypedTablePanel.getTableWithData(Main.getAllData(), TestModelDto.class, 3);
        table.installDataUpdateAdapter(new TestModelService());
        ManualPanel<TestModelDto> manualPanel = new ManualPanel<>(() -> table.getSelectedItem(), TestModelDto.class);
        manualPanel.setDataFlowAdapter(new TestModelService());
        manualPanel.connectFieldWithPanel("columnA", textField);
        manualPanel.connectFieldWithPanel("columnB", spinner);
        manualPanel.connectFieldWithPanel("columnC", textField3);
        manualPanel.connectFieldWithPanel("testFormatClass", selectPanel);
        manualPanel.connectFieldWithPanel("date", datePicker);

        controllPanel.add(new JLabel("ColumnA"));
        controllPanel.add(textField, "grow, wrap");
        controllPanel.add(new JLabel("ColumnB"));
        controllPanel.add(spinner, "grow, wrap");
        controllPanel.add(new JLabel("ColumnC"));
        controllPanel.add(textField3, "grow, wrap");
        controllPanel.add(new JLabel("TestFormatClass"));
        controllPanel.add(selectPanel, "grow, wrap");
        controllPanel.add(new JLabel("Date"));
        controllPanel.add(datePicker, "grow, wrap");
        controllPanel.add(manualPanel, "span, wrap");



        rightPanel.add(controllPanel, "wrap");
        rightPanel.add(table, "grow,push");

        TextFieldWithTableSelect<TestFormatClass> selectPanel2 = TextFieldWithTableSelect.getTextWithTableSelect(List.of(new TestFormatClass("A"), new TestFormatClass("B")), "TestFormatClass.class");
        table.setTableEditorForClass(TestFormatClass.class, selectPanel2);

        new PanelTableController<>(table, manualPanel);

        add(rightPanel, "grow,push");
        setVisible(true);
        setPreferredSize(new Dimension(1500, 600));
        pack();
    }
}
