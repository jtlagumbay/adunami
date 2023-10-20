package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
        JFrame helpFrame = new JFrame();
        helpFrame.getContentPane().setBackground(new Color(60, 63, 65));
        ImageIcon img = new ImageIcon("src/main/resources/images/logo.png");
        helpFrame.setIconImage(img.getImage());
        // helpFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        // Disable the frame's ability to gain focus
        helpFrame.setFocusableWindowState(false);
        
        // JTextArea helpText = new JTextArea();
        // helpText.setEditable(false);
        // helpText.setLineWrap(true);
        // helpText.setWrapStyleWord(true);
        //helpText.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        // // Add the help information
        // String helpInfo = "Welcome to adunami IDE!\n\n"
        //                 + "This is the first ever IDE that has snake game on the lower right side to help you relieve your stress while coding and motivational quotes panel on the upper right side to guide and motivate you until you finish coding. Happy coding!\n";
        // helpText.setText(helpInfo);

    // Create a top container for the upper part
    String htmlCode = 
"<html>" +
"<body style='text-align: justify;'>" +
        "<p><b><font color='#FFFFFF'>Welcome to adunami IDE!</font></b></p>" +
        "<p><font color='#FFFFFF'>This is the first ever IDE that has a" +
        "<b style='color:#68FFDC;'> snake game</b> on the lower right side to help you relieve your stress while coding and a" +
        "<b style='color:#68FFDC;'> motivational quotes</b> panel on the upper right side to guide and motivate you until you finish coding. Happy coding!</font></p>" +
    "</body>" +
"</html>";

JEditorPane topTextArea = new JEditorPane();
topTextArea.setContentType("text/html");
HTMLEditorKit htmlEditor = new HTMLEditorKit();
topTextArea.setEditorKit(htmlEditor); // Set HTML editor kit
Document doc = topTextArea.getDocument();
try {
    htmlEditor.insertHTML((HTMLDocument) doc, doc.getLength(), htmlCode, 0, 0, null);
}catch(Exception e){}

topTextArea.setEditable(false);
topTextArea.setOpaque(false); // Make the background transparent
//JScrollPane scrollPane = new JScrollPane(topTextArea);

// Optional: Set a preferred size for the scroll pane
//scrollPane.setPreferredSize(new Dimension(400, 200));


//Create a top container for the upper part
//JTextArea topTextArea = new JTextArea("Welcome to adunami IDE!\n\n");
//topTextArea.setFont(new Font("Jost", Font.BOLD, 14)); // Set font to bold
//topTextArea.setForeground(new Color(255, 255, 255)); // Set color to #FFFFFF
//topTextArea.setEditable(false);
//topTextArea.setLineWrap(true);
//topTextArea.setWrapStyleWord(true);

// Create a top container for the upper part paragraph
//JTextArea topTextArea2 = new JTextArea("This is the first ever IDE that has a ");
//topTextArea2.setForeground(new Color(255, 255, 255)); // Set color to #FFFFFF
//topTextArea2.setEditable(false);
//topTextArea2.setLineWrap(true);
//topTextArea2.setWrapStyleWord(true);

// Create a JPanel for the bottom part, which contains two containers
JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

// Create a left container for the bottom-left part
JPanel leftBottomPanel = new JPanel(new BorderLayout());
JTextArea leftBottomTextArea = new JTextArea("Left Bottom Text Area");
leftBottomTextArea.setEditable(false);
leftBottomPanel.add(leftBottomTextArea, BorderLayout.CENTER);

// Create a right container for the title (left)
leftBottomTextArea.setEditable(false);
JLabel startLabel = new JLabel("Start");
startLabel.setFont(new Font("Jost", Font.BOLD, 14)); // Set font to bold
startLabel.setForeground(new Color(104, 255, 220)); // Set color to #68FFDC
leftBottomPanel.add(startLabel, BorderLayout.NORTH);


// Create a JList for the left side with icons
DefaultListModel<ListItem> leftListModel = new DefaultListModel<>();

leftListModel.addElement(new ListItem("New File", new ImageIcon("src\\main\\resources\\images\\add_file_icon.png")));
leftListModel.addElement(new ListItem("Folder", new ImageIcon("src\\main\\resources\\images\\open_file_icon.png")));
leftListModel.addElement(new ListItem("Save", new ImageIcon("src\\main\\resources\\images\\save_file_icon.png")));
leftListModel.addElement(new ListItem("Save As", new ImageIcon("src\\main\\resources\\images\\save_as_icon.png")));
leftListModel.addElement(new ListItem("Undo", new ImageIcon("src\\main\\resources\\images\\undo_icon.png")));
leftListModel.addElement(new ListItem("Redo", new ImageIcon("src\\main\\resources\\images\\redo_icon.png")));
leftListModel.addElement(new ListItem("Cut", new ImageIcon("src\\main\\resources\\images\\cut_icon.png")));
leftListModel.addElement(new ListItem("Copy", new ImageIcon("src\\main\\resources\\images\\copy_icon.png")));
leftListModel.addElement(new ListItem("Paste", new ImageIcon("src\\main\\resources\\images\\paste_icon.png")));
leftListModel.addElement(new ListItem("Help", new ImageIcon("src\\main\\resources\\images\\help_icon.png")));


JList<ListItem> leftList = new JList<>(leftListModel);
leftList.setCellRenderer(new IconListRenderer());

// Set the preferred size for the JList
leftList.setPreferredSize(new Dimension(200, 150)); // Adjust the size as needed
// Add the JList to the right side
leftBottomPanel.add(leftList, BorderLayout.CENTER);

// Create a right container for the bottom-right part
JPanel rightBottomPanel = new JPanel(new BorderLayout());
JTextArea rightBottomTextArea = new JTextArea("Shortcut Keys");
rightBottomTextArea.setEditable(false);
rightBottomPanel.add(rightBottomTextArea, BorderLayout.CENTER);

// Create a right container for the title (right)
rightBottomTextArea.setEditable(false);
JLabel shortcutKeysLabel = new JLabel("Shortcut Keys");
shortcutKeysLabel.setFont(new Font("Jost", Font.BOLD, 14)); // Set font to bold
shortcutKeysLabel.setForeground(new Color(104, 255, 220)); // Set color to #68FFDC
rightBottomPanel.add(shortcutKeysLabel, BorderLayout.NORTH);

// Create a JList for the right side
DefaultListModel<String> rightListModel = new DefaultListModel<>();
rightListModel.addElement("<html>New File: <b>CTRL + N</b></html>");
rightListModel.addElement("<html>Open: <b>CTRL + O</b></html>");
rightListModel.addElement("<html>Save: <b>CTRL + S</b></html>");
rightListModel.addElement("<html>Undo: <b>CTRL + Z</b></html>");
rightListModel.addElement("<html>Redo: <b>CTRL + SHIFT + Z</b><html>");
rightListModel.addElement("<html>Compile: <b>CTRL + B</b></html>");
rightListModel.addElement("<html>Run: <b>CTRL + R</b></html>");
JList<String> rightList = new JList<>(rightListModel);

// Set the preferred size for the JList
rightList.setPreferredSize(new Dimension(200, 150)); // Adjust the size as needed

// Add the JList to the right side
rightBottomPanel.add(rightList, BorderLayout.CENTER);

// Add components to the bottom part
bottomPanel.add(leftBottomPanel);
bottomPanel.add(rightBottomPanel);

// Add components to the frame
helpFrame.add(topTextArea, BorderLayout.NORTH);
helpFrame.add(bottomPanel, BorderLayout.CENTER);

        // Set the background color for the top text area
topTextArea.setBackground(helpFrame.getBackground());

// Set the background color for the left and right bottom panels
leftBottomPanel.setBackground(helpFrame.getBackground());
rightBottomPanel.setBackground(helpFrame.getBackground());

// Set the background color for the content pane
helpFrame.getContentPane().setBackground(helpFrame.getBackground());
       
        // // Create a JScrollPane for the help text
        // JScrollPane scrollPane = new JScrollPane(helpText);
      
        
        // // Create a custom close button
        // JButton closeButton1 = new JButton("x");
        // closeButton1.addActionListener(e -> helpFrame.dispose());

        // // Add the close button to the top-right corner
        // JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // closePanel.add(closeButton1);
        // helpFrame.add(closePanel, BorderLayout.NORTH);

        // Create a close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                helpFrame.dispose();
            }
        });
        closeButton.setPreferredSize(new Dimension(100, 60));
        // Add components to the JFrame
        // helpFrame.add(scrollPane, BorderLayout.CENTER);
        helpFrame.add(closeButton, BorderLayout.SOUTH);
        
        // Set JFrame properties
        helpFrame.setSize(400, 480);
        helpFrame.setLocationRelativeTo(null); // Center the frame on screen
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setVisible(true);
    }

    static class IconListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ListItem item = (ListItem) value;
            label.setIcon(item.getIcon());
            label.setText(item.getText());
            return label;
        }
    }

    static class ListItem {
        private final String text;
        private final ImageIcon icon;

        public ListItem(String text, ImageIcon icon) {
            this.text = text;

            // Resize the icon to the desired dimensions
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(resizedImg);
        }

        public String getText() {
            return text;
        }

        public ImageIcon getIcon() {
            return icon;
        }
    }
}




    