package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;
import javax.swing.undo.UndoManager;

public class RedoButton extends NavbarButtonTheme {
  public RedoButton(UndoManager undoManager) {
    super(GlobalVar.IMAGE_PATH+"redo_icon.png", "Redo");

    addActionListener(e -> {
      try {
         undoManager.redo();
       } catch (Exception ex){

       }
   });
  }
}
