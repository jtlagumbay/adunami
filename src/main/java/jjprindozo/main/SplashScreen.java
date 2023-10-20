package jjprindozo.main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.*;

public class SplashScreen extends JFrame {
    public SplashScreen() {
        initComponents();
        loadSplash();
    }
    
    public SplashScreen(Object object, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
    private void initComponents() {
        
        backgroundPanel = new javax.swing.JPanel();
        bar = new javax.swing.JProgressBar();
        barStatus = new javax.swing.JLabel();
        loadingValue = new javax.swing.JLabel();
        animation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(700, 500));

        backgroundPanel.setBackground(new java.awt.Color(7, 65, 98));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bar.setForeground(new java.awt.Color(0, 204, 0));
        backgroundPanel.add(bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 380, 320, 10));

        barStatus.setFont(new java.awt.Font("OCR A Extended", 0, 12));
        barStatus.setForeground(new java.awt.Color(255, 255, 255));
        barStatus.setText("adunami IDE is starting...");
        backgroundPanel.add(barStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, -1, -1));

        loadingValue.setFont(new java.awt.Font("OCR A Extended", 0, 12));
        loadingValue.setForeground(new java.awt.Color(255, 255, 255));
        loadingValue.setText("0%");
        backgroundPanel.add(loadingValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, -1, -1));

        animation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        animation.setIcon(new ImageIcon(splashGif.getScaledInstance(700, 500, Image.SCALE_REPLICATE)));
        animation.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        backgroundPanel.add(animation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 490));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void loadSplash() {
        // layout for frame
        setVisible(true);
        ImageIcon logo = new ImageIcon("src/main/resources/images/logo.png");
        setIconImage(logo.getImage());
        
        try{
            for(int i = 0; i <= 100; i++){
                Thread.sleep(30);
                loadingValue.setText(i + "%");
                
                switch (i) {
                    case 0:
                        barStatus.setText("adunami IDE is starting...");
                        break;
                    case 20:
                        barStatus.setText("initializing, please wait...");
                        break;
                    case 80:
                        barStatus.setText("getting things ready for you...");
                        break;
                    case 90:
                        barStatus.setText("all set, ready to roll");
                        break;
                    default:
                        break;
                }
                bar.setValue(i);
                
                
            }
        } catch(InterruptedException e){
            JOptionPane.showMessageDialog(null, e);
        }
        setVisible(false);
    }

    private javax.swing.JLabel animation;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JProgressBar bar;
    private javax.swing.JLabel barStatus;
    private javax.swing.JLabel loadingValue;
    private Image splashGif = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/splash_screen_animation.gif");

}