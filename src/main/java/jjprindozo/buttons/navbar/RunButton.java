package jjprindozo.buttons.navbar;

import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import jjprindozo.buttons.topbar.TopbarButtonTheme;
import jjprindozo.common.GlobalVar;

public class RunButton extends TopbarButtonTheme {
  private static KeyStroke ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);

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
  }
}
