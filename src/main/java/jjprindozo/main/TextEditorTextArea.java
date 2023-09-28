package jjprindozo.main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextArea;

import jjprindozo.common.Colors;

public class TextEditorTextArea extends JTextArea {
    public TextEditorTextArea() {
        setEditable(true);
        setBackground(Colors.LIGHTGRAY);
        setForeground(Colors.WHITE);
        setFont(new Font("monospaced", Font.PLAIN, 16));
        setMargin(new Insets(0, 40, 0, 0));
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
    
        String[] lines = getText().split("\n");
        int lineCount = lines.length;
    
        for (int i = 0; i < lineCount; i++) {
            // Adjusted starting position and width of the line number area
            g.drawString(String.valueOf(i + 1), 5, startY);
            startY += lineHeight;
        }
    }
    
}
