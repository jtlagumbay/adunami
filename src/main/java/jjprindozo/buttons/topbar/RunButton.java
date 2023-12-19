package jjprindozo.buttons.topbar;

import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.FileHandler;
import jjprindozo.main.TerminalArea;

public class RunButton extends TopbarButtonTheme {
  private static FileHandler fileHandler = FileHandler.getInstance();
  private static KeyStroke ctrlRKeyStroke;

  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + R
          ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + R
          ctrlRKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
      }
  }

  public RunButton(JTextArea textArea, TerminalArea Terminal) {
    super(
      GlobalVar.IMAGE_PATH+"run.png", 
      "Run", 
      ctrlRKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String file_name = fileHandler.getSelectedFile().getAbsolutePath();
            file_name = file_name.replace(".adm", "");
            
            Terminal.executeCommand("cd " + GlobalVar.LANGUAGE_PATH);
            Terminal.executeCommand("g++ parser.cpp -o parser");
            Terminal.executeCommand("parser.exe");
            Terminal.executeCommand(file_name);
            Terminal.executeCommand("cd " + GlobalVar.IDE_PATH);
            Terminal.executeCommand("DONE");
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