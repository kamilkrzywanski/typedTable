package org.krzywanski.test;

import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.krzywanski.table.SortColumn;
import org.krzywanski.table.TypedTablePanel;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.DefaultDataPrivder;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.mapper.TestModelMapper;
import org.krzywanski.test.model.TestModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    public static Session session = sessionFactory.openSession();

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
        TypedTablePanel<TestModelDto> panel = TypedTablePanel.getTableWithProvider(new DefaultDataPrivder<>(20, Main::getData, Main::getSize), TestModelDto.class);
        TypedTablePanel<TestModelDto> panel2 = TypedTablePanel.getTableWithData( Main.getAllData(), TestModelDto.class, 3);
        panel.addComuptedColumn("Computed column",String.class,  value -> value.getColumnA() + " " + value.getColumnB());
        panel.addGenericSelectionListener(element -> System.out.println(element.getColumnA()));
        TreeSet<TestModelDto> collection = new TreeSet<>();
        panel.addMultiSelectColumn("Multi select column", collection);
        frame.add(panel, "grow,push");
        frame.add(panel2, "grow,push");
        frame.setVisible(true);
        frame.pack();
    }

    public static List<TestModelDto> getAllData() {
        return Main.getData();
    }
    public static List<TestModelDto> getData(int limit, int offest, List<SortColumn> sortColumn, String searchString, ActionType actionType, Map<String, String> extraParams) {
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

    static List<TestModelDto> getData() {
        Query<TestModel> list = session.createQuery("from TestModel", TestModel.class);
        List<TestModel> testModels = list.list();
        return testModels.stream().map(testModel -> TestModelMapper.mapTestModelToDto(testModel, new TestModelDto())).collect(Collectors.toList());
    }
}