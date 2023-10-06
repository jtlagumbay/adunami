package jjprindozo.buttons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import jjprindozo.common.GlobalVar;

public class SaveButton extends NavbarButtonTheme {
  private static File selectedFile;
  private static JFileChooser fileChooser = new JFileChooser();

  public SaveButton(JTextArea textArea,  OpenButton open) {
    super(GlobalVar.IMAGE_PATH+"save_file_icon.png", "Save");
    addActionListener(e -> {
      selectedFile = open.getSelectedFile();
      saveFile(textArea);
    });
  }
  private void saveFile(JTextArea textArea) {
		// if there is a file being edited
		if(selectedFile != null) {
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

			if(result == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
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

