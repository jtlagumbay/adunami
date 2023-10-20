package jjprindozo.buttons.navbar;

import jjprindozo.common.Colors;
import jjprindozo.common.Fonts;
import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class HelpButton extends NavbarButtonTheme {
    private static KeyStroke ctrlSlashKeyStroke;
    private static boolean isOpen = false;
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
                    if(isOpen == false) {
                        isOpen = true;
                        showHelp();
                    }
                }
            },
            "helpAction"
        );

        // set a tooltip with a black background
        setToolTipText("<html><div style='background-color: black; color: white;'>Help</div></html>");
    }

    private static void showHelp() {
        JFrame helpFrame = new JFrame();
        helpFrame.setBackground(Colors.GRAY);
        ImageIcon img = new ImageIcon("src/main/resources/images/logo.png");
        helpFrame.setIconImage(img.getImage());
        
        helpFrame.setFocusableWindowState(true);
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
        topTextArea.setFont(Fonts.getRegular());
        topTextArea.setContentType("text/html");
        topTextArea.setMargin(new Insets(10, 30, 10, 30));
        HTMLEditorKit htmlEditor = new HTMLEditorKit();
        topTextArea.setEditorKit(htmlEditor); // Set HTML editor kit
        Document doc = topTextArea.getDocument();
        try {
            htmlEditor.insertHTML((HTMLDocument) doc, doc.getLength(), htmlCode, 0, 0, null);
        }catch(Exception e){}

        topTextArea.setEditable(false);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setBackground(Colors.GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));

        JPanel leftBottomPanel = new JPanel(new BorderLayout());
        leftBottomPanel.setBackground(Colors.GRAY);

        JLabel startLabel = new JLabel("Start");
        startLabel.setFont(Fonts.getBold()); // Set font to bold
        startLabel.setForeground(new Color(104, 255, 220));
        leftBottomPanel.add(startLabel, BorderLayout.NORTH);

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
        leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftList.setBackground(Colors.GRAY);

        leftList.setPreferredSize(new Dimension(200, 150));

        leftBottomPanel.add(leftList, BorderLayout.CENTER);

        JPanel rightBottomPanel = new JPanel(new BorderLayout());
        rightBottomPanel.setBackground(Colors.GRAY);

        JLabel shortcutKeysLabel = new JLabel("Shortcut Keys");
        shortcutKeysLabel.setFont(Fonts.getBold());
        shortcutKeysLabel.setForeground(new Color(104, 255, 220));
        rightBottomPanel.add(shortcutKeysLabel, BorderLayout.NORTH);

        DefaultListModel<String> rightListModel = new DefaultListModel<>();
        rightListModel.addElement("<html>New File: <b>CTRL + N</b></html>");
        rightListModel.addElement("<html>Open: <b>CTRL + O</b></html>");
        rightListModel.addElement("<html>Save: <b>CTRL + S</b></html>");
        rightListModel.addElement("<html>Undo: <b>CTRL + Z</b></html>");
        rightListModel.addElement("<html>Redo: <b>CTRL + SHIFT + Z</b><html>");
        rightListModel.addElement("<html>Compile: <b>CTRL + B</b></html>");
        rightListModel.addElement("<html>Run: <b>CTRL + R</b></html>");
        
        JList<String> rightList = new JList<>(rightListModel);
        rightList.setPreferredSize(new Dimension(200, 150));
        rightList.setBackground(Colors.GRAY);

        rightBottomPanel.add(rightList, BorderLayout.CENTER);

        bottomPanel.add(leftBottomPanel);
        bottomPanel.add(rightBottomPanel);

        helpFrame.add(topTextArea, BorderLayout.NORTH);
        helpFrame.add(bottomPanel, BorderLayout.CENTER);
        
        helpFrame.setSize(400, 480);
        helpFrame.setLocationRelativeTo(null); // Center the frame on screen
        helpFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(helpFrame);
            }
        });
        helpFrame.setVisible(true);
    }

    static class IconListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ListItem item = (ListItem) value;
            label.setIcon(item.getIcon());
            label.setText(item.getText());
            return label;
        }
    }

    private static void closeWindow(JFrame frame) {
        if(isOpen == true) {
            isOpen = false;
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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




    