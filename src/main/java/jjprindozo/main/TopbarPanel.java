package jjprindozo.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jjprindozo.buttons.topbar.CompileButton;
import jjprindozo.buttons.topbar.RunButton;
import jjprindozo.common.Colors;
import jjprindozo.files.FileChangeListener;
import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;

public class TopbarPanel extends JPanel implements FileChangeListener {
    private static FileHandler fileHandler = FileHandler.getInstance();
    private static JLabel file;

    public TopbarPanel(JTextArea textArea) {
        // layout
        setLayout(new BorderLayout());
        setBackground(Colors.GREEN);
        setBorder(new EmptyBorder(0, 10, 0, 10));

        // custom font: jost
        Font customFont = new Font("sans-serif", Font.BOLD, 14);
        try {
          InputStream inputStream = getClass().getResourceAsStream("/fonts/jost.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(14f);
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

        // listens to unsaved changes DO NOT DELETE!!
        MonitorFile monitorFile = new MonitorFile(null, textArea, file);
        
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
