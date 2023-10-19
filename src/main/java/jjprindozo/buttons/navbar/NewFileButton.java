package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.FileHandler;
import jjprindozo.files.MonitorFile;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class NewFileButton extends NavbarButtonTheme {
  private static KeyStroke ctrlNKeyStroke;
  private static FileHandler fileHandler = FileHandler.getInstance();
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + N
          ctrlNKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + N
          ctrlNKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public NewFileButton(JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "add_file_icon.png",
        "New File",
        ctrlNKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            switch(MonitorFile.saveChanges(textArea)) {
              case 0:
                SaveButton.saveFile(textArea);
                NewFile(textArea);
                break;
              
              case 2:
                break;
              
              default:
                NewFile(textArea);
                break;
            }
          }
        },
        "newFileAction"
    );

      }
  
  private static void NewFile(JTextArea textArea) {
    textArea.setText("// write your code here\n ");
    fileHandler.setSelectedFile(null);
  }
}