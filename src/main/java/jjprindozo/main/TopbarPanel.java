package jjprindozo.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import jjprindozo.buttons.CompileButton;
import jjprindozo.buttons.RunButton;
import jjprindozo.common.Colors;

public class TopbarPanel extends JPanel {
    public TopbarPanel() {
        // layout
        setLayout(new BorderLayout());
        setBackground(Colors.GREEN);
        setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel file = new JLabel("SampleFile.txt");
        file.setFont(new Font("sans-serif", Font.BOLD, 14));
        file.setForeground(Colors.WHITE);
        
        add(file, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttons.setBackground(Colors.GREEN);

        /** <-------  COMPILE  -------> **/
        CompileButton compile = new CompileButton();
        buttons.add(compile);

        /** <-------  RUN  -------> **/
        RunButton run = new RunButton();
        buttons.add(run);

        add(buttons, BorderLayout.EAST);
    }
}
