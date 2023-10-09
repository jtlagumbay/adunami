package jjprindozo.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import jjprindozo.buttons.CompileButton;
import jjprindozo.buttons.RunButton;
import jjprindozo.common.Colors;

public class TopbarPanel extends JPanel implements FileChangeListener {
    private static FileHandler fileHandler = FileHandler.getInstance();
    private static JLabel file;

    public TopbarPanel() {
        // layout
        setLayout(new BorderLayout());
        setBackground(Colors.GREEN);
        setBorder(new EmptyBorder(0, 10, 0, 10));

        // custom font: jost
        Font customFont = new Font("sans-serif", Font.BOLD, 14);
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/assets/fonts/jost.ttf")).deriveFont(14f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        // file name
        File fileName = fileHandler.getSelectedFile(); 

        file = new JLabel(fileName != null ? fileName.getName() : "Untitled Text");
        file.setFont(customFont);
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

    @Override
    public void onFileChange(String fileName) {
        file.setText(fileName);
    }
}
