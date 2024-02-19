package org.krzywanski.test;

import com.formdev.flatlaf.FlatLightLaf;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jdesktop.swingx.JXDatePicker;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.panel_v1.TypedPanelFields;
import org.krzywanski.panel_v1.fields.DefaultFieldProvider;
import org.krzywanski.table.SortColumn;
import org.krzywanski.table.components.FilterDialog;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.IFilterComponent;
import org.krzywanski.test.dto.TestModelDto;
import org.krzywanski.test.mapper.TestModelMapper;
import org.krzywanski.test.model.TestModel;
import org.krzywanski.test.panelfield.DateValueController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    public static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    /**
     * ONLY FOR TEST USING CLASS
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.showHorizontalLines", true);


        TypedPanelFields.registerField(Date.class, new DefaultFieldProvider<>(new JXDatePicker(), component -> new DateValueController(component)));
        TypedFrameworkConfiguration.addResourceBundle("Messages");
        TypedFrameworkConfiguration.addResourceBundle("TestModelDto");

        addExampleData();

        FilterDialog.registerCustomFilterComponent(Boolean.class, () -> new IFilterComponent() {
            private final JCheckBox checkBox = new JCheckBox();
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

        new ExampleUI();

    }


    private static void addExampleData() {
        try {
            InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("h2_fill_tables.sql");
            String sql = new String(Objects.requireNonNull(resourceAsStream).readAllBytes(), StandardCharsets.UTF_8);
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Query<Boolean> query = session.createNativeQuery(sql + ";", Boolean.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<TestModelDto> getAllData() {
        return Main.getData(0, Integer.MAX_VALUE, "");
    }

    public static List<TestModelDto> getData(int limit, int offest, List<SortColumn> sortColumn, String searchString, ActionType actionType, Map<String, String> extraParams) {
        extraParams.forEach((s, s2) -> System.out.println(s + " " + s2));

        if (limit == -1)
            return Main.getData(0, Integer.MAX_VALUE, searchString);
        return Main.getData(offest, limit, searchString);
    }

    public static int getSize(String searchString, Map<String, String> extraParams) {
        return getSize(searchString);
    }

    private static Query<TestModel> getQuery(String searchString) {
        CriteriaBuilder cb = sessionFactory.openSession().getCriteriaBuilder();
        CriteriaQuery<TestModel> cr = cb.createQuery(TestModel.class);
        Root<TestModel> root = cr.from(TestModel.class);

        if (searchString != null && !searchString.isEmpty()) {
            cr.where(cb.or(cb.like(root.get("columnA"), "%" + searchString + "%"), cb.like(root.get("columnC"), "%" + searchString + "%")));
        }
        cr.select(root);

        return sessionFactory.openSession().createQuery(cr);
    }

    static List<TestModelDto> getData(int from, int limit, String searchString) {
        Query<TestModel> query = getQuery(searchString);
        query.setFirstResult(from).setMaxResults(limit);
        List<TestModel> testModels = query.getResultList();
        return testModels.stream().map(testModel -> TestModelMapper.mapTestModelToDto(testModel, new TestModelDto())).collect(Collectors.toList());

    }

    static int getSize(String searchString) {
        Query<TestModel> query = getQuery(searchString);
        return query.getResultList().size();
    }

}