package jjprindozo.main;

import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalArea extends JPanel {
    private JTextArea outputTextArea;
    private JTextField inputTextField;
    private Process cmdProcess;

    public TerminalArea() {
        setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(Color.BLACK);
        outputTextArea.setForeground(Color.WHITE);

        inputTextField = new JTextField();
        inputTextField.addActionListener(e -> {
            executeCommand(inputTextField.getText());
            inputTextField.setText("");
        });

        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        add(scrollPane, BorderLayout.CENTER);
        add(inputTextField, BorderLayout.SOUTH);

        startCmdProcess();
    }

    private void startCmdProcess() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd");
            processBuilder.redirectErrorStream(true);
            cmdProcess = processBuilder.start();

            // Read the output of CMD in a separate thread
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        appendToOutput(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Close the process when the window is closed
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (cmdProcess != null) {
                    cmdProcess.destroy();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String command) {
        try {
            if(command == "DONE") {
                cmdProcess.getOutputStream().write(("^C" + "\n").getBytes());
                cmdProcess.getOutputStream().flush();
            } else {
                cmdProcess.getOutputStream().write((command + "\n").getBytes());
                cmdProcess.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToOutput(String text) {
        SwingUtilities.invokeLater(() -> outputTextArea.append(text));
    }
}


