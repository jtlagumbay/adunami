package jjprindozo.buttons.navbar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


import jjprindozo.common.GlobalVar;

public class PasteButton extends NavbarButtonTheme{
  private static KeyStroke ctrlVKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + S
          ctrlVKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + S
          ctrlVKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public PasteButton() {
    super(
        GlobalVar.IMAGE_PATH + "paste_icon.png",
        "Paste",
        ctrlVKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("Paste");
          }
        },
        "pasteAction"
      );
  }
  
}
