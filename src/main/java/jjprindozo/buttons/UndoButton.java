package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class UndoButton extends NavbarButtonTheme {
    private static KeyStroke ctrlZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);

  public UndoButton() {
    super(
        GlobalVar.IMAGE_PATH + "undo_icon.png",
        "Undo",
        ctrlZKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            
          }
        },
        "undoAction"
      );
  }
}
