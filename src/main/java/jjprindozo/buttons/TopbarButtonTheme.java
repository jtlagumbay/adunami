package jjprindozo.buttons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;

import jjprindozo.common.Colors;

public class TopbarButtonTheme extends JButton {

  public TopbarButtonTheme(String iconPath, String tootlTipText) {
    super.setContentAreaFilled(false);

    ImageIcon openIcon = new ImageIcon(iconPath);
    setBackground(Colors.GREEN);
    setBorder(null);
    setFocusPainted(false);
    setPreferredSize(new Dimension(30, 30));
    setIcon(new ImageIcon(openIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    setToolTipText(tootlTipText);
  }
  
    @Override
    protected void paintComponent(Graphics g) {
      if (getModel().isPressed()) {
        g.setColor(Colors.DARKGREENHOVER);
      } else if (getModel().isRollover()) {
        g.setColor(Colors.DARKGREENHOVER);
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

