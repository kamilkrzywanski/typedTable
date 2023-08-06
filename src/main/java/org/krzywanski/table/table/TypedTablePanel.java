package org.krzywanski.table.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.test.TestModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.List;

/**
 * Container for table and pagination buttons
 * @param <T>
 */
public class TypedTablePanel<T> extends JPanel {

    JButton nextButton;
    JButton prevButton;
    JButton lastButton;
    JButton firstButton;

    JLabel page;

    private Pagination pagination;

    public TypedTablePanel(List<T> dataList, Class<? extends T> typeClass, Pagination pagination) {
        super(new MigLayout());
        createButtons();
        TypedTable<T> table = new TypedTable<>(dataList,typeClass, pagination);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,"wrap");
        this.pagination = pagination;
    }
    public TypedTablePanel(List<T> dataList, Class<? extends T> typeClass) {
        this(dataList,typeClass, null);
    }


    void createButtons() {
        nextButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("next.png")));
        prevButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("back.png")));
        lastButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-forward-button.png")));
        firstButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("fast-backward.png")));
        page = new JLabel("");


        addButton(firstButton,"split 5, al right");
        addButton(prevButton,"");
        add(page);
        addButton(nextButton,"");
        addButton(lastButton,"wrap");

    }

    void addButton(JButton button,String constraints){
        button.setBackground(null);
        button.setOpaque(true);
        button.setBackground(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        add(button,constraints);
    }
}



