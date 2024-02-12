package org.krzywanski.test;

import com.formdev.flatlaf.FlatLightLaf;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import net.miginfocom.swing.MigLayout;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jdesktop.swingx.JXDatePicker;
import org.krzywanski.panel_v1.PanelTableController;
import org.krzywanski.panel_v1.TypedPanelFields;
import org.krzywanski.panel_v1.fields.DefaultFieldProvider;
import org.krzywanski.panel_v1.fields.TableValueController;
import org.krzywanski.table.SortColumn;
import org.krzywanski.table.TypedTablePanel;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.table.providers.DefaultDataPrivder;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.mapper.TestModelMapper;
import org.krzywanski.test.model.TestFormatClass;
import org.krzywanski.test.model.TestModel;
import org.krzywanski.test.panelfield.DateValueController;
import org.krzywanski.test.service.TestModelService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    /**
     * ONLY FOR TEST USING CLASS
     */
    public static void main(String[] args) {
        TypedPanelFields.registerField(Date.class, new DefaultFieldProvider<>(new JXDatePicker(), component -> new DateValueController(component)));
        FlatLightLaf.setup();
        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.showHorizontalLines", true);
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
        TypedTablePanel<TestModelDto> panel = TypedTablePanel.getTableWithProvider(new DefaultDataPrivder<>(10, Main::getData, Main::getSize), TestModelDto.class);
        panel.addComuptedColumn("Computed column", String.class, value -> value.getColumnA() + " " + value.getColumnB());
        panel.addGenericSelectionListener(element -> {
            if (element != null)
                System.out.println(element.getColumnA());
        });
        TreeSet<TestModelDto> collection = new TreeSet<>();
        panel.addMultiSelectColumn("Multi select column", collection);

//        panel.addCustomFormatter(TestFormatClass.class, new Format() {
//            @Override
//            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//                return new StringBuffer("FORMAT");
//            }
//
//            @Override
//            public Object parseObject(String source, ParsePosition pos) {
//                return null;
//            }
//        });
        TypedAutoPanel<TestModelDto> autoPanel = new TypedAutoPanel<>(() -> panel.getSelectedItem(), TestModelDto.class);
        autoPanel.setDataFlowAdapter(new TestModelService());

        TypedTablePanel<TestFormatClass> selectPanel = TypedTablePanel.getTableWithData(List.of(new TestFormatClass("A"), new TestFormatClass("B")), TestFormatClass.class);
        autoPanel.addDataEditor("testFormatClass", TestFormatClass.class, new TableValueController<>(selectPanel, "Select format class"));

        new PanelTableController<>(panel.table, autoPanel);

        frame.add(autoPanel.buildPanel(), "wrap");

        frame.add(panel, "grow,push");
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(1500, 600));
        frame.pack();

    }

    public static List<TestModelDto> getAllData() {
        return Main.getData(0, Integer.MAX_VALUE);
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

        if (limit == -1)
            return Main.getData(0, Integer.MAX_VALUE);
        return Main.getData(offest, limit).stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).collect(Collectors.toList());
    }

    public static int getSize(String searchString, Map<String, String> extraParams) {
        return getSize();
//        return (int) Main.getData(0, Integer.MAX_VALUE).stream().filter(testModel -> testModel.getColumnA().toLowerCase().contains(Objects.requireNonNullElse(searchString, ""))).count();
    }

    static List<TestModelDto> getData(int from, int limit) {
        CriteriaBuilder cb = sessionFactory.openSession().getCriteriaBuilder();
        CriteriaQuery<TestModel> cr = cb.createQuery(TestModel.class);
        Root<TestModel> root = cr.from(TestModel.class);
        cr.orderBy(cb.asc(root.get("id")));
        cr.select(root);
        Query<TestModel> query = sessionFactory.openSession().createQuery(cr);
        query.setFirstResult(from).setMaxResults(limit);
        List<TestModel> testModels = query.getResultList();
        return testModels.stream().map(testModel -> TestModelMapper.mapTestModelToDto(testModel, new TestModelDto())).collect(Collectors.toList());

    }

    static int getSize() {
        CriteriaBuilder cb = sessionFactory.openSession().getCriteriaBuilder();
        CriteriaQuery<TestModel> cr = cb.createQuery(TestModel.class);
        Root<TestModel> root = cr.from(TestModel.class);
        cr.select(root);

        Query<TestModel> query = sessionFactory.openSession().createQuery(cr);
        return query.getResultList().size();
    }
}