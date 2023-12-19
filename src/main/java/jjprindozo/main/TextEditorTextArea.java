package jjprindozo.main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import jjprindozo.common.Colors;

public class TextEditorTextArea extends JTextArea {
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

        // set TAB key to two spaces only
        KeyStroke tabKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        getInputMap().put(tabKeyStroke, "insertTwoSpaces");

        Action insertTwoSpacesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replaceSelection("  ");
            }
        };

        getActionMap().put("insertTwoSpaces", insertTwoSpacesAction);

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
            g.drawString(String.valueOf(i + 1), 5, startY);
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
