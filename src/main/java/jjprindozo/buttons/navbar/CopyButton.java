package jjprindozo.buttons.navbar;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import jjprindozo.common.GlobalVar;

public class CopyButton extends NavbarButtonTheme{
  private static KeyStroke ctrlCKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + C
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + C
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public CopyButton() {
    super(
        GlobalVar.IMAGE_PATH + "copy_icon.png",
        "Copy",
        ctrlCKeyStroke,
        new DefaultEditorKit.CopyAction(),
        "copyAction"
      );

     // set a tooltip with a black background
     setToolTipText("<html><div style='background-color: black; color: white;'>Copy</div></html>");
  }
  
}
