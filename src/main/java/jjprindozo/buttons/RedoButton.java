package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class RedoButton extends NavbarButtonTheme {
     private static KeyStroke ctrlYKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
  public RedoButton() {
    super(
        GlobalVar.IMAGE_PATH + "redo_icon.png",
        "Redo",
        ctrlYKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
          }
        },
        "redoAction"
      );
  }
}
