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
import javax.swing.undo.UndoManager;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.CustomFileChooser;
import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;



public class OpenButton extends NavbarButtonTheme {
  private static File selectedFile;
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static JFileChooser fileChooser = new CustomFileChooser();
  private static KeyStroke ctrlOKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + O
          ctrlOKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + O
          ctrlOKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public OpenButton(JTextArea textArea, UndoManager undoManager) {
    super(
      GlobalVar.IMAGE_PATH + "open_file_icon.png", 
      "Open", 
      ctrlOKeyStroke,
      new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          switch(MonitorFile.saveChanges(textArea)) {
            case 0:
              SaveButton.saveFile(textArea);
              openFile(textArea, undoManager); // pass the UndoManager
              break;
            
            case 2:
              break;
            
            default:
              openFile(textArea, undoManager); // pass the UndoManager
              break;
          }
        }
      },
      "openAction"
    );
 
  }
  
  public File getSelectedFile() {
    if(selectedFile!=null && selectedFile.exists())
      return selectedFile;
    else return null;
  }
  
  private static void openFile(JTextArea textArea, UndoManager undoManager) {
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


        //Reset the UndoManager for the opened file
        undoManager.discardAllEdits();   //Clear the undo history
        
        // Set the initial text to the text area
        textArea.getDocument().addUndoableEditListener(undoManager);
        

			} catch (IOException e1) {
				e1.printStackTrace();

			}
		}
	}
}

