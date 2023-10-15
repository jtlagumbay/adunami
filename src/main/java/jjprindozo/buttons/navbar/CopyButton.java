package jjprindozo.buttons.navbar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


import jjprindozo.common.GlobalVar;

public class CopyButton extends NavbarButtonTheme{
  private static KeyStroke ctrlCKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + S
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + S
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public CopyButton() {
    super(
        GlobalVar.IMAGE_PATH + "copy_icon.png",
        "Copy",
        ctrlCKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("Copy");
          }
        },
        "copyAction"
      );
  }
  
}
