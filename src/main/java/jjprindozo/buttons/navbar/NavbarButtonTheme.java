package jjprindozo.buttons.navbar;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;

import jjprindozo.common.Colors;

public class NavbarButtonTheme extends JButton {

  public NavbarButtonTheme(
    String iconPath,
    String tootlTipText,
    KeyStroke buttonKeyStroke,
    Action buttonAction,
    String actionObject
  ) {
    super.setContentAreaFilled(false);
    // Actions
    addActionListener(buttonAction);
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(buttonKeyStroke, actionObject);
    getActionMap().put(actionObject, buttonAction);

    // UI
    URL logoOneUrl = getClass().getResource(iconPath);
    if (logoOneUrl != null) {
      ImageIcon openIcon = new ImageIcon(logoOneUrl);
      setIcon(new ImageIcon(openIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    }
    
    setBackground(Colors.DARKGRAY);
    setBorder(null);
    setFocusPainted(false);
    setPreferredSize(new Dimension(50, 50));
    setToolTipText("<html><div style='background-color: black; color: white;'>"+tootlTipText+"</div></html>");
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
  
    @Override
    protected void paintComponent(Graphics g) {
      if (getModel().isPressed()) {
        g.setColor(Colors.DARKGRAYHOVER);
      } else if (getModel().isRollover()) {
        g.setColor(Colors.DARKGRAYHOVER);
      } else {
        g.setColor(getBackground());
      }
      g.fillRect(0, 0, getWidth(), getHeight());
      super.paintComponent(g);
    }
    
   @Override
    public JToolTip createToolTip() {
        return (new NavbarToolTip(this));
    }
    
  class NavbarToolTip extends JToolTip {
    public NavbarToolTip(JComponent component) {
        super();
        setComponent(component);
        setBackground(Colors.DARKGRAYHOVER);
        setForeground(Colors.WHITE);
        setBorder(null);
    }
}
}

