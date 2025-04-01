package JeuEchec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Classe separer pour les parties normales
class PartieNormale extends Damier{

	private static final long serialVersionUID = 1L;
	
	public PartieNormale(int taille) {	
		
		super(taille);	
		
		ParamPartie();
		
		Piece r1 = new Roi(true);
        Reine r2 = new Reine(true);
        Pion p0 = new Pion(true);
        Pion p1 = new Pion(true);
        Pion p2 = new Pion(true);
        Pion p3 = new Pion(true);
        Pion p4 = new Pion(true);
        Pion p5 = new Pion(true);
        Pion p6 = new Pion(true);
        Pion p7 = new Pion(true);
        Fou f1 = new Fou(true);
        Cavalier c1 = new Cavalier(true);
        Tour t1 = new Tour(true);
        grid.setPiece(p0,0,1);
        grid.setPiece(p1,1,1);
        grid.setPiece(p2,2,1);
        grid.setPiece(p3,3,1);
        grid.setPiece(p4,4,1);
        grid.setPiece(p5,5,1);
        grid.setPiece(p6,6,1);
        grid.setPiece(p7,7,1);
        grid.setPiece(r2,3,0);
        grid.setPiece(r1,4,0);
        grid.setPiece(f1,2,0);
        grid.setPiece(f1,5,0);
        grid.setPiece(c1,6,0);
        grid.setPiece(c1,1,0);
        grid.setPiece(t1,0,0);
        grid.setPiece(t1,7,0);
        
        Piece r3 = new Roi(false);
        Reine r4 = new Reine(false);
        Pion p8 = new Pion(false);
        Pion p9 = new Pion(false);
        Pion p10 = new Pion(false);
        Pion p11 = new Pion(false);
        Pion p12 = new Pion(false);
        Pion p13 = new Pion(false);
        Pion p14 = new Pion(false);
        Pion p15 = new Pion(false);
        Fou f2 = new Fou(false);
        Cavalier c2 = new Cavalier(false);
        Tour t2 = new Tour(false);
        grid.setPiece(p8,0,6);
        grid.setPiece(p9,1,6);
        grid.setPiece(p10,2,6);
        grid.setPiece(p11,3,6);
        grid.setPiece(p12,4,6);
        grid.setPiece(p13,5,6);
        grid.setPiece(p14,6,6);
        grid.setPiece(p15,7,6);
        grid.setPiece(r4,3,7);
        grid.setPiece(r3,4,7);
        grid.setPiece(f2,2,7);
        grid.setPiece(f2,5,7);
        grid.setPiece(c2,6,7);
        grid.setPiece(c2,1,7);
        grid.setPiece(t2,0,7);
        grid.setPiece(t2,7,7);
	}
	
	public void ParamPartie() {
		
		final JFrame frame = new JFrame("Parametre pour partie standard");
	    JPanel panel = new JPanel();
	    
	    final JCheckBox checkbox1 = new JCheckBox("Promotion");
	    final JCheckBox checkbox2 = new JCheckBox("Passant");

	    JButton val = new JButton("Ok");
	    val.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                promotion = checkbox1.isSelected();
                passant = checkbox2.isSelected();
            	frame.dispose();
            	//Affiche le jeu
            	drawGrid();
            }	
        });
	    
	    panel.add(checkbox1);
	    panel.add(checkbox2);
	    panel.add(val);
	    frame.add(panel);
	    
	    frame.setSize(120, 150);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
	}
}
