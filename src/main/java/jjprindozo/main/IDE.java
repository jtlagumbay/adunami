package jjprindozo.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.formdev.flatlaf.FlatDarculaLaf;

public class IDE {
    // GUI Window
    private static void createWindow() {
        // setup the window
        JFrame frame = new JFrame("adunami IDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set to fullscreen
        frame.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));

        // change icon
        ImageIcon img = new ImageIcon("src/assets/images/logo.png");
        frame.setIconImage(img.getImage());
        
        // text area
        TextEditorTextArea code = new TextEditorTextArea();
        frame.add(code, BorderLayout.EAST);

        // navbar
        NavbarPanel nav = new NavbarPanel(code);
        frame.add(nav, BorderLayout.WEST);

        // scroller for text area
        JScrollPane scroll = new JScrollPane(code);
        frame.add(scroll);

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
