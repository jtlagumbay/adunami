package jjprindozo.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.UndoManager;

import jjprindozo.buttons.navbar.SaveButton;
import jjprindozo.common.Fonts;

import com.formdev.flatlaf.FlatDarculaLaf;

import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;

public class IDE {
    // GUI Window
    private static void createWindow() {
    	
        // Initialize
    	UndoManager undoManager = new UndoManager();
    	FileHandler fileHandler = FileHandler.getInstance();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // setup the window
        JFrame frame = new JFrame("adunami IDE"); // navbar && pane

        frame.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));
        ImageIcon img = new ImageIcon("src/main/resources/images/logo.png");
        frame.setIconImage(img.getImage());

        /*<------------ JPANELS ------------>*/
        JPanel pane = new JPanel(new BorderLayout()); // topbar && cont
        JPanel cont = new JPanel(new BorderLayout()); // text && right
        JPanel right = new JPanel(new BorderLayout()); // quotes && games

        /*<------------ TEXT AREA ------------>*/
        TextEditorTextArea code = new TextEditorTextArea(undoManager);

        /*<------------ TERMINAL ------------>*/
        JPanel terminal = new JPanel(new BorderLayout());

        JLabel terminalLabel = new JLabel("Output");
        terminalLabel.setFont(Fonts.getRegular());
        terminalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        terminal.add(terminalLabel, BorderLayout.NORTH);

        TerminalArea term = new TerminalArea();
        terminal.add(term);
        

        JSplitPane text = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(code), terminal); // textarea && terminal
        text.setDividerLocation((int) (11*screenSize.getHeight()/20));
        cont.add(text);

        /*<------------ GAME ------------>*/
        Adunami display = new Adunami();
        right.add(display, BorderLayout.SOUTH);

        frame.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
            @Override
            public Component getDefaultComponent(Container aContainer) {
                return display;  // The component you want to focus on
            }
        });

        /*<------------ QUOTE ------------>*/
        Quotes quote = new Quotes();
        right.add(quote);

        cont.add(right, BorderLayout.EAST);
        pane.add(cont);

        /*<------------ TOPBAR ------------>*/
        TopbarPanel bar = new TopbarPanel(code, term);
        pane.add(bar, BorderLayout.PAGE_START);

        // Register topbarPanel as a listener for file changes
        fileHandler.setFileChangeListener(bar);

        frame.add(pane);

        /*<------------ NAVBAR ------------>*/
        NavbarPanel nav = new NavbarPanel(code, undoManager);
        frame.add(nav, BorderLayout.WEST);

        // display the window
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(frame, code);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
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

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        
        SplashScreen sp = new SplashScreen();
        
        if(!sp.isVisible())
            createWindow();
    }
}
