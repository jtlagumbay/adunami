package jjprindozo.buttons.topbar;

import jjprindozo.buttons.navbar.NavbarButtonTheme;
import jjprindozo.common.GlobalVar;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

public class UndoButton extends NavbarButtonTheme {
    private static KeyStroke ctrlZKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);

  public UndoButton(UndoManager undoManager) {
    super(
        GlobalVar.IMAGE_PATH + "undo_icon.png",
        "Undo",
        ctrlZKeyStroke,
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("undo");
          }
        },
        "undoAction"
      );
    addActionListener(e -> {
        try {
           undoManager.undo();
         } catch (Exception ex){

         }
     });
  }
}
