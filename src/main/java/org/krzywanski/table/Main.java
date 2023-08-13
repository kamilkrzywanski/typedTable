package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.table.DataProvider;
import org.krzywanski.table.table.TypedTablePanel;
import org.krzywanski.table.test.TestModel;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {


    /**
     * ONLY FOR TEST USING CLASS
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("JTable Example");
        frame.setLayout(new MigLayout());

        frame.add(TypedTablePanel.getTableWithProvider(new DataProvider<TestModel>(11) {
            @Override
            public List<TestModel> getData(int limit, int offest) {
                return Main.getData().stream().skip(offest).limit(limit).collect(Collectors.toList());
            }

            @Override
            public int getSize() {
                return Main.getData().size();
            }
        }, TestModel.class));
        frame.setSize(new Dimension(500, 500));
        frame.setVisible(true);
    }


    static List<TestModel> getData(){
        List<TestModel> list = new ArrayList<>();

        for(int i = 0 ; i<101;i++){
            TestModel testModel = new TestModel();
            testModel.setColumnA("TEST VALUE" + i);
            testModel.setColumnB(new BigDecimal(i+"."+i));
            list.add(testModel);
        }
        return  list;
    }
}