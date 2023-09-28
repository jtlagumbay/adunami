package jjprindozo.buttons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import jjprindozo.common.GlobalVar;

public class OpenButton extends NavbarButtonTheme {
  private static File selectedFile;
  private static JFileChooser fileChooser = new JFileChooser();

  public OpenButton(JTextArea textArea) {
    super(GlobalVar.IMAGE_PATH+"open_file_icon.png", "Open");
    addActionListener(e -> {
			openFile(textArea);
		});
  }
  
  private void openFile(JTextArea textArea) {
		int returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();

			try {
				File file = new File(selectedFile.getAbsolutePath());

				if(file.exists()) {
					FileReader reader = new FileReader(selectedFile.getAbsolutePath());
					BufferedReader br = new BufferedReader(reader);
					textArea.read(br, null);
					br.close();
					textArea.requestFocus();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

