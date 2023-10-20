package jjprindozo.buttons.navbar;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import jjprindozo.common.GlobalVar;

public class CopyButton extends NavbarButtonTheme{
  private static KeyStroke ctrlCKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + C
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + C
          ctrlCKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public CopyButton(JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "copy_icon.png",
        "Copy",
        ctrlCKeyStroke,
        new DefaultEditorKit.CopyAction(),
        "copyAction"
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
