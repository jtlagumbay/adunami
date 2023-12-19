package jjprindozo.buttons.topbar;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import jjprindozo.common.GlobalVar;
import jjprindozo.files.FileHandler;

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

  public RunButton(JTextArea textArea) {
    super(
      GlobalVar.IMAGE_PATH+"run.png", 
      "Run", 
      ctrlRKeyStroke,
      new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // clearTerminal();
            RunLanguage();
            // System.out.println("run");
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

  private static void RunLanguage() {
    String parserPath = GlobalVar.LANGUAGE_PATH;
    try {
      // Compile command
      String compileCommand = "g++ -o parser.exe parser.cpp";
      String file_name = fileHandler.getSelectedFile().getAbsolutePath();
      file_name = file_name.replace(".adm", "");
      System.out.println("Running File: " + file_name + "\n\n");
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
      System.out.println("Compilation Exit Code: " + compileExitCode);
  
      if (compileExitCode == 0) {
          // Execution command with input redirection
          String executionCommand = "echo " + file_name + " | parser.exe";
  
          // Create ProcessBuilder for execution
          ProcessBuilder executionProcessBuilder = new ProcessBuilder("cmd", "/c", executionCommand);
          executionProcessBuilder.directory(workingDirectory);
          executionProcessBuilder.redirectErrorStream(true); // Merge standard output and error streams
  
          // Start execution process
          Process executionProcess = executionProcessBuilder.start();
  
          // Get execution output
          BufferedReader executionOutput = new BufferedReader(new InputStreamReader(executionProcess.getInputStream()));
          while ((line = executionOutput.readLine()) != null) {
              System.out.println(line);
          }
  
          // Wait for execution to complete
          int executionExitCode = executionProcess.waitFor();
          System.out.println("Done Execution with Exit Code: " + executionExitCode);
      }
  
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
  }
}
