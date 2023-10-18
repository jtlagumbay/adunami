package jjprindozo.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.UndoManager;

import jjprindozo.buttons.navbar.SaveButton;

import com.formdev.flatlaf.FlatDarculaLaf;

import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;

public class IDE {
    // GUI Window
    private static void createWindow() {
    	
        //Create UndoManager for text area
    	UndoManager undoManager = new UndoManager();

        // setup the window
        JFrame frame = new JFrame("adunami IDE");

        // set to fullscreen
        frame.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));

        // change icon
        ImageIcon img = new ImageIcon("src/main/resources/images/logo.png");
        frame.setIconImage(img.getImage());

        // panel that houses textArea and topbar
        JPanel pane = new JPanel(new BorderLayout());

        // text area
        TextEditorTextArea code = new TextEditorTextArea(undoManager);

        // topbar
        TopbarPanel bar = new TopbarPanel(code);
        pane.add(bar, BorderLayout.PAGE_START);
    	
        // Initialize FileHandler
        FileHandler fileHandler = FileHandler.getInstance();

        // Register topbarPanel as a listener for file changes
        fileHandler.setFileChangeListener(bar);

        // scroller for text area
        JScrollPane scroll = new JScrollPane(code);        
        pane.add(scroll);

        frame.add(pane);

        // navbar
        NavbarPanel nav = new NavbarPanel(code, undoManager);
        frame.add(nav, BorderLayout.WEST);

        // custom close function
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(frame, code);
            }
        });

        // display the window
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
      FlatDarculaLaf.setup();

		// call window
        createWindow();
    }

    private static void closeWindow(JFrame frame, JTextArea textArea) {
        switch(MonitorFile.saveChanges(textArea)) {
            case 0:
                SaveButton.saveFile(textArea);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                break;
            
            case 2:
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                break;
            
            default:
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                break;
        }
    }
}
