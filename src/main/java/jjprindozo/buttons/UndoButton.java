package jjprindozo.buttons;

import jjprindozo.common.GlobalVar;
import javax.swing.undo.UndoManager;

public class UndoButton extends NavbarButtonTheme {
  public UndoButton(UndoManager undoManager) {
    super(GlobalVar.IMAGE_PATH+"undo_icon.png", "Undo");

    addActionListener(e -> {
      try {
         undoManager.undo();
       } catch (Exception ex){

       }
   });
  }
}
