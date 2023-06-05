package org.krzywanski.table.table;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.test.TestModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;
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

    public TypedTablePanel(List<T> dataList, Class<? extends T> typeClass) {
        super(new MigLayout());
        createButtons();
        TypedTable<T> table = new TypedTable<>(dataList,typeClass);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,"wrap");
    }


    void createButtons(){
        nextButton = new RoundButton(">");
        prevButton = new RoundButton("<");
        lastButton = new RoundButton(">>");
        firstButton = new RoundButton("<<");
        page = new JLabel("");


        addButton(firstButton,"split 5, al right");
        addButton(prevButton,"");
        add(page);
        addButton(nextButton,"");
        addButton(lastButton,"wrap");

    }

    void addButton(JButton button,String constraints){
        add(button,constraints);
    }

    public class RoundButton extends JButton {

        public RoundButton(String label) {
            super(label);
            setPreferredSize(new Dimension(5,5));
            setBackground(Color.lightGray);
            setFocusable(false);

            setContentAreaFilled(false);
        }

        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(Color.gray);
            } else {
                g.setColor(getBackground());
            }
            g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(Color.darkGray);
            g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
        }

        // Hit detection.
        Shape shape;

        public boolean contains(int x, int y) {
            // If the button has changed size,  make a new shape object.
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
            }
            return shape.contains(x, y);
        }
    }
}



