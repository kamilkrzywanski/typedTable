package org.krzywanski.test;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.PanelTableController;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.panel_v1.autopanel.buttons.DefaultControllerValidator;
import org.krzywanski.panel_v1.fields.TableValueController;
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
        TypedTablePanel<TestModelDto> table = TypedTablePanel.getTableWithProvider(new DefaultDataPrivder<>(10, Main::getData, Main::getSize), TestModelDto.class);
        table.addComuptedColumn("Computed column", String.class, value -> value.getColumnA() + " " + value.getColumnB());
        table.addGenericSelectionListener(element -> {
            if (element != null)
                System.out.println(element.getColumnA());
        });
        TreeSet<TestModelDto> collection = new TreeSet<>();
        table.addMultiSelectColumn("Multi select column", collection);

        TypedAutoPanel<TestModelDto> autoPanel = new TypedAutoPanel<>(() -> table.getSelectedItem(), TestModelDto.class, true);
        autoPanel.setDataFlowAdapter(new TestModelService());

        TypedTablePanel<TestFormatClass> selectPanel = TypedTablePanel.getTableWithData(List.of(new TestFormatClass("A"), new TestFormatClass("B")), TestFormatClass.class);
        autoPanel.addDataEditor("testFormatClass", TestFormatClass.class, new TableValueController<>(selectPanel, "Select format class"));

        autoPanel.addInsertValidator(new DefaultControllerValidator<>((r) -> !"D".equals(r.getColumnA()), () -> "ERROR"));
        autoPanel.setPreferredSize(new Dimension(600, 300));

        new PanelTableController<>(table.table, autoPanel);

        add(autoPanel.buildPanel(2), "wrap");
        add(table, "grow,push");

    }

    private void buildRightPanel() {
        TypedTablePanel<TestModelDto> panel2 = TypedTablePanel.getTableWithData(Main.getAllData(), TestModelDto.class, 3);

        add(panel2, "grow,push");
        setVisible(true);
        setPreferredSize(new Dimension(1500, 600));
        pack();
    }
}
