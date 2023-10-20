package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

public class UndoButton extends NavbarButtonTheme {
  private static KeyStroke ctrlZKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
      // On macOS, use Command + Z
      ctrlZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.META_DOWN_MASK);
    } else {
      // On other platforms, use Ctrl + Z
      ctrlZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
    }
  }

  public UndoButton(UndoManager undoManager, JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "undo_icon.png",
        "Undo",
        ctrlZKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            undoText(undoManager);
          }
        },
        "undoAction");
    setEnabled(false);

 
      textArea.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
          if (undoManager.canUndo()) {
            setEnabled(true);
          }
          else{
            setEnabled(false);
          }
            
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
                if (undoManager.canUndo()) {
            setEnabled(true);
          }
          else{
            setEnabled(false);
          }
        }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (undoManager.canUndo()) {
              setEnabled(true);
            }
            else{
              setEnabled(false);
            }
            }
        });
  }
  
  private static void undoText(UndoManager undoManager) {
    try {
      if(undoManager.canUndo()) {
        undoManager.undo();     //Perform undo action
      }
    } catch (Exception ex){
      ex.printStackTrace(); // Handle any exceptions
    }
  }
}
