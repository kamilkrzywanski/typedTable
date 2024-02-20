package org.krzywanski.test;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.PanelTableController;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.panel_v1.autopanel.buttons.DefaultControllerValidator;
import org.krzywanski.panel_v1.fields.TableValueController;
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

        new PanelTableController<>(table.table, autoPanel);

        leftPanel.add(autoPanel.buildPanel(2), "wrap");
        leftPanel.add(table, "grow,push");
        add(leftPanel, "grow,push");

    }

    private void buildRightPanel() {
        JPanel rightPanel = new JPanel(new MigLayout());
        JPanel controllPanel = new JPanel(new MigLayout());
        JTextField textField = new JFormattedTextField();
        JTextField textField2 = new JFormattedTextField();
        JTextField textField3 = new JFormattedTextField();

        ManualPanel<TestModelDto> manualPanel = new ManualPanel<>(() -> Main.getAllData().get(0), TestModelDto.class);
        manualPanel.setDataFlowAdapter(new TestModelService());
        manualPanel.connectFieldWithPanel("columnA", textField);
        manualPanel.connectFieldWithPanel("columnB", textField2);
        manualPanel.connectFieldWithPanel("columnC", textField3);


        controllPanel.add(new JLabel("ColumnA"));
        controllPanel.add(textField, "grow, wrap");
        controllPanel.add(manualPanel, "span, wrap");


        TypedTablePanel<TestModelDto> table = TypedTablePanel.getTableWithData(Main.getAllData(), TestModelDto.class, 3);

        rightPanel.add(controllPanel, "wrap");
        rightPanel.add(table, "grow,push");


        add(rightPanel, "grow,push");
        setVisible(true);
        setPreferredSize(new Dimension(1500, 600));
        pack();
    }
}
