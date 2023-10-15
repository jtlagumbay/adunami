package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
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

  public UndoButton(UndoManager undoManager) {
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
        "undoAction"
      );
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
