package jjprindozo.main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.Timer;

public class TypeWriterEffect {
    private Timer timer;
    private int characterIndex = 0;
    @SuppressWarnings("all")
    private String input;
    private JTextArea textArea;

    public TypeWriterEffect(JTextArea textArea) {
        this.textArea = textArea; 
        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (characterIndex < input.length()) {
                    textArea.append(Character.toString(input.charAt(characterIndex)));
                    System.out.print(characterIndex + " ");
                    characterIndex++;
                } else {
                    stop();
                }
            }
        });
    }

    public void setText(String input){
        this.input = input;
    }

    public void start() {
        textArea.setText(null);
        characterIndex = 0;
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

}