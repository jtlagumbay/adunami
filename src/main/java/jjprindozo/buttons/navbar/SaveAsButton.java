package jjprindozo.buttons.navbar;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import jjprindozo.common.GlobalVar;
import jjprindozo.files.CustomFileChooser;
import jjprindozo.files.FileHandler;

public class SaveAsButton extends NavbarButtonTheme {
   private static File selectedFile;
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static CustomFileChooser fileChooser = new CustomFileChooser();
  private static KeyStroke ctrlShiftSKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
      // On macOS, use Command + S
      ctrlShiftSKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
    } else {
      // On other platforms, use Ctrl + S
      ctrlShiftSKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
    }
  }
  
  public SaveAsButton(JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "save_as_icon.png",
        "Save as new file",
        ctrlShiftSKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            selectedFile = fileHandler.getSelectedFile();
            saveFileAs(textArea);
          }
        },
        "saveAsAction");
  }
  
  private static void saveFileAs(JTextArea textArea) {
    // if there is a file being edited
    selectedFile = fileHandler.getSelectedFile();

    int result = fileChooser.showSaveDialog(textArea);
  
    if (result == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();
      if (!selectedFile.getName().endsWith(GlobalVar.FILE_EXTENSION)) {
        String newFilePath = selectedFile.getAbsolutePath() + "." + GlobalVar.FILE_EXTENSION;
        File renamedFile = new File(newFilePath);
        selectedFile = renamedFile;
      }

      fileHandler.setSelectedFile(selectedFile);
      String fileName = selectedFile.getAbsolutePath();
      JFrame frame = new JFrame();

      // add .txt extension
      if (!fileName.toLowerCase().endsWith(".txt")) {
        fileName += ".txt";
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

        // Write the text from the JTextArea to the file
        writer.write(textArea.getText());
        writer.flush();
        JOptionPane.showMessageDialog(frame, "File saved successfully.");

      } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "An error occurred while saving the file.");
        e.printStackTrace();
      }
    }
      
	
  }
  
}
