package jjprindozo.buttons.navbar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


import jjprindozo.common.GlobalVar;

public class CutButton extends NavbarButtonTheme{
  private static KeyStroke ctrlXKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + S
          ctrlXKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + S
          ctrlXKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public CutButton() {
    super(
        GlobalVar.IMAGE_PATH + "cut_icon.png",
        "Cut",
        ctrlXKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("Cut");
          }
        },
        "cutAction"
      );
  }
  
}
