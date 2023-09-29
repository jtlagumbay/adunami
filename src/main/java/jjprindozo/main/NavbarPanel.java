package jjprindozo.main;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

import jjprindozo.buttons.NewFileButton;
import jjprindozo.buttons.OpenButton;
import jjprindozo.buttons.RedoButton;
import jjprindozo.buttons.SaveButton;
import jjprindozo.buttons.UndoButton;
import jjprindozo.common.Colors;

public class NavbarPanel extends JPanel {
	
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

