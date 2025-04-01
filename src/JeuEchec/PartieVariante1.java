package JeuEchec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Classe pour la variante catapulte 
 * Regle:
 *   
 */
class PartieVariante1 extends Damier{ 

	private static final long serialVersionUID = 1L;
	public boolean passant = false;
    public boolean promotion = true;
	
	public PartieVariante1(int taille) {
		
		super(taille);
		
		regleVar1();
		
		Piece r1 = new Roi(true);
        Reine r2 = new Reine(true);
        Pion p8 = new Pion(true);
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
        grid.setPiece(p8,0,1);
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
        Pion p16 = new Pion(false);
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
        grid.setPiece(p15,0,6);
        grid.setPiece(p9,1,6);
        grid.setPiece(p10,2,6);
        grid.setPiece(p11,3,6);
        grid.setPiece(p12,4,6);
        grid.setPiece(p13,5,6);
        grid.setPiece(p14,6,6);
        grid.setPiece(p16,7,6);
        grid.setPiece(r4,3,7);
        grid.setPiece(r3,4,7);
        grid.setPiece(f2,2,7);
        grid.setPiece(f2,5,7);
        grid.setPiece(c2,6,7);
        grid.setPiece(c2,1,7);
        grid.setPiece(t2,0,7);
        grid.setPiece(t2,7,7);
	}
	
	public void regleVar1() {
		
		final JFrame frame = new JFrame("Variante: TotalWar");
	    JPanel panel = new JPanel();
	    
	    JLabel label1 = new JLabel("<html><h1>Regle:</h1></html>");
	    
	    JLabel label2 = new JLabel("<html>Dans cette variante, les joueurs <br>"
	    		+ "peuvent ajouter et jouer différentes<br> types de "
	    		+ "pièces féerique créer par nos soins.<br></html>");
	    
	    JLabel label3 = new JLabel("<html>La catapulte ne peut que se deplacer<br>"
	    		+ "d'une case vers le haut ou vers le bas.<br> Elle attaquee "
	    		+ "à l'horizontale uniquement <br> et permet d'attaquer des pièces <br>"
	    		+ "dérriere d'autres pièces.<br>"
	    		+ "(remplace les pions de chaque cotés)</html>");
	    
	    JLabel label4 = new JLabel("<html>Le jeune fou peut se<br>"
	    		+ "deplacer de un en diagonale.<br> Par contre, contrairement au "
	    		+ "<br>fou, il ne peut que manger de un<br>"
	    		+ "à l'horizontale et à la verticale.<br>"
	    		+ "(remplace les fous)</html>");
	    
	    JLabel label5 = new JLabel("<html>Enfin un pion assez intelligent<br>"
	    		+ "pour aller en avant et en arrière,<br>"
	    		+ " et manger en avant et en arrière.<br>"
	    		+ "(remplace les pions de chaques cotés +1 case)</html>");
	    
	    JLabel label6 = new JLabel("<html>Le paladin possède les mêmes<br>"
	    		+ "déplacements que le roi, mais peut aussi<br>"
	    		+ "manger une pièce deux cases éloignée de lui<br>"
	    		+ "à l'horizontale et à la verticale.<br>"
	    		+ "(remplace les cavaliers)</html>");
	    
	    final JCheckBox checkbox1 = new JCheckBox("Catapulte");
	    final JCheckBox checkbox2 = new JCheckBox("Jeune Fou");
	    final JCheckBox checkbox3 = new JCheckBox("PionCompétant");
	    final JCheckBox checkbox4 = new JCheckBox("Paladin");
	    
	    JButton val = new JButton("Ok");
	    val.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(checkbox1.isSelected()) {
            		Catapulte cat1 = new Catapulte(true);
            		Catapulte cat2 = new Catapulte(true);
            		Catapulte cat3 = new Catapulte(false);
            		Catapulte cat4 = new Catapulte(false);
            		grid.setPiece(cat1,0,1);
            		grid.setPiece(cat2,7,1);
            		grid.setPiece(cat3,7,6);
            		grid.setPiece(cat4,0,6);
            	}
            	if(checkbox2.isSelected()) {
            		JeuneFou cat1 = new JeuneFou(true);
            		JeuneFou cat2 = new JeuneFou(true);
            		JeuneFou cat3 = new JeuneFou(false);
            		JeuneFou cat4 = new JeuneFou(false);
            		grid.setPiece(cat1,2,0);
            		grid.setPiece(cat2,5,0);
            		grid.setPiece(cat3,2,7);
            		grid.setPiece(cat4,5,7);
            	}
            	if(checkbox3.isSelected()) {
            		PionC cat1 = new PionC(true);
            		PionC cat2 = new PionC(true);
            		PionC cat3 = new PionC(false);
            		PionC cat4 = new PionC(false);
            		grid.setPiece(cat1,1,1);
            		grid.setPiece(cat2,6,1);
            		grid.setPiece(cat3,1,6);
            		grid.setPiece(cat4,6,6);
            	}
            	if(checkbox4.isSelected()) {
            		Paladin cat1 = new Paladin(true);
            		Paladin cat2 = new Paladin(true);
            		Paladin cat3 = new Paladin(false);
            		Paladin cat4 = new Paladin(false);
            		grid.setPiece(cat1,1,0);
            		grid.setPiece(cat2,6,0);
            		grid.setPiece(cat3,1,7);
            		grid.setPiece(cat4,6,7);
            	}
            	frame.dispose();
            	//Affiche le jeu
            	drawGrid();
            }	
        });
	    
	    panel.add(label1);
	    panel.add(label2);
	    panel.add(checkbox1);
	    panel.add(label3);
	    panel.add(checkbox2);
	    panel.add(label4);
	    panel.add(checkbox3);
	    panel.add(label5);
	    panel.add(checkbox4);
	    panel.add(label6);
	    panel.add(val);
	    frame.add(panel);
	    
	    frame.setSize(336,650);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
