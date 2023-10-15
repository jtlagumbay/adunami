package jjprindozo.files;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class MonitorFile {
    private static FileHandler fileHandler = FileHandler.getInstance();
    JButton btn;
    public static boolean hasChange;

    public MonitorFile(JButton btn, JTextArea textArea) {
        this.btn = btn;

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(fileHandler.getSelectedFile() == null && textArea.getText().trim().isEmpty()) {
                    btn.setEnabled(false);
                } else if(fileHandler.getSelectedFile() != null && fileHandler.getFileContent().equals(textArea.getText().trim())) {
                    btn.setEnabled(false);
                } else {
                    btn.setEnabled(true);
                }
            }
        }, 0, 100);
    }

    public MonitorFile(JButton btn, JTextArea textArea, JLabel file) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(fileHandler.getSelectedFile() == null && textArea.getText().trim().isEmpty()) {
                    hasChange = false;
                } else if(fileHandler.getSelectedFile() != null && fileHandler.getFileContent().equals(textArea.getText().trim())) {
                    hasChange = false;
                } else {
                    hasChange = true;
                }

                updateLabel(file);
            }
        }, 0, 100);

    }

    private static void updateLabel(JLabel file) {
        String fileName = fileHandler.getSelectedFile() != null ? fileHandler.getSelectedFile().getName() : "Untitled Text";
        file.setText(fileName + (hasChange == true ? "*" : ""));
    }

    public static boolean isModified(JTextArea textArea) {
        if(fileHandler.getSelectedFile() == null && textArea.getText().trim().isEmpty())
            return false;
        else if(fileHandler.getSelectedFile() != null && fileHandler.getFileContent().equals(textArea.getText().trim())) 
            return false;
        else
            return true;
    }

    public static int saveChanges(JTextArea textArea) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } 

        String options[] = {"Save", "Don't Save", "Cancel"};

        if(MonitorFile.isModified(textArea) == true) {
            // JFrame frame = new JFrame();
            int option = JOptionPane.showOptionDialog(
                null, 
                "Do you want to save the changes you made?", 
                "Save Changes", 
                0, 
                2, 
                null, 
                options, 
                options[0]);

            return option;
        } else return -1;
    }
}
