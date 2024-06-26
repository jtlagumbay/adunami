package jjprindozo.main;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

import jjprindozo.buttons.navbar.CopyButton;
import jjprindozo.buttons.navbar.CutButton;
import jjprindozo.buttons.navbar.HelpButton;
import jjprindozo.buttons.navbar.NewFileButton;
import jjprindozo.buttons.navbar.OpenButton;
import jjprindozo.buttons.navbar.PasteButton;
import jjprindozo.buttons.navbar.RedoButton;
import jjprindozo.buttons.navbar.SaveAsButton;
import jjprindozo.buttons.navbar.SaveButton;
import jjprindozo.buttons.navbar.UndoButton;
import jjprindozo.common.Colors;

public class NavbarPanel extends JPanel {    
    public NavbarPanel(JTextArea textArea, UndoManager undoManager) {
      // JPanel layout
      setLayout(new GridLayout(15, 1, 5, 5));
      setBackground(Colors.DARKGRAY);

      /** <-------  NEW FUNCTION  -------> **/
      NewFileButton newFile = new NewFileButton(textArea);
      add(newFile);
      
      /** <-------  OPEN FUNCTION  -------> **/
      OpenButton open = new OpenButton(textArea, undoManager);
      add(open);

      /** <-------  SAVE FUNCTION  -------> **/
      SaveButton save = new SaveButton(textArea);
      add(save);

      /** <-------  SAVE AS NEW FILE FUNCTION  -------> **/
      SaveAsButton saveAs = new SaveAsButton(textArea);
      add(saveAs);

      /** <-------  UNDO FUNCTION  -------> **/
      UndoButton undo = new UndoButton(undoManager, textArea);
      add(undo);
      
      /** <-------  REDO FUNCTION  -------> **/
      RedoButton redo = new RedoButton(undoManager, textArea);
      add(redo);
      
      /** <-------  CUT FUNCTION  -------> **/
      CutButton cut = new CutButton(textArea);
      add(cut);
      
      /** <-------  COPY FUNCTION  -------> **/
      CopyButton copy = new CopyButton(textArea);
      add(copy);

      /** <-------  PASTE FUNCTION  -------> **/
      PasteButton paste = new PasteButton();
      add(paste);

      /** <-------  PASTE FUNCTION  -------> **/
      HelpButton help = new HelpButton();
      add(help);

    }




    }

