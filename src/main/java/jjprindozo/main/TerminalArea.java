package jjprindozo.main;

import javax.swing.*;

import jjprindozo.common.Colors;

import java.awt.*;

public class TerminalArea extends JTextArea {
    public TerminalArea() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setPreferredSize(new Dimension((int) (screenSize.getWidth()/4), HEIGHT));
        setBackground(Colors.BLACK);
        setForeground(Colors.WHITE);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        setEditable(true);  // Allow user input
        setMargin(new Insets(0, 40, 0, 0));

        updateLineNumbers();
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
        int startY = insets.top + fontMetrics.getAscent();

        int lineCount = getLineCount();

        for (int i = 0; i < lineCount; i++) {
            g.drawString(">", 5, startY);
            startY += lineHeight;
        }
    }

    private void updateLineNumbers() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }
}


