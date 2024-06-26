package jjprindozo.buttons.topbar;

import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.FileHandler;
import jjprindozo.main.TerminalArea;

public class CompileButton extends TopbarButtonTheme {
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static KeyStroke ctrlBKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + B
          ctrlBKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + B
          ctrlBKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public CompileButton(JTextArea textArea, TerminalArea Terminal) {
    super(
      GlobalVar.IMAGE_PATH+"compile.png",
      "Compile",
      ctrlBKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String file_name = fileHandler.getSelectedFile().getAbsolutePath();
            file_name = file_name.replace(".adm", "");
            
            Terminal.executeCommand("cd " + GlobalVar.LANGUAGE_PATH);
            Terminal.executeCommand("g++ parser.cpp -o parser");
          }
        },
        "runAction"
    );

    Timer timer = new Timer(true);
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            if(textArea.getText().trim().isEmpty())
              setEnabled(false);
            else
              setEnabled(true);
        }
    }, 0, 100);
  }
}