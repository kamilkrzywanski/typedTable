package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.table.DataProvider;
import org.krzywanski.table.table.SortColumn;
import org.krzywanski.table.table.TypedTablePanel;
import org.krzywanski.table.test.TestModel2;

import javax.swing.*;
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
        frame.add(TypedTablePanel.getTableWithProvider(new DataProvider<TestModel2>(20) {
            @Override
            public List<TestModel2> getData(int limit, int offest, SortColumn sortColumn) {
                return Main.getData().stream().skip(offest).limit(limit).collect(Collectors.toList());
            }

            @Override
            public int getSize() {
                return Main.getData().size();
            }
        }, TestModel2.class), "grow,push");
        frame.setVisible(true);
        frame.pack();
    }


    static List<TestModel2> getData(){
        List<TestModel2> list = new ArrayList<>();

        for(int i = 0 ; i<101;i++){
            TestModel2 TestModel2 = new TestModel2();
            TestModel2.setColumnA("TEST VALUE" + i);
            TestModel2.setColumnB(new Double(i+"."+i));
            list.add(TestModel2);
        }
        return  list;
    }
}