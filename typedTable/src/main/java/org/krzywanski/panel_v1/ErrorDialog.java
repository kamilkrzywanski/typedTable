package org.krzywanski.panel_v1;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog {
    public static void showErrorDialog(Component c, String error_message,
                                       Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(error_message);
        pw.print("Exception is: ");
        t.printStackTrace(pw);
        pw.flush();

        JTextArea ta = new JTextArea(sw.toString(), 10, 60);
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);

        JOptionPane.showMessageDialog(c, sp, error_message,
                JOptionPane.ERROR_MESSAGE);
    }
}
