package jjprindozo.main;

import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;

public class IDE {
    // GUI Window
    private static void createWindow() {
        // Initialize FileHandler and TopbarPanel
        FileHandler fileHandler = FileHandler.getInstance();
        TopbarPanel topbarPanel = new TopbarPanel();

        // Register topbarPanel as a listener for file changes
        fileHandler.setFileChangeListener(topbarPanel);

        // setup the window
        JFrame frame = new JFrame("adunami IDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set to fullscreen
        frame.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));

        // change icon
        ImageIcon img = new ImageIcon("src/assets/images/logo.png");
        frame.setIconImage(img.getImage());

        // panel that houses textArea and topbar
        JPanel pane = new JPanel(new BorderLayout());

        // topbar
        TopbarPanel bar = new TopbarPanel();
        pane.add(bar, BorderLayout.PAGE_START);

        // text area
        TextEditorTextArea code = new TextEditorTextArea();

        // scroller for text area
        JScrollPane scroll = new JScrollPane(code);        
        pane.add(scroll);

        frame.add(pane);

        // navbar
        NavbarPanel nav = new NavbarPanel(code);
        frame.add(nav, BorderLayout.WEST);

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
}
