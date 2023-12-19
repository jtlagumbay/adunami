package jjprindozo.buttons.topbar;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.FileHandler;

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

  public CompileButton(JTextArea textArea) {
    super(
      GlobalVar.IMAGE_PATH+"compile.png",
      "Compile",
      ctrlBKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            CompileLanguage();
            // System.out.println("compile");
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

  private static void CompileLanguage() {
    String parserPath = "C:/Users/princ/Desktop/adunami_language";
    try {
      // Compile command
      String compileCommand = "g++ -o parser.exe parser.cpp";
      String file_name = fileHandler.getSelectedFile().getAbsolutePath();
      file_name = file_name.replace(".adm", "");
      System.out.println("Compiling File: " + file_name + "\n\n");
      File workingDirectory = new File(parserPath);
  
      // Create ProcessBuilder for compilation
      ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd", "/c", compileCommand);
      compileProcessBuilder.directory(workingDirectory);
      compileProcessBuilder.redirectErrorStream(true); // Merge standard output and error streams
  
      // Start compilation process
      Process compileProcess = compileProcessBuilder.start();
  
      // Get compilation output
      BufferedReader compileOutput = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
      String line;
      while ((line = compileOutput.readLine()) != null) {
          System.out.println(line);
      }
  
      // Wait for compilation to complete
      int compileExitCode = compileProcess.waitFor();
      System.out.println("Done Compilation with Exit Code: " + compileExitCode);
  
  } catch (IOException | InterruptedException e) {
      e.printStackTrace();
  }
  }
}
