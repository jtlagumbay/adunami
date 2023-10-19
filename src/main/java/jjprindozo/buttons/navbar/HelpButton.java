package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

public class HelpButton extends NavbarButtonTheme {
    private static KeyStroke ctrlSlashKeyStroke;
    static {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            // On macOS, use Command + Z
            ctrlSlashKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.META_DOWN_MASK);
        } else {
            // On other platforms, use Ctrl + Z
            ctrlSlashKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK);
        }
    }

    public HelpButton() {
        super(
            GlobalVar.IMAGE_PATH + "help_icon.png",
            "Help",
            ctrlSlashKeyStroke,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showHelp();
                }
            },
            "helpAction"
        );

        // set a tooltip with a black background
        setToolTipText("<html><div style='background-color: black; color: white;'>Help</div></html>");
    }

    private static void showHelp() {
        // Create a JFrame to display the help information
        JFrame helpFrame = new JFrame("IDE Help");
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        
        // Add the help information
        String helpInfo = "Welcome to the IDE Help Section!\n\n"
                        + "- Button 1: Description of button 1 functionality.\n"
                        + "- Button 2: Description of button 2 functionality.\n"
                        + "- ...\n";
        helpText.setText(helpInfo);
        
        // Create a JScrollPane for the help text
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Create a close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                helpFrame.dispose();
            }
        });
        
        // Add components to the JFrame
        helpFrame.add(scrollPane, BorderLayout.CENTER);
        helpFrame.add(closeButton, BorderLayout.SOUTH);
        
        // Set JFrame properties
        helpFrame.setSize(400, 300);
        helpFrame.setLocationRelativeTo(null); // Center the frame on screen
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setVisible(true);
    }
}
