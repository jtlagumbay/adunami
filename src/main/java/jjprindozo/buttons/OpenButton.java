package jjprindozo.buttons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import jjprindozo.common.GlobalVar;
import jjprindozo.main.FileHandler;



public class OpenButton extends NavbarButtonTheme {
  private static File selectedFile;
  private FileHandler fileHandler = FileHandler.getInstance();
  private static JFileChooser fileChooser = new JFileChooser();

  public OpenButton(JTextArea textArea) {
    super(GlobalVar.IMAGE_PATH + "open_file_icon.png", "Open");
    // this.fileHandler = fileHandler;
    addActionListener(e -> {
      openFile(textArea);
    });
  }
  
  public File getSelectedFile() {
    if(selectedFile!=null && selectedFile.exists())
      return selectedFile;
    else return null;
  }
  
  private void openFile(JTextArea textArea) {
		int returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();
      fileHandler.setSelectedFile(selectedFile);
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

