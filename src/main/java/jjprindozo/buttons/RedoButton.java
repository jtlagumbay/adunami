package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class RedoButton extends NavbarButtonTheme {
  private static KeyStroke ctrlShiftZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

  public RedoButton() {
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
  }
}
