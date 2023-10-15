package jjprindozo.buttons.navbar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.CustomFileChooser;
import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;



public class OpenButton extends NavbarButtonTheme {
  private static File selectedFile;
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static JFileChooser fileChooser = new CustomFileChooser();
  private static KeyStroke ctrlOKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);

  public OpenButton(JTextArea textArea) {
    super(
      GlobalVar.IMAGE_PATH + "open_file_icon.png", 
      "Open", 
      ctrlOKeyStroke,
      new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          switch(MonitorFile.saveChanges(textArea, 0)) {
            case 0:
              SaveButton.saveFile(textArea);
              openFile(textArea);
              break;
            
            case 1:
              openFile(textArea);
              break;
            
            case 2:
              break;
            
            default:
              openFile(textArea);
              break;
          }
        }
      },
      "openAction"
    );
    
    
    
    // addActionListener(saveAction);
    // getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlSKeyStroke, "saveAction");
    // getActionMap().put("saveAction", saveAction);

  }
  
  public File getSelectedFile() {
    if(selectedFile!=null && selectedFile.exists())
      return selectedFile;
    else return null;
  }
  
  private static void openFile(JTextArea textArea) {
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

