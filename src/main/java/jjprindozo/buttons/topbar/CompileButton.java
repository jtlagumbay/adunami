package jjprindozo.buttons.topbar;

import java.awt.event.*;

import javax.swing.*;

import jjprindozo.common.GlobalVar;

public class CompileButton extends TopbarButtonTheme {
  private static KeyStroke ctrlBKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + B
          ctrlBKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + B
          ctrlBKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public CompileButton() {
    super(
      GlobalVar.IMAGE_PATH+"compile.png",
      "Compile",
      ctrlBKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("compile");
          }
        },
        "runAction"
    );
  
  }
}
