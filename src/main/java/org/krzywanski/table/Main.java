package org.krzywanski.table;

import org.krzywanski.table.table.TypedTable;
import org.krzywanski.table.test.TestModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {


    /**
     * ONLY FOR TEST USING CLASS
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("JTable Example");
        TestModel testModel = new TestModel();

////        String[] columnNames = {"Imię", "Nazwisko", "Wiek"};
//
//        Object[][] data = {
//                {"Jan", "Kowalski", 32},
//                {"Anna", "Nowak", 27},
//                {"Marek", "Pawlak", 41},
//                {"Karolina", "Wójcik", 22}
//        };

        List<TestModel> list = new ArrayList<TestModel>() {}; // creates a generic sub-type
        list.add(testModel);


        TypedTable<TestModel> table = new TypedTable<>(Collections.singletonList(testModel), testModel.getClass());


        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setSize(500, 200);

        frame.setVisible(true);
    }
}