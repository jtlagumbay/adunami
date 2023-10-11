package jjprindozo.main;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

import jjprindozo.buttons.navbar.NewFileButton;
import jjprindozo.buttons.navbar.OpenButton;
import jjprindozo.buttons.navbar.RedoButton;
import jjprindozo.buttons.navbar.SaveButton;
import jjprindozo.buttons.topbar.UndoButton;
import jjprindozo.common.Colors;


public class NavbarPanel extends JPanel {    
  // private static FileHandler fileHandler = FileHandler.getInstance();
    public NavbarPanel(JTextArea textArea, UndoManager undoManager) {
      // JPanel layout
      setLayout(new GridLayout(15, 1, 5, 5));
      setBackground(Colors.DARKGRAY);
		
      /** <-------  NEW FUNCTION  -------> **/
      NewFileButton newFile = new NewFileButton();
      add(newFile);
      
      /** <-------  OPEN FUNCTION  -------> **/
      OpenButton open = new OpenButton(textArea);
      add(open);

      /** <-------  SAVE FUNCTION  -------> **/
      SaveButton save = new SaveButton(textArea);
      add(save);
      
      /** <-------  UNDO FUNCTION  -------> **/
      UndoButton undo = new UndoButton(undoManager);
      add(undo);
      
      /** <-------  REDO FUNCTION  -------> **/
      RedoButton redo = new RedoButton(undoManager);
      add(redo);

    }




    }

