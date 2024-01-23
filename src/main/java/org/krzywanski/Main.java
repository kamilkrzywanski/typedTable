package org.krzywanski;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.*;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.DefaultDataPrivder;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.test.TestModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {


    /**
     * ONLY FOR TEST USING CLASS
     */
    public static void main(String[] args) {
        FilterDialog.registerCustomFilterComponent(Boolean.class, new IFilterComponent() {
            final JCheckBox checkBox = new JCheckBox();
            @Override
            public String getFilterValue() {
                return checkBox.isSelected() ? "true" : "false";
            }

            @Override
            public Component getComponent() {
                return checkBox;
            }

            @Override
            public void clear() {
                checkBox.setSelected(false);
            }
        });
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("JTable Example");
        frame.setLayout(new MigLayout());
        TypedTablePanel<TestModel> panel = TypedTablePanel.getTableWithProvider(new DefaultDataPrivder<>(20, Main::getData, Main::getSize), TestModel.class);
        TypedTablePanel<TestModel> panel2 = TypedTablePanel.getTableWithData( Main.getAllData(), TestModel.class);

        panel.addGenericSelectionListener(element -> System.out.println(element.getColumnA()));
        frame.add(panel, "grow,push");
        frame.add(panel2, "grow,push");
        frame.setVisible(true);
        frame.pack();
    }

    public static List<TestModel> getAllData() {
        return Main.getData();
    }
    public static List<TestModel> getData(int limit, int offest, List<SortColumn> sortColumn, String searchString, ActionType actionType, Map<String, String> extraParams) {
        extraParams.forEach((s, s2) -> System.out.println(s + " " + s2));
//        if (sortColumn != null && !sortColumn.isEmpty() && sortColumn.get(0).getColumnName().equals("columnB")) {
//
//            if (SortOrder.ASCENDING.equals(sortColumn.get(0).getSortOrder()))
//                return Main.getData().stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).sorted((o1, o2) -> o1.getColumnB().compareTo(o2.getColumnB())).skip(offest).limit(limit).collect(Collectors.toList());
//            else
//                return Main.getData().stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).sorted((o1, o2) -> o2.getColumnB().compareTo(o1.getColumnB())).skip(offest).limit(limit).collect(Collectors.toList());
//        }

        if(limit == -1)
            return Main.getData();
        return Main.getData().stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).skip(offest).limit(limit).collect(Collectors.toList());
    }

    public static int getSize(String searchString, Map<String, String> extraParams) {
        return (int) Main.getData().stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).count();
    }

    static List<TestModel> getData() {
        List<TestModel> list = new ArrayList<>();

        for (int i = 0; i < 101; i++) {
            TestModel TestModel2 = new TestModel();
            TestModel2.setColumnA("TEST VALUE" + i);
            TestModel2.setColumnB(Double.parseDouble(i + "." + i));
            list.add(TestModel2);
        }
        return list;
    }
}