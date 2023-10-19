package jjprindozo.buttons.navbar;

import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class HelpButton extends NavbarButtonTheme {
  private static KeyStroke ctrlSlashKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + Z
          ctrlSlashKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + Z
          ctrlSlashKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public HelpButton() {
    super(
        GlobalVar.IMAGE_PATH + "help_icon.png",
        "Help",
        ctrlSlashKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            showHelp();
          }
        },
        "helpAction"
    );

    }

  private static void showHelp() {
    // Code here
  }
}
