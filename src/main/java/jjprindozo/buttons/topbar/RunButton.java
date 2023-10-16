package jjprindozo.buttons.topbar;

import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import jjprindozo.common.GlobalVar;

public class RunButton extends TopbarButtonTheme {
  private static KeyStroke ctrlRKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + R
          ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + R
          ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public RunButton() {
    super(
      GlobalVar.IMAGE_PATH+"run.png", 
      "Run", 
      ctrlRKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("run");
          }
        },
        "runAction"
    );
     // set a tooltip with a black background
     setToolTipText("<html><div style='background-color: black; color: white;'>Run</div></html>");
  }
}
