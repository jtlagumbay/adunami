package jjprindozo.buttons.navbar;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import jjprindozo.common.GlobalVar;

public class PasteButton extends NavbarButtonTheme{
  private static KeyStroke ctrlVKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + V
          ctrlVKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + V
          ctrlVKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public PasteButton() {
    super(
        GlobalVar.IMAGE_PATH + "paste_icon.png",
        "Paste",
        ctrlVKeyStroke,
        new DefaultEditorKit.PasteAction(),
        "pasteAction"
      );

  
    }
  
}
