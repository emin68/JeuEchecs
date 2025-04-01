package JeuEchec;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
    
    public void start() {
        
        final JFrame frame = new JFrame("Menu de jeu d'échecs");
        JPanel panel = new JPanel();
        
        JLabel taille = new JLabel("Définir la taille du damier:");
        JSlider slider = new JSlider(0, 150, 75);
        slider.setPaintTrack(true); 
        slider.setPaintTicks(true); 
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(30); 
        slider.setMinorTickSpacing(5);
        
        JButton normalButton = new JButton("Jouer une partie normale");
        normalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Jouer une partie normale ou personnaliser
            	PartieNormale frame2 = null;
            	if(slider.getValue() < 50) {
            		frame2 = new PartieNormale(50);
            	}
            	else frame2 = new PartieNormale(slider.getValue());
                frame2.setTitle("Echec");
                frame2.pack();
                frame2.setResizable(false);
                frame2.setLocationRelativeTo( null );
                frame2.setVisible(true);
                frame.dispose();
            }
        });
        
        JButton variant1Button = new JButton("Jouer une partie de variante 1");
        variant1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Jouer une partie avec la variante 1
            	PartieVariante1 frame2 = null;
            	if(slider.getValue() < 50) {
            		frame2 = new PartieVariante1(50);
            	}
            	else frame2 = new PartieVariante1(slider.getValue());
                frame2.setTitle("Echec variante 1");
                frame2.pack();
                frame2.setResizable(false);
                frame2.setLocationRelativeTo( null );
                frame2.setVisible(true);
                frame.dispose();
            }
        });
        
        JButton variant2Button = new JButton("Jouer une partie de variante 2");
        variant2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Jouer une partie avec la variante 2           	
            	PartieVariante2 frame2 = null;
            	if(slider.getValue() < 50) {
            		frame2 = new PartieVariante2(50);
            	}
            	else frame2 = new PartieVariante2(slider.getValue());
                frame2.setTitle("Echec variante 2");
                frame2.pack();
                frame2.setResizable(false);
                frame2.setLocationRelativeTo(null);
                frame2.setVisible(true);
                frame.dispose();
            }
        });
        
        JButton quitButton = new JButton("Quitter le menu");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        
        panel.add(normalButton);
        panel.add(variant1Button);
        panel.add(variant2Button);
        panel.add(taille);
        panel.add(slider);
        panel.add(quitButton);
        frame.add(panel);
        
        frame.setSize(340, 240);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
}