package org.krzywanski.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.table.DataProvider;
import org.krzywanski.table.table.SortColumn;
import org.krzywanski.table.table.SortOrder;
import org.krzywanski.table.table.TypedTablePanel;
import org.krzywanski.table.test.TestModel;

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
        frame.add(TypedTablePanel.getTableWithProvider(new DataProvider<TestModel>(20) {
            @Override
            public List<TestModel> getData(int limit, int offest, SortColumn sortColumn) {

                if(sortColumn!= null && sortColumn.getColumnName().equals("columnB")){

                    if(sortColumn.getSortOrder() == SortOrder.ASCENDING)
                        return Main.getData().stream().sorted((o1, o2) -> o1.getColumnB().compareTo(o2.getColumnB())).skip(offest).limit(limit).collect(Collectors.toList());
                    else
                        return Main.getData().stream().sorted((o1, o2) -> o2.getColumnB().compareTo(o1.getColumnB())).skip(offest).limit(limit).collect(Collectors.toList());
                }

                return Main.getData().stream().skip(offest).limit(limit).collect(Collectors.toList());
            }

            @Override
            public int getSize() {
                return Main.getData().size();
            }
        }, TestModel.class), "grow,push");
        frame.setVisible(true);
        frame.pack();
    }


    static List<TestModel> getData(){
        List<TestModel> list = new ArrayList<>();

        for(int i = 0 ; i<101;i++){
            TestModel TestModel2 = new TestModel();
            TestModel2.setColumnA("TEST VALUE" + i);
            TestModel2.setColumnB(new Double(i+"."+i));
            list.add(TestModel2);
        }
        return  list;
    }
}