package jjprindozo.files;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class MonitorFile {
    private static FileHandler fileHandler = FileHandler.getInstance();
    JButton btn;

    public MonitorFile(JButton btn, JTextArea textArea) {
        this.btn = btn;

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(fileHandler.getSelectedFile() == null && textArea.getText().trim().isEmpty())
                    btn.setEnabled(false);
                else if(fileHandler.getSelectedFile() != null && fileHandler.getFileContent().equals(textArea.getText().trim())) 
                    btn.setEnabled(false);
                else
                    btn.setEnabled(true);
            }
        }, 0, 100);
    }

    public static boolean isModified(JTextArea textArea) {
        if(fileHandler.getSelectedFile() == null && textArea.getText().trim().isEmpty())
            return false;
        else if(fileHandler.getSelectedFile() != null && fileHandler.getFileContent().equals(textArea.getText().trim())) 
            return false;
        else
            return true;
    }

    public static int saveChanges(JTextArea textArea, int type) {
        String options0[] = {"Save", "Don't Save", "Cancel"};
        String options1[] = {"Save", "Don't Save"};

        if(MonitorFile.isModified(textArea) == true) {
            // JFrame frame = new JFrame();
            int option = JOptionPane.showOptionDialog(
                null, 
                "Do you want to save the changes you made?", 
                "Save Changes", 
                type, 
                2, 
                null, 
                type == 0 ? options0 : options1, 
                type == 0 ? options0[0] : options1[0]);

            return option;
        } else return -1;
    }
}
