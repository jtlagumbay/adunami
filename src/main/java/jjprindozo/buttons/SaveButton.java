package jjprindozo.buttons;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import jjprindozo.common.GlobalVar;
import jjprindozo.main.CustomFileChooser;
import jjprindozo.main.FileHandler;

public class SaveButton extends NavbarButtonTheme {
  private static File selectedFile;
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static JFileChooser fileChooser = new CustomFileChooser();
  private static KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);

  public SaveButton(JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "save_file_icon.png",
        "Save",
        ctrlSKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            selectedFile = fileHandler.getSelectedFile(); 
            saveFile(textArea);
          }
        },
        "saveAction"
      );
   
  }
  private static void saveFile(JTextArea textArea) {
    // if there is a file being edited
    selectedFile = fileHandler.getSelectedFile();

    if (selectedFile != null) {
			JFrame frame = new JFrame();

			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(selectedFile.getAbsolutePath()))){
				try {
					writer.write(textArea.getText());
					JOptionPane.showMessageDialog(frame, "File saved successfully.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(frame, "An error occurred while saving the file.");
				e2.printStackTrace();
			}
		
		// if no file, make new one
      } else {
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

}

