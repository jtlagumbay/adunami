package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class NewFileButton extends NavbarButtonTheme {
    private static KeyStroke ctrlNKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);

  public NewFileButton() {
    super(
        GlobalVar.IMAGE_PATH + "add_file_icon.png",
        "New File",
        ctrlNKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("new file");
          }
        },
        "newFileAction"
        );
  }
  //hhhhh
}