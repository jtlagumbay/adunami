package jjprindozo.buttons.navbar;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import jjprindozo.common.GlobalVar;

public class CutButton extends NavbarButtonTheme{
  private static KeyStroke ctrlXKeyStroke;
  static {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
          // On macOS, use Command + X
          ctrlXKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_DOWN_MASK);
      } else {
          // On other platforms, use Ctrl + X
          ctrlXKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
      }
  }
  public CutButton(JTextArea textArea) {
    super(
        GlobalVar.IMAGE_PATH + "cut_icon.png",
        "Cut",
        ctrlXKeyStroke,
        new DefaultEditorKit.CutAction(),
        "cutAction"
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
