package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.table.TypedTablePanel;
import org.krzywanski.table.test.TestModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
        frame.setLayout(new MigLayout());
        TestModel testModel = new TestModel();
        testModel.setColumnA("TEST VALUE");
        TestModel testModel2 = new TestModel();
        testModel2.setColumnA("TEST VALUE2");
        List<TestModel> list = new ArrayList<TestModel>() {}; // creates a generic sub-type
        list.add(testModel);
        list.add(testModel2);


        frame.add(TypedTablePanel.getTableWithData(list, testModel.getClass()));
        frame.setSize(new Dimension(500,500));
        frame.setVisible(true);
    }
}