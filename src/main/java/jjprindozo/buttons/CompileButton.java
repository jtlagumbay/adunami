package jjprindozo.buttons;

import java.awt.event.*;

import javax.swing.*;

import jjprindozo.common.GlobalVar;

public class CompileButton extends TopbarButtonTheme {
  private static KeyStroke ctrlBKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);

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
