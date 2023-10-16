package jjprindozo.main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

import jjprindozo.common.Colors;

public class TextEditorTextArea extends JTextArea {
    int line_number = 1;
    public TextEditorTextArea(UndoManager undoManager) {
        setEditable(true);
        setBackground(Colors.LIGHTGRAY);
        setForeground(Colors.WHITE);
        setFont(new Font("monospaced", Font.PLAIN, 16));
        setMargin(new Insets(0, 40, 0, 0));

        // Register the UndoManager with the text area
        getDocument().addUndoableEditListener(undoManager);

        // Set the caret color to white
        setCaretColor(Colors.WHITE);

        // Add a KeyListener to update line numbers when Enter is pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    line_number++;
                }
                if(e.getKeyChar() == '\b') {
                     int caretPosition = getCaretPosition();
                try {
                    Rectangle caretRectangle = modelToView(caretPosition);
                    if (caretRectangle != null) {
                        int horizontalCaretPosition = caretRectangle.x; // Horizontal caret position
                        if(horizontalCaretPosition == 40 && line_number > 1) {
                            line_number--;
                        }
                    }
                } catch(BadLocationException ee) {
                        ee.printStackTrace();
                }
                }
                
                    updateLineNumbers();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLineNumbers(g);
    }

    private void drawLineNumbers(Graphics g) {
        FontMetrics fontMetrics = g.getFontMetrics();
        Insets insets = getInsets();
        int lineHeight = fontMetrics.getHeight();
        int startY = insets.top + fontMetrics.getAscent(); // Adjusted starting position
        
        int lineCount = line_number;

        for (int i = 0; i < lineCount; i++) {
            // Adjusted starting position and width of the line number area
            g.drawString(String.valueOf(i + 1), 5, startY);
            startY += lineHeight;
        }
    }

     private void updateLineNumbers() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint(); // Trigger a repaint to update line numbers
            }
        });
    }
}