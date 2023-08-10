package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.table.DataProvider;
import org.krzywanski.table.table.TypedTablePanel;
import org.krzywanski.table.test.TestModel;

import javax.swing.*;
import java.awt.*;
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
        frame.setLayout(new MigLayout());

//        frame.add(TypedTablePanel.getTableWithData(getData(), TestModel.class));

        frame.add(TypedTablePanel.getTableWithProvider(new DataProvider<TestModel>(10) {
            @Override
            public List<TestModel> getData(int limit, int offest) {
                return Collections.singletonList(Main.getData().get(offest));
            }

            @Override
            public long getSize() {
                return Main.getData().size();
            }
        }, TestModel.class));
        frame.setSize(new Dimension(500, 500));
        frame.setVisible(true);
    }


    static List<TestModel> getData(){

        TestModel testModel = new TestModel();
        testModel.setColumnA("TEST VALUE");
        TestModel testModel2 = new TestModel();
        testModel2.setColumnA("TEST VALUE2");
        List<TestModel> list = new ArrayList<TestModel>() {}; // creates a generic sub-type
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        list.add(testModel);
        list.add(testModel2);
        return  list;
    }
}