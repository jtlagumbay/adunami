package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

public class RedoButton extends NavbarButtonTheme {
  private static KeyStroke ctrlShiftZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

  public RedoButton(UndoManager undoManager) {
    super(
        GlobalVar.IMAGE_PATH + "redo_icon.png",
        "Redo",
        ctrlShiftZKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("redo");
          }
        },
        "redoAction"
      );
    addActionListener(e -> {
        try {
          if (undoManager.canRedo()) {
            undoManager.redo(); // Perform redo action
          }
         } catch (Exception ex){
          ex.printStackTrace(); // Handle any exceptions
         }
     });
  }
}
