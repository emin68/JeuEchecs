package JeuEchec;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Faire menace

/** Fenetre graphique (classe heritant de JFrame) qui reagit au clic de souris (implements MouseListener)
 *   qui affiche le damier, qui permet de selectionner/deplacer des pieces d'echec
 */

abstract class Damier extends JFrame implements MouseListener {
	
    private static final long serialVersionUID = 1L;
    
    /** Panneau principal */
    JPanel mainPanel;
    /** Panneau correspondant au damier */
    JPanel chessBoard;
    	
    protected static int SIZE_CASE_X;
    protected static int SIZE_CASE_Y;
    protected Grid grid;
    
    //Pour savoir a qui c'est le tour
    protected boolean tourNoir = true;
    
    //Les parametres pour une partie (promotion et passant);
    protected boolean passant = true;
    //Parametre pour dire si on joue la promotion
    protected boolean promotion = true;
    
    //Couple pour la position temporaire de la piece qui va etre deplacer 
    protected Couple posTemp;
    
    //Couple pour determiné la position d'une menace sur le roi
    protected Couple posMenace;
    
    //Timer pour savoir quand faut-t-il lancer une fenêtre pour proposer l'abandon
    protected Timer time;
    
    /////
    //Parametre pour dire qu'on joue la variante
    protected boolean variante2 = false;
    //Nombre de tour pour la variante 2
    protected int tourn = 0;

    static final Color BLACK_CASE = Color.gray;
    static final Color WHITE_CASE = Color.white;
    static final Color HIGHLIGHT_CASE = Color.orange;
    
    protected boolean RoiEchec = false; //pour tester l'echec

    public Damier(int taille) {
    	
    	//Timer laissant le temps au joueur de jouer 1min
    	time = new Timer(60*1000,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Abandon(); // Méthode à exécuter à la fin du timer
            }
    	});
    	
    	//Pour faire la taille
    	SIZE_CASE_X = taille;
    	SIZE_CASE_Y = taille;
    	
        // Taille d'un element graphique
        Dimension boardSize = new Dimension(SIZE_CASE_X * Grid.SIZE_LINE_GRID, SIZE_CASE_Y * Grid.SIZE_ROW_GRID);
        
        //Initialisation de la variable pour enregistrer les deplacements
        this.posTemp = new Couple(0,0);
        this.posMenace = new Couple(0,0);

        // Definition du panel principal
        mainPanel = new JPanel();
        getContentPane().add(mainPanel); // Ajout du panel principal a la fenetre principale (JFrame)
        mainPanel.setPreferredSize(boardSize);
        mainPanel.addMouseListener(this); // Ajout de la gestion des clics

        // Definition du panel contenant le damier
        chessBoard = new JPanel();
        mainPanel.add(chessBoard); // Ajout du panel damier au panel principal
        chessBoard.setLayout( new GridLayout(Grid.SIZE_LINE_GRID, Grid.SIZE_ROW_GRID) ); // le damier sera une grille N * M
        chessBoard.setPreferredSize( boardSize );
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        // Dans le damier, ajout de N*M panneau, chacun correspondant a une case de la grille
        for (int i = 0; i < Grid.SIZE_LINE_GRID *Grid.SIZE_ROW_GRID; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setBorder(BorderFactory.createLineBorder(Color.black));
            chessBoard.add(square);
        }
        // Instantciation de l'echiquier
        grid = new Grid();     
        
        /*
         * Simple proposition d'égalité.
         * Fait dans le constructeur pour éviter les
         * problèmes de rechargement de la fenêtre.
         */
        final JFrame frame = new JFrame("égalité ?");
	    JPanel panel = new JPanel();
	    
	    JLabel lab = new JLabel("Voulez-vous proposer une égalité ?");
	    
		JButton val = new JButton("Oui");
	    val.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {             	
            	
            	final JFrame frame2 = new JFrame("Parametre pour partie standard");
        	    JPanel panel2 = new JPanel();
        	    
        	    JLabel lab2 = new JLabel("Veux-tu accépté l'égalité ?");
        	    
        	    JButton val3 = new JButton("Oui");
        	    val3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	
	                	final JFrame frame3 = new JFrame("égalité des joueurs !");
	                	JPanel panel3 = new JPanel();
	                	
	                	JLabel lab3 = new JLabel("égalité accepté !");
	                	
	             	   	panel3.add(lab3);	             	   	 
	             	   	frame3.add(panel3);
               	    
	                 	frame3.setSize(250,100);
	                    frame3.setVisible(true);
	                    frame3.setLocationRelativeTo(null);
	                    frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    
	                    frame2.dispose();
	                    frame.dispose();
	                    mainPanel.setVisible(false);
	                    chessBoard.setVisible(false); 
                    }
        	    });
        	    
        	    JButton val4 = new JButton("Non");
        	    val4.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	frame2.dispose();
                    }
        	    });
        	    
        	    panel2.add(lab2);
        	    panel2.add(val3);
        	    panel2.add(val4);
        	    frame2.add(panel2);
        	    
        	    frame2.setSize(250,100);
                frame2.setVisible(true);
                frame2.setLocationRelativeTo(null);
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       	            	       	
            }	
        });	    
	    JButton val2 = new JButton("Non");
	    val2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	frame.dispose();
            }	
        });
	    
	    panel.add(lab);
	    panel.add(val);
	    panel.add(val2);
	    frame.add(panel);
	    
	    frame.setSize(260,120);
        frame.setVisible(true);
        frame.setLocationRelativeTo(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
      //Choses qui vont se passer quand on va fermer la fenêtre 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                time.stop(); // Arrêter le Timer lorsque la fenêtre se ferme
                dispose(); // Fermer la fenêtre
                frame.dispose();
            }
        });
    }

    
    /** Methode d'affichage de la grille
     *
     */
    public void drawGrid() {
    	
    	/*
    	 * Condition lors du rechargement du damier pour
    	 * savoir si le roi est en echec et pour savoir
    	 * si il est en échec et mat.
    	 */
    	if(RoiEchec) EchecetMat();
    	
        JPanel square;
        // Pour chaque case de la grille
        for (int i = 0; i < Grid.SIZE_LINE_GRID *Grid.SIZE_ROW_GRID; i++) {
            square = (JPanel)chessBoard.getComponent(i);

            // Dessin case noire/ case blanche
            int row = (i / Grid.SIZE_LINE_GRID) % 2;
            if (row == 0)
                square.setBackground( i % 2 == 0 ? WHITE_CASE : BLACK_CASE );
            else
                square.setBackground( i % 2 == 0 ? BLACK_CASE : WHITE_CASE );

            // Dessin d'une piece (image chargee dans un jlabel, ajoutee au jpanel)
            if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) != null) {
            	ImageIcon image = null; 
            	JLabel piece = null;
            	 if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Pion) {
                 	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                 		image = new ImageIcon(getClass().getClassLoader().getResource("pngwing2.com.png"));
                 		piece = new JLabel(image);
                 	}
                 	else {
                 		image = new ImageIcon(getClass().getClassLoader().getResource("pngwing.com.png"));
                 		piece = new JLabel(image);
                 	}
                 }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Roi) {
                	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                       	image = new ImageIcon(getClass().getClassLoader().getResource("pngk2.com.png"));
                       	piece = new JLabel(image);
                	}
                	else {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngk.com.png"));
                       	piece = new JLabel(image);
                	}
                }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Reine) {
                	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngq2.com.png"));
                       	piece = new JLabel(image);
                	}
                	else {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngq.com.png"));
                       	piece = new JLabel(image);
                	}
                }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Fou) {
                	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngfou2.com.png"));
                       	piece = new JLabel(image);
                	}
                	else {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngfou.com.png"));
                       	piece = new JLabel(image);
                	}
                }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Cavalier) {
                	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngcav2.com.png"));
                       	piece = new JLabel(image);
                	}
                	else {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngcav.com.png"));
                       	piece = new JLabel(image);
                	}
                }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Tour) {
                	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngtour2.com.png"));
                       	piece = new JLabel(image);
                	}
                	else {
                		image = new ImageIcon(getClass().getClassLoader().getResource("pngtour.com.png"));
                       	piece = new JLabel(image);
                	}
                }
                //Piece pour la premiere variante
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Catapulte) {
                 	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                 		piece = new JLabel("CAT Noir");
                 	}
                 	else {
                 		piece = new JLabel("CAT Blanc");
                 		piece.setBackground(Color.white);
                 	}
                }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof JeuneFou) {
                 	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                 		piece = new JLabel("JF Noir");
                 	}
                 	else {
                 		piece = new JLabel("JF Blanc");
                 		piece.setBackground(Color.white);
                 	}
                 }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof PionC) {
                 	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                 		piece = new JLabel("PC Noir");
                 	}
                 	else {
                 		piece = new JLabel("PC Blanc");
                 		piece.setBackground(Color.white);
                 	}
                 }
                if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID) instanceof Paladin) {
                 	if(grid.getPiece(i%Grid.SIZE_LINE_GRID, i/Grid.SIZE_LINE_GRID).Couleur()) {
                 		piece = new JLabel("PAL Noir");
                 	}
                 	else {
                 		piece = new JLabel("PAL Blanc");
                 		piece.setBackground(Color.white);
                 	}
                 }
                square.add(piece);
            }

            // Dessin de la case mise en valeur (changement de la couleur du fond du jpanel)
            if(grid.highLightCase[i%Grid.SIZE_LINE_GRID][i/Grid.SIZE_LINE_GRID] ) { square.setBackground(HIGHLIGHT_CASE); }
        }                   

        // Forcer la mise Ã  jour de l'affichage
        revalidate();
        repaint();
    }

    
    
  //Methode pour la promotion
    void promotion(int c,int l) {
    	
    	//On creer une frame et un jpanel pour donner les choix
    	JFrame frame = new JFrame("Promotion du pion");
        JPanel panel = new JPanel();
        
        //Si on choisi une reine
        JButton dame = new JButton("Dame");
        dame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(grid.getPiece(c,l).Couleur()) {
        			Reine f = new Reine(true);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		else {
        			Reine f = new Reine(false);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		//Efface la fenetre
        		frame.dispose();
        	}
    	});
        
        //Si on choisit un fou 
        JButton fou = new JButton("Fou");
        fou.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(grid.getPiece(c,l).Couleur()) {
        			Fou f = new Fou(true);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		else {
        			Fou f = new Fou(false);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		//Efface la fenetre
        		frame.dispose();
        	}
    	});
        
        JButton cavalier = new JButton("Cavalier");
        cavalier.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(grid.getPiece(c,l).Couleur()) {
        			Cavalier f = new Cavalier(true);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		else {
        			Cavalier f = new Cavalier(false);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		//Efface la fenetre
        		frame.dispose();
        	}
    	});
        
        JButton tour = new JButton("Tour");
        tour.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(grid.getPiece(c,l).Couleur()) {
        			Tour f = new Tour(true);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		else {
        			Tour f = new Tour(false);
            		grid.removePiece(c,l);
            		grid.setPiece(f,c,l);
        		}
        		//Efface la fenetre
        		frame.dispose();
        	}
    	});
        
        panel.add(dame);
        panel.add(fou);
        panel.add(cavalier);
        panel.add(tour);
        frame.add(panel);
        
        frame.setSize(100, 200);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    
    
    //Methode pour action et deplacement des pieces
    public void mousePressed(MouseEvent e){
    	
    	//On démarre le chrono pour jouer
    	time.start();
    	
        // Conversion de la position cliquee en position de la grille
        int c = e.getX() / (Damier.SIZE_CASE_X);
        int l = e.getY() / (Damier.SIZE_CASE_Y);
        
        //Juste pour afficher la position des pions qu'on va jouer
        char []colABC = {'A','B','C','D','E','F','G','H'};
        
        /*
         *  Si la position cliquee correspond a une piece, affichage des deplacements.
         *  Le boolean tourNoir permet de savoir a qui c'est le tour de jouer
         *  , donc true si c'est au tour des noirs, false pour le tour des blancs.
         */
        if(grid.getPiece(c,l) != null && grid.getPiece(c,l).Couleur() == tourNoir) {        	
        	//Recuperassions de la position de la piece temporairement
        	posTemp.changeVar(c, l);
        	
        	//Pour avoir la position precise sur la console
        	char str = colABC[posTemp.x];
        	System.out.print(str);
        	System.out.println(posTemp.y);
        	
        	if(grid.getPiece(c,l) instanceof Pion) { //Deplacement du pion
        		grid.resetHighlight();
    			if(grid.getPiece(c,l).Couleur()) {
    				if(l < 7) {
    					if(grid.getPiece(c,l+1) == null  && !RoiEchec) grid.highLightCase[c][l+1] = true;
    					if(l == 1 && grid.getPiece(c,l+2) == null && grid.getPiece(c,l+1) == null && !RoiEchec) grid.highLightCase[c][l+2] = true;
    				}
    				if(l < 7 && c < 7) {
    					if(grid.getPiece(c+1,l+1) != null && grid.getPiece(c,l).Couleur() != grid.getPiece(c+1, l+1).Couleur() && (!RoiEchec || (c+1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c+1][l+1] = true;
    					if(grid.getPiece(c+1,l) != null && grid.getPiece(c+1,l) instanceof Pion && grid.getPiece(c+1,l).Couleur() != grid.getPiece(c,l).Couleur()  && grid.getPiece(c+1,l).deplacement2 && passant) {
    						grid.highLightCase[c+1][l+1] = true;
    					}
    				}
    				if(l < 7 && c > 0) {
    					if(grid.getPiece(c-1,l+1) != null && grid.getPiece(c,l).Couleur() != grid.getPiece(c-1, l+1).Couleur() && (!RoiEchec || (c-1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c-1][l+1] = true;
    					if(grid.getPiece(c-1,l) != null && grid.getPiece(c-1,l) instanceof Pion && grid.getPiece(c-1,l).Couleur() != grid.getPiece(c,l).Couleur()  && grid.getPiece(c-1,l).deplacement2 && passant) {
    						grid.highLightCase[c-1][l+1] = true;
    					}
    				}
    			}
    			else {
    				if(l > 0) {
    					if(grid.getPiece(c,l-1) == null && !RoiEchec) grid.highLightCase[c][l-1] = true;
    					if(l == 6 && grid.getPiece(c,l-2) == null && grid.getPiece(c,l-1) == null && !RoiEchec) grid.highLightCase[c][l-2] = true;
    				}
    				if(l > 0 && c < 7) {
    					if(grid.getPiece(c+1,l-1) != null && (grid.getPiece(c, l).Couleur() != grid.getPiece(c+1, l-1).Couleur()) && (!RoiEchec || (c+1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c+1][l-1] = true;
    					if(grid.getPiece(c+1,l) != null && grid.getPiece(c+1,l) instanceof Pion && grid.getPiece(c+1,l).Couleur() != grid.getPiece(c,l).Couleur()  && grid.getPiece(c+1,l).deplacement2 && passant) {
    						grid.highLightCase[c+1][l-1] = true;
    					}
    				}
    				if(l > 0 && c > 0) {
    					if(grid.getPiece(c-1,l-1) != null && (grid.getPiece(c, l).Couleur() != grid.getPiece(c-1, l-1).Couleur()) && (!RoiEchec || (c-1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c-1][l-1] = true;
    					if(grid.getPiece(c-1,l) != null && grid.getPiece(c-1,l) instanceof Pion && grid.getPiece(c-1,l).Couleur() != grid.getPiece(c,l).Couleur() && grid.getPiece(c-1,l).deplacement2 && passant) {
    						grid.highLightCase[c-1][l-1] = true;
    					}
    				}
    			}        			
        	}
        	
        	if(grid.getPiece(c,l) instanceof Roi) { //Deplacement du roi
        		grid.resetHighlight();
        		if(l < 7) {
        			if((grid.getPiece(c,l+1) == null || grid.getPiece(c,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c,l+1)) grid.highLightCase[c][l+1] = true;
        		}
        		if(l > 0) {
        			if((grid.getPiece(c,l-1) == null || grid.getPiece(c,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c,l-1)) grid.highLightCase[c][l-1] = true;
        		}
        		if(c < 7) {
        			if((grid.getPiece(c+1,l) == null || grid.getPiece(c+1,l).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c+1,l)) grid.highLightCase[c+1][l] = true;
        		}
        		if(c > 0) {
        			if((grid.getPiece(c-1,l) == null || grid.getPiece(c-1,l).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c-1,l)) grid.highLightCase[c-1][l] = true;
        		}
        		if(c+1 <= 7 && l+1 <= 7){
        			if((grid.getPiece(c+1,l+1) == null || grid.getPiece(c+1,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c+1,l+1)) grid.highLightCase[c+1][l+1] = true;
        		}        		
    			if(c-1 >= 0 && l-1 >= 0) {
    				if((grid.getPiece(c-1,l-1) == null || grid.getPiece(c-1,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c-1,l-1)) grid.highLightCase[c-1][l-1] = true;
    			}        		
        		if(c+1 <= 7 && l-1 >= 0) { 
        			if((grid.getPiece(c+1,l-1) == null || grid.getPiece(c+1,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c+1,l-1)) grid.highLightCase[c+1][l-1] = true;
        		}        		 
    			if(c-1 >= 0 && l+1 <= 7) {
    				if((grid.getPiece(c-1,l+1) == null || grid.getPiece(c-1,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && !deplacement_nonpossible(c-1,l+1)) grid.highLightCase[c-1][l+1] = true;
    			}			
    		}    	
        	
        	if(grid.getPiece(c,l) instanceof Reine) { //Deplacement de la renne
        		grid.resetHighlight();
        		
    			//On fait des verifications pour que sa soit dans la grille
        		for(int i = 1; i < 8;i++) {
        			if(c+i <= 7 && l+i <= 7){
        				if(grid.getPiece(c+i,l+i) == null && !RoiEchec) grid.highLightCase[c+i][l+i] = true;
        				if(grid.getPiece(c+i,l+i) != null && (grid.getPiece(c+i,l+i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+i==posMenace.x && l+i==posMenace.y))){
        					grid.highLightCase[c+i][l+i] = true;
        					break;
        				}
        				if(grid.getPiece(c+i,l+i) != null && grid.getPiece(c+i,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;
        			}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(c-i >= 0 && l-i >= 0) {
        				if(grid.getPiece(c-i,l-i) == null && !RoiEchec) grid.highLightCase[c-i][l-i] = true;
        				if(grid.getPiece(c-i,l-i) != null && (grid.getPiece(c-i,l-i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-i==posMenace.x && l-i==posMenace.y))) {
        					grid.highLightCase[c-i][l-i] = true;
        					break;
        				}
        				if(grid.getPiece(c-i,l-i) != null && grid.getPiece(c-i,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;
        			}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(c+i <= 7 && l-i >= 0) { 
        				if(grid.getPiece(c+i,l-i) == null && !RoiEchec) grid.highLightCase[c+i][l-i] = true;
        				if(grid.getPiece(c+i,l-i) != null && (grid.getPiece(c+i,l-i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+i==posMenace.x && l-i==posMenace.y))) {
        					grid.highLightCase[c+i][l-i] = true;
        					break;
        				}
        				if(grid.getPiece(c+i,l-i) != null && grid.getPiece(c+i,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;
        			}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(c-i >= 0 && l+i <= 7) {
        				if(grid.getPiece(c-i,l+i) == null && !RoiEchec) grid.highLightCase[c-i][l+i] = true;
        				if(grid.getPiece(c-i,l+i) != null && (grid.getPiece(c-i,l+i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-i==posMenace.x && l+i==posMenace.y))) {
        					grid.highLightCase[c-i][l+i] = true;
        					break;
        				}
        				if(grid.getPiece(c-i,l+i) != null && grid.getPiece(c-i,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;
        			}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(l+i <= 7) {
        				if(grid.getPiece(c,l+i) == null && !RoiEchec)grid.highLightCase[c][l+i] = true;
	        			if(grid.getPiece(c,l+i) != null && (grid.getPiece(c,l+i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l+i==posMenace.y))) {
	        				grid.highLightCase[c][l+i] = true;
	        				break;
	        			}
	        			if(grid.getPiece(c,l+i) != null &&grid.getPiece(c,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;
	        		}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(c+i <= 7) {
        				if(grid.getPiece(c+i,l) == null && !RoiEchec) grid.highLightCase[c+i][l] = true;
	        			if(grid.getPiece(c+i,l) != null && (grid.getPiece(c+i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+i==posMenace.x && l==posMenace.y))) {
	        				grid.highLightCase[c+i][l] = true;
	        				break;
	        			}
	        			if(grid.getPiece(c+i,l) != null && grid.getPiece(c+i,l).Couleur() == grid.getPiece(c,l).Couleur()) break;
	        		}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(l-i >= 0) {
        				if(grid.getPiece(c,l-i) == null && !RoiEchec)grid.highLightCase[c][l-i] = true;
	        			if(grid.getPiece(c,l-i) != null && (grid.getPiece(c,l-i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l-i==posMenace.y))) {
	        				grid.highLightCase[c][l-i] = true;
	        				break;
	        			}
	        			if(grid.getPiece(c,l-i) != null && grid.getPiece(c,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;
	        		}
        		}
        		
        		for(int i = 1; i < 8;i++) {
        			if(c-i >= 0) {
        				if(grid.getPiece(c-i,l) == null && !RoiEchec)grid.highLightCase[c-i][l] = true;
	        			if(grid.getPiece(c-i,l) != null && (grid.getPiece(c-i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-i==posMenace.x && l==posMenace.y))) {
	        				grid.highLightCase[c-i][l] = true;
	        				break;
	        			}
	        			if(grid.getPiece(c-i,l) != null && grid.getPiece(c-i,l).Couleur() == grid.getPiece(c,l).Couleur()) break;
	        		}
        		}        		
        	}
        	
        	if(grid.getPiece(c,l) instanceof Cavalier) { //Deplacement du cavalier
        		grid.resetHighlight();        		
    			if(c+1 <= 7 && l+2 <= 7) {
        			if((grid.getPiece(c+1,l+2) == null || grid.getPiece(c+1,l+2).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+1==posMenace.x && l+2==posMenace.y))) {
            			grid.highLightCase[c+1][l+2] = true;
            		}
        		}	
        		if(c-1 >= 0 && l+2 <= 7 ) {
        			if((grid.getPiece(c-1,l+2) == null || grid.getPiece(c-1,l+2).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-1==posMenace.x && l+2==posMenace.y))) {
            			grid.highLightCase[c-1][l+2] = true;
            		}
        		}
        		if(c+2 <= 7 && l+1 <= 7 ) {
        			if((grid.getPiece(c+2,l+1) == null || grid.getPiece(c+2,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+2==posMenace.x && l+1==posMenace.y))) {
            			grid.highLightCase[c+2][l+1] = true;
            		}
        		}
        		if(c-2 >= 0 && l+1 <= 7) {
        			if((grid.getPiece(c-2,l+1) == null || grid.getPiece(c-2,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-2==posMenace.x && l+1==posMenace.y))) {
            			grid.highLightCase[c-2][l+1] = true;
            		}
        		}
        		if(c-2 >= 0 && l-1 >= 0) {
        			if((grid.getPiece(c-2,l-1) == null || grid.getPiece(c-2,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-2==posMenace.x && l-1==posMenace.y))) {
            			grid.highLightCase[c-2][l-1] = true;
            		}
        		}
        		if(l-1 >= 0 && c+2 <= 7) {
        			if((grid.getPiece(c+2,l-1) == null || grid.getPiece(c+2,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+2==posMenace.x && l-1==posMenace.y))) {
            			grid.highLightCase[c+2][l-1] = true;
            		}
        		}
        		
        		if(l-2 >= 0 && c+1 <= 7) {
        			if((grid.getPiece(c+1,l-2) == null || grid.getPiece(c+1,l-2).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+1==posMenace.x && l-2==posMenace.y))) {
            			grid.highLightCase[c+1][l-2] = true;
            		}
        		}
        		if(c-1 >= 0 && l-2 >= 0) {
        			if((grid.getPiece(c-1,l-2) == null || grid.getPiece(c-1,l-2).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-1==posMenace.x && l-2==posMenace.y))) {
            			grid.highLightCase[c-1][l-2] = true;
            		}
        		}       		      		
        	}
        	
        	if(grid.getPiece(c,l) instanceof Fou) { //Deplacement du fou
        		grid.resetHighlight();
        		if(!RoiEchec) {
        			for(int i = 1; i < 8;i++) {
            			if(c+i <= 7 && l+i <= 7){
            				if(grid.getPiece(c+i,l+i) == null && !RoiEchec) grid.highLightCase[c+i][l+i] = true;
            				if(grid.getPiece(c+i,l+i) != null && grid.getPiece(c+i,l+i).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+i==posMenace.x && l+i==posMenace.y))) {
            					grid.highLightCase[c+i][l+i] = true;
            					break;
            				}
            				if(grid.getPiece(c+i,l+i) != null && grid.getPiece(c+i,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;
            			}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(c-i >= 0 && l-i >= 0) {
            				if(grid.getPiece(c-i,l-i) == null && !RoiEchec) grid.highLightCase[c-i][l-i] = true;
            				if(grid.getPiece(c-i,l-i) != null && grid.getPiece(c-i,l-i).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-i==posMenace.x && l-i==posMenace.y))) {
            					grid.highLightCase[c-i][l-i] = true;
            					break;
            				}
            				if(grid.getPiece(c-i,l-i) != null && grid.getPiece(c-i,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;
            			}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(c+i <= 7 && l-i >= 0) { 
            				if(grid.getPiece(c+i,l-i) == null && !RoiEchec) grid.highLightCase[c+i][l-i] = true;
            				if(grid.getPiece(c+i,l-i) != null && grid.getPiece(c+i,l-i).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+i==posMenace.x && l-i==posMenace.y))) {
            					grid.highLightCase[c+i][l-i] = true;
            					break;
            				}
            				if(grid.getPiece(c+i,l-i) != null && grid.getPiece(c+i,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;
            			}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(c-i >= 0 && l+i <= 7) {
            				if(grid.getPiece(c-i,l+i) == null && !RoiEchec) grid.highLightCase[c-i][l+i] = true;
            				if(grid.getPiece(c-i,l+i) != null && grid.getPiece(c-i,l+i).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-i==posMenace.x && l+i==posMenace.y))) {
            					grid.highLightCase[c-i][l+i] = true;
            					break;
            				}
            				if(grid.getPiece(c-i,l+i) != null && grid.getPiece(c-i,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;
            			}
            		}
            	}
        	}		
        	
        	if(grid.getPiece(c,l) instanceof Tour) { //Deplacement de la tour
        		grid.resetHighlight();
        		if(!RoiEchec) {
        			for(int i = 1; i < 8;i++) {
            			if(l+i <= 7) {
            				if(grid.getPiece(c,l+i) == null)grid.highLightCase[c][l+i] = true;
    	        			if(grid.getPiece(c,l+i) != null && (grid.getPiece(c,l+i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l+i==posMenace.y))) {
    	        				grid.highLightCase[c][l+i] = true;
    	        				break;
    	        			}
    	        			if(grid.getPiece(c,l+i) != null && grid.getPiece(c,l+i).Couleur() == grid.getPiece(c,l).Couleur()) break;    	        			
    	        		}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(c+i <= 7) {
            				if(grid.getPiece(c+i,l) == null) grid.highLightCase[c+i][l] = true;
    	        			if(grid.getPiece(c+i,l) != null && (grid.getPiece(c+i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+i==posMenace.x && l==posMenace.y))) {
    	        				grid.highLightCase[c+i][l] = true;
    	        				break;
    	        			}
    	        			if(grid.getPiece(c+i,l) != null && grid.getPiece(c+i,l).Couleur() == grid.getPiece(c,l).Couleur()) break;  	        			
    	        		}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(l-i >= 0) {
            				if(grid.getPiece(c,l-i) == null)grid.highLightCase[c][l-i] = true;
    	        			if(grid.getPiece(c,l-i) != null && (grid.getPiece(c,l-i).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l-i==posMenace.y))) {
    	        				grid.highLightCase[c][l-i] = true;
    	        				break;
    	        			}
    	        			if(grid.getPiece(c,l-i) != null && grid.getPiece(c,l-i).Couleur() == grid.getPiece(c,l).Couleur()) break;  	        		
    	        		}
            		}
            		
            		for(int i = 1; i < 8;i++) {
            			if(c-i >= 0) {
            				if(grid.getPiece(c-i,l) == null)grid.highLightCase[c-i][l] = true;
    	        			if(grid.getPiece(c-i,l) != null && (grid.getPiece(c-i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-i==posMenace.x && l==posMenace.y))) {
    	        				grid.highLightCase[c-i][l] = true;
    	        				break;
    	        			}
    	        			if(grid.getPiece(c-i,l) != null && grid.getPiece(c-i,l).Couleur() == grid.getPiece(c,l).Couleur()) break; 	        			
    	        		}
            		}
            	}
        	}
        	
        	/////
        	//Deplacement pour les pieces variantes
        	
        	if(grid.getPiece(c,l) instanceof Catapulte) {
        		grid.resetHighlight();
        		if(l < 7) {
        			if(grid.getPiece(c,l+1) == null && !RoiEchec) grid.highLightCase[c][l+1] = true;
        		}
        		if(l > 0) {
        			if(grid.getPiece(c,l-1) == null && !RoiEchec) grid.highLightCase[c][l-1] = true;
        		}
        		
        		for(int i = 1; i < 8;i++) {
            		if(c+i <= 7) {
    	        		if(grid.getPiece(c+i,l) != null && (grid.getPiece(c+i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+i==posMenace.x && l==posMenace.y))) {
    	        			grid.highLightCase[c+i][l] = true;
    	        		}        		
            		}
        		}
        		
        		for(int i = 1; i < 8;i++) {
            		if(c-i >= 0) {
    	        		if(grid.getPiece(c-i,l) != null && (grid.getPiece(c-i,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-i==posMenace.x && l==posMenace.y))) {
    	        			grid.highLightCase[c-i][l] = true;
    	        		}	        		
            		}
        		}
        	}
        	
        	if(grid.getPiece(c,l) instanceof JeuneFou) {
        		grid.resetHighlight();
        		if(c+1 <= 7 && l+1 <= 7){
        			if(grid.getPiece(c+1,l+1) == null && !RoiEchec) grid.highLightCase[c+1][l+1] = true;
        		}        		
    			if(c-1 >= 0 && l-1 >= 0) {
    				if(grid.getPiece(c-1,l-1) == null && !RoiEchec) grid.highLightCase[c-1][l-1] = true;
    			}        		
        		if(c+1 <= 7 && l-1 >= 0) { 
        			if(grid.getPiece(c+1,l-1) == null && !RoiEchec) grid.highLightCase[c+1][l-1] = true;
        		}        		 
    			if(c-1 >= 0 && l+1 <= 7) {
    				if(grid.getPiece(c-1,l+1) == null && !RoiEchec) grid.highLightCase[c-1][l+1] = true;
    			}
    			//Partie des mouvements pour qu'il mange
    			if(l < 7) {
        			if((grid.getPiece(c,l+1) != null && grid.getPiece(c,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c][l+1] = true;
        		}
        		if(l > 0) {
        			if((grid.getPiece(c,l-1) != null && grid.getPiece(c,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c][l-1] = true;
        		}
        		if(c < 7) {
        			if((grid.getPiece(c+1,l) != null && grid.getPiece(c+1,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c+1==posMenace.x && l==posMenace.y))) grid.highLightCase[c+1][l] = true;
        		}
        		if(c > 0) {
        			if((grid.getPiece(c-1,l) != null && grid.getPiece(c-1,l).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c-1==posMenace.x && l==posMenace.y))) grid.highLightCase[c-1][l] = true;
        		}
        	}
        	
        	if(grid.getPiece(c,l) instanceof PionC) {
        		grid.resetHighlight();
        		if(l < 7) {
        			if(grid.getPiece(c,l+1) == null || (grid.getPiece(c,l+1) != null && grid.getPiece(c,l+1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c][l+1] = true;
        		}
        		if(l > 0) {
        			if(grid.getPiece(c,l-1) == null || (grid.getPiece(c,l-1) != null && grid.getPiece(c,l-1).Couleur() != grid.getPiece(c,l).Couleur()) && (!RoiEchec || (c==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c][l-1] = true;
        		}
    			
        		if(grid.getPiece(c,l).Couleur()) {
        			if(c+1 <= 7 && l+1 <= 7){
            			if(grid.getPiece(c+1,l+1) != null && grid.getPiece(c+1,l+1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c+1][l+1] = true;
            		}
        			if(c-1 >= 0 && l+1 <= 7) {
        				if(grid.getPiece(c-1,l+1) != null && grid.getPiece(c-1,l+1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c-1][l+1] = true;
        			}
        		}
        		else {
        			if(c-1 >= 0 && l-1 >= 0) {
        				if(grid.getPiece(c-1,l-1) != null && grid.getPiece(c-1,l-1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c-1][l-1] = true;
        			}
        			if(c+1 >= 0 && l-1 >= 0) {
        				if(grid.getPiece(c+1,l-1) != null && grid.getPiece(c+1,l-1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c+1][l-1] = true;
        			}
        		}  			        		
        	}
        	
        	if(grid.getPiece(c,l) instanceof Paladin) { //Deplacement du roi
        		grid.resetHighlight();
        		if(l < 7) {
        			if(grid.getPiece(c,l+1) == null || grid.getPiece(c,l+1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c][l+1] = true;
        		}
        		if(l+2 < 7) {
        			if(grid.getPiece(c,l+2) != null && grid.getPiece(c,l+2).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c==posMenace.x && l+2==posMenace.y))) grid.highLightCase[c][l+2] = true;
        		}
        		if(l > 0) {
        			if(grid.getPiece(c,l-1) == null || grid.getPiece(c,l-1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c][l-1] = true;
        		}
        		if(l-2 > 0) {
        			if(grid.getPiece(c,l-2) != null && grid.getPiece(c,l-2).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c==posMenace.x && l-2==posMenace.y))) grid.highLightCase[c][l-2] = true;
        		}
        		if(c < 7) {
        			if(grid.getPiece(c+1,l) == null || grid.getPiece(c+1,l).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+1==posMenace.x && l==posMenace.y))) grid.highLightCase[c+1][l] = true;
        		}
        		if(c+2 < 7) {
        			if(grid.getPiece(c+2,l) != null && grid.getPiece(c+2,l).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+2==posMenace.x && l==posMenace.y))) grid.highLightCase[c+2][l] = true;
        		}
        		if(c > 0) {
        			if(grid.getPiece(c-1,l) == null || grid.getPiece(c-1,l).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-1==posMenace.x && l==posMenace.y))) grid.highLightCase[c-1][l] = true;
        		}
        		if(c-2 > 0) {
        			if(grid.getPiece(c-2,l) != null && grid.getPiece(c-2,l).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-2==posMenace.x && l==posMenace.y))) grid.highLightCase[c-2][l] = true;
        		}
        		if(c+1 <= 7 && l+1 <= 7){
        			if(grid.getPiece(c+1,l+1) == null || grid.getPiece(c+1,l+1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c+1][l+1] = true;
        		}        		
    			if(c-1 >= 0 && l-1 >= 0) {
    				if(grid.getPiece(c-1,l-1) == null || grid.getPiece(c-1,l-1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c-1][l-1] = true;
    			}        		
        		if(c+1 <= 7 && l-1 >= 0) { 
        			if(grid.getPiece(c+1,l-1) == null || grid.getPiece(c+1,l-1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c+1==posMenace.x && l-1==posMenace.y))) grid.highLightCase[c+1][l-1] = true;
        		}        		 
    			if(c-1 >= 0 && l+1 <= 7) {
    				if(grid.getPiece(c-1,l+1) == null || grid.getPiece(c-1,l+1).Couleur() != grid.getPiece(c,l).Couleur() && (!RoiEchec || (c-1==posMenace.x && l+1==posMenace.y))) grid.highLightCase[c-1][l+1] = true;
    			}			
    		}    
        }
        else {
        	//Affichage dans la console des deplacements 
        	char str = colABC[posTemp.x];
        	System.out.print(str);
        	System.out.println(posTemp.y);
        }
        
        // si la position correspond a une position de deplacement possible, effectuer le deplacement
        if(grid.highLightCase[c][l]) {
        	
            // Supression de l'ancienne piece et de la nouvelle piece sur son ancienne position(jplabel dans le jpanel correspondant à la case de la grille)
        	((JPanel)chessBoard.getComponent((l) * Grid.SIZE_ROW_GRID +c)).removeAll();       		
            ((JPanel)chessBoard.getComponent((posTemp.y) * Grid.SIZE_ROW_GRID +posTemp.x)).removeAll();
            
            //On reset le tableau des cases pour jouer le prochain tour
            grid.resetHighlight();
            // Deplacement de la piece
            grid.movePiece(posTemp.x,posTemp.y,c,l);
            
            //Pour la prise en passant
            if(grid.getPiece(c,l) instanceof Pion && (posTemp.y-l == 2 || l+posTemp.y == 2 || l-posTemp.y == 2 || l+posTemp.y == 2)) grid.getPiece(c,l).deplacement2 = true;       
            else grid.getPiece(c,l).deplacement2 = false;          
            if(l-1 >= 0) {
            	if(grid.getPiece(c,l).Couleur() && grid.getPiece(c,l-1) instanceof Pion && grid.getPiece(c,l).Couleur() != grid.getPiece(c,l-1).Couleur() && grid.getPiece(c,l-1).deplacement2) {
                	((JPanel)chessBoard.getComponent((l-1) * Grid.SIZE_ROW_GRID +c)).removeAll();
                	grid.removePiece(c, l-1);
            	}
            }
            if(l+1 <= 7) {
            	if(!grid.getPiece(c,l).Couleur() && grid.getPiece(c,l+1) instanceof Pion && grid.getPiece(c,l).Couleur() != grid.getPiece(c,l+1).Couleur() && grid.getPiece(c,l+1).deplacement2) {
                	((JPanel)chessBoard.getComponent((l+1) * Grid.SIZE_ROW_GRID +c)).removeAll();
                	grid.removePiece(c, l+1);
            	}
            }
            
            /*Fait appelle a la fonction promotion de la grille si c'est un pion est qu'il a la bonne position
            pour etre promu*/
            if((grid.getPiece(c,l) instanceof Pion && (l == 7 || l == 0) && promotion)){
            	promotion(c,l);
            }
            
            //Si c'etait au tour des noirs avant
            if(grid.getPiece(c,l).Couleur())tourNoir = false;
            //Sinon, si c'etait au tour des blancs avant
            else tourNoir = true;
            
            /*
             * Verification pour la variante 2, si tourn == 2 et que nous jouons dans la variante 2
             * , alors c'est au tour de l'adversaire, sinon c'est encore au tour de l'autre. 
             */
            tourn++;         
            if(variante2) {
            	if(tourn == 2) tourn = 0;
            	else tourNoir = grid.getPiece(c,l).Couleur();
            }          
            
            //Affichage dans la console des deplacements 
            char str = colABC[c];
        	System.out.print(str);
        	System.out.println(l);
            
            //On reset la position temporaire pour la suite
            posTemp.changeVar(0, 0);
            
            //On reset la position temporaire  de la menace prioritaire pour la suite
            posMenace.changeVar(0,0);
            
            //Verification de si le roi de la couleur qui va jouer est en echec
            RoiEchec = en_echec();
            
            //On reset le timer pour le tour suivant
            time.restart();
            }
        // Mettre a jour l'affichage
        drawGrid();
    }
     
    
    
    
    
    /////
    //renvoie si le positon teste est dans les choix de la position courante , x et y position teste
    public boolean deplacement_nonpossible(int x,int y) {
    	
    	//Permet de directement dire qu'un deplacement n'est pas possible
    	if(x > 7 || y > 7 || x < 0 || y < 0) return true; 
    	
    	for(int c = 0;c<8;c++) {
    		for(int l =0;l<8;l++) {
    			if(grid.getPiece(c,l)!=null && (c != x || l != y) && grid.getPiece(c,l).Couleur() != tourNoir){ //Condition pour que se soit une piece et que ce ne soit pas celle en parametre 
    				
			    	if(grid.getPiece(c,l) instanceof Pion) { //Deplacement possible du pion		    		
						if(grid.getPiece(c,l).Couleur()) {							
							if(l < 7 && c < 7) {								
								if(grid.getPiece(c+1,l+1) != null && (c+1==x) && (l+1==y)) {
									posMenace.changeVar(c,l);							
									return true;
								}
								if(grid.getPiece(c+1,l+1) == null && (c+1==x) && (l+1==y)) {
									return true;
								}
							}
							if(l < 7 && c > 0) {
								if(grid.getPiece(c-1,l+1) != null && (c-1==x) && (l+1==y)) {
									posMenace.changeVar(c,l);
									return true;	
								}
								if(grid.getPiece(c-1,l+1) == null && (c-1==x) && (l+1==y)) {
									return true;
								}								
							}
						}
						else {
							if(l > 0 && c < 7) {
								
								if(grid.getPiece(c+1,l-1) != null && (c+1==x) && (l-1==y)) {
									posMenace.changeVar(c,l);
									return true; 
								}
								if(grid.getPiece(c+1,l-1) == null && (c+1==x) && (l-1==y)) {
									return true;
								}
									
								
							}
							if(l > 0 && c > 0) {								
								if(grid.getPiece(c-1,l-1) != null && (c-1==x) && (l-1==y)) {
									posMenace.changeVar(c,l);
									return true;
								}
								if(grid.getPiece(c-1,l-1) == null && (c-1==x) && (l-1==y)) {
									return true;
								}								
							}
						}
			    	}			    	
			    	
			    	if(grid.getPiece(c,l) instanceof Roi) { //Deplacement du roi
			    		if(l < 7) {		    			
			    			if(c==x && l+1==y) return true;
			    		}
			    		if(l > 0) {		    			
			    			if(c==x && l-1==y) return true;
			    		}
			    		if(c < 7) {			    			
			    			if(c+1==x && l==y) return true;
			    		}
			    		if(c > 0) {			    			
			    			if(c-1==x && l==y) return true;			 
			    		}
			    		if(c+1 <= 7 && l+1 <= 7){		    			
			    			if(c+1==x && l+1==y) return true;
			    		}        		
						if(c-1 >= 0 && l-1 >= 0) {							
							if(c-1==x && l-1==y) return true;
						}        		
			    		if(c+1 <= 7 && l-1 >= 0) {
			    			if(c+1==x && l-1==y) return true;
			    		}        		
						if(c-1 >= 0 && l+1 <= 7) {							
							if(c-1==x && l+1==y) return true;
						}
			    	}
			    	
			    	if(grid.getPiece(c,l) instanceof Reine) { //Deplacement de la renne
			    		
			    		//On fait des verifications pour que sa soit dans la grille
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7 && l+i <= 7){		    				
			    				if(grid.getPiece(c+i,l+i) != null) {
			    					if(grid.getPiece(c+i,l+i).Couleur() != grid.getPiece(c,l).Couleur() && (c+i==x) && (l+i==y)) {
			    						posMenace.changeVar(c,l);
			        					return true;
			        				}
			    					else break;
			    				}
			    				if(grid.getPiece(c+i,l+i) == null && (c+i==x) && (l+i==y)) return true;		    					
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0 && l-i >= 0) {
			    				if(grid.getPiece(c-i,l-i) != null) {
			    					if(grid.getPiece(c-i,l-i).Couleur() != grid.getPiece(c,l).Couleur() && (c-i==x) && (l-i==y)) {
			    						posMenace.changeVar(c,l);
			        					return true;
			        				}
			    					else break;
			    				}
			    				if(grid.getPiece(c-i,l-i) == null && (c-i==x) && (l-i==y)) return true;
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7 && l-i >= 0) {  		    				
			    				if(grid.getPiece(c+i,l-i) != null) {
			    					if(grid.getPiece(c+i,l-i).Couleur() != grid.getPiece(c,l).Couleur() && (c+i==x) && (l-i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else break;
			    				}
			    				if(grid.getPiece(c+i,l-i) == null && (c+i==x) && (l-i==y)) return true;   				
			    			}
			    		}
			    			
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0 && l+i <= 7) {			    				
			    				if(grid.getPiece(c-i,l+i) != null) {
			    					if(grid.getPiece(c-i,l+i).Couleur() != grid.getPiece(c,l).Couleur() && (c-i==x) && (l+i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else break;
			    				}
			    				if(grid.getPiece(c-i,l+i) == null && (c-i==x) && (l+i==y)) return true;
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(l+i <= 7) {			    				
			        			if(grid.getPiece(c,l+i) != null) {
			        				if(grid.getPiece(c,l+i).Couleur() != grid.getPiece(c,l).Couleur() && (c==x) && (l+i==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c,l+i) == null && (c==x) && (l+i==y)) return true;     			
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7) {			    				
			        			if(grid.getPiece(c+i,l) != null) {
			        				if(grid.getPiece(c+i,l).Couleur() != grid.getPiece(c,l).Couleur() && (c+i==x) && (l==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;    			
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c+i,l) == null && (c+i==x) && (l==y)) return true;
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(l-i >= 0) {			    				
			        			if(grid.getPiece(c,l-i) != null) {
			        				if(grid.getPiece(c,l-i).Couleur() != grid.getPiece(c,l).Couleur() && (c==x) && (l-i==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c,l-i) == null && (c==x) && (l-i==y)) return true;
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0) {			    				
			        			if(grid.getPiece(c-i,l) != null) {
			        				if((c-i==x) && (l==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c-i,l) == null && (c-i==x) && (l==y)) return true;
			        		}
			    		}
			    	}
			    	
			    	if(grid.getPiece(c,l) instanceof Cavalier) { //Deplacement du cavalier
			    		
			    		if(c+1 <= 7 && l+2 <= 7) {	    		
			    			if(grid.getPiece(c+1,l+2) != null) {
			    				if(grid.getPiece(c+1,l+2).Couleur() != grid.getPiece(c,l).Couleur() && (c+1==x) && (l+2==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c+1,l+2) == null && (c+1==x) && (l+2==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}	
			    		if(c-1 >= 0 && l+2 <= 7 ) {
			    			if(grid.getPiece(c-1,l+2) != null) {
			    				if(grid.getPiece(c-1,l+2).Couleur() != grid.getPiece(c,l).Couleur() && (c-1==x) && (l+2==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c-1,l+2) == null && (c-1==x) && (l+2==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(c+2 <= 7 && l+1 <= 7 ) {
			    			if(grid.getPiece(c+2,l+1) != null) {
			    				if(grid.getPiece(c+2,l+1).Couleur() != grid.getPiece(c,l).Couleur()&& (c+2==x) && (l+1==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;    		
			    				}
			        		}
			    			if(grid.getPiece(c+2,l+1) == null && (c+2==x) && (l+1==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(c-2 >= 0 && l+1 <= 7) {
			    			if(grid.getPiece(c-2,l+1) != null) {
			    				if(grid.getPiece(c-2,l+1).Couleur() != grid.getPiece(c,l).Couleur() && c-2==x && l+1==y) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c-2,l+1) == null && (c-2==x) && (l+1==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(c-2 >= 0 && l-1 >= 0) {
			    			if(grid.getPiece(c-2,l-1) != null) {
			    				if(grid.getPiece(c-2,l-1).Couleur() != grid.getPiece(c,l).Couleur()&& (c-2==x) && (l-1==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c-2,l-1) == null && (c-2==x) && (l-1==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(l-1 >= 0 && c+2 <= 7) {
			    			if(grid.getPiece(c+2,l-1) != null) {
			    				if(grid.getPiece(c+2,l-1).Couleur() != grid.getPiece(c,l).Couleur()&& (c+2==x) && (l-1==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c+2,l-1) == null && (c+2==x) && (l-1==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(l-2 >= 0 && c+1 <= 7) {
			    			if(grid.getPiece(c+1,l-2) != null) {
			    				if(grid.getPiece(c+1,l-2).Couleur() != grid.getPiece(c,l).Couleur()&& (c+1==x) && (l-2==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c+1,l-2) == null && (c+1==x) && (l-2==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}
			    		if(c-1 >= 0 && l-2 >= 0) {
			    			if(grid.getPiece(c-1,l-2) != null) {
			    				if(grid.getPiece(c-1,l-2).Couleur() != grid.getPiece(c,l).Couleur()&& (c-1==x) && (l-2==y)) {
			    					posMenace.changeVar(c,l);
			    					return true;
			    				}
			        		}
			    			if(grid.getPiece(c-1,l-2) == null && (c-1==x) && (l-2==y) && grid.getPiece(c,l).Couleur() != tourNoir) return true;
			    		}			    		
			    	}
			    	
			    	if(grid.getPiece(c,l) instanceof Fou) { //Deplacement du fou
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7 && l+i <= 7){
			    				if(grid.getPiece(c+i,l+i) != null) {
			    					if((c+i==x) && (l+i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else  break;
			    				}
			    				if(grid.getPiece(c+i,l+i) == null && (c+i==x) && (l+i==y)) return true;
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0 && l-i >= 0) {
			    				if(grid.getPiece(c-i,l-i) != null) {
			    					if((c-i==x) && (l-i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else break;
			    				}
			    				if(grid.getPiece(c-i,l-i) == null && (c-i==x) && (l-i==y)) return true;
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7 && l-i >= 0) {
			    				if(grid.getPiece(c+i,l-i) != null) {
			    					if((c+i==x) && (l-i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else break;
			    				}
			    				if(grid.getPiece(c+i,l-i) == null && (c+i==x) && (l-i==y)) return true;
			    			}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0 && l+i <= 7) {
			    				if(grid.getPiece(c-i,l+i) != null) {
			    					if((c-i==x) && (l+i==y)) {
			    						posMenace.changeVar(c,l);
			    						return true;
			    					}
			    					else break;
			    				}
			    				if(grid.getPiece(c-i,l+i) == null && (c-i==x) && (l+i==y)) return true;
			    			}
			    		}
			    			
			    	}
			    	
			    	if(grid.getPiece(c,l) instanceof Tour) {
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(l+i <= 7) {
			        			if(grid.getPiece(c,l+i) != null) {
			        				if((c==x) && (l+i==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c,l+i) == null && c==x && l+i==y) return true;
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c+i <= 7) {
			        			if(grid.getPiece(c+i,l) != null) {
			        				if((c+i==x) && (l==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c+i,l) == null && c+i==x && l==y) return true;
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(l-i >= 0) {
			        			if(grid.getPiece(c,l-i) != null) {
			        				if((c==x) && (l-i==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;        								
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c,l-i) == null && c==x && l-i==y) return true;
			        		}
			    		}
			    		
			    		for(int i = 1; i < 8;i++) {
			    			if(c-i >= 0) {
			        			if(grid.getPiece(c-i,l) != null) {
			        				if((c-i==x) && (l==y)) {
			        					posMenace.changeVar(c,l);
			        					return true;
			        				}
			        				else break;
			        			}
			        			if(grid.getPiece(c-i,l) == null && c-i==x && l==y) return true;
			        		}
			    		}
			    	}
			    	
			    	////
			    	//Pour toutes les pièces féeriques
			    	if(grid.getPiece(c,l) instanceof Catapulte) {        	

		        		for(int i = 1; i < 8;i++) {
		            		if(c+i <= 7) {
		    	        		if(c+i==x && l==y) {
		    	        			posMenace.changeVar(c,l);
		    	        			return true;        		
		    	        		}
		            		}
		        		}
		        		
		        		for(int i = 1; i < 8;i++) {
		            		if(c-i >= 0) {
		    	        		if(c-i==x && l==y) {
		    	        			posMenace.changeVar(c,l);
		    	        			return true;		    	        		        		
		    	        		}
		            		}
		        		}
		        	}
			    	
			    	if(grid.getPiece(c,l) instanceof JeuneFou) {
		        		
			    		if(l < 7) {		    			
			    			if(c==x && l+1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(l > 0) {		    			
			    			if(c==x && l-1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(c < 7) {			    			
			    			if(c+1==x && l==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(c > 0) {			    			
			    			if(c-1==x && l==y) {
			    				posMenace.changeVar(c,l);
			    				return true;			 
			    			}
			    		}
			    	}
			    	
			    	if(grid.getPiece(c,l) instanceof PionC) {
		        		if(l < 7) {
		        			if(c==x && l+1==y) {
		        				posMenace.changeVar(c,l);
		        				return true;
		        			}
		        		}
		        		if(l > 0) {
		        			if(c==x && l-1==y) {
		        				posMenace.changeVar(c,l);
		        				return true;
		        			}
		        		}
		    			
		        		if(grid.getPiece(c,l).Couleur()) {
		        			if(c+1 <= 7 && l+1 <= 7){
		            			if(c+1==x && l+1==y) {
		            				posMenace.changeVar(c,l);
		            				return true;
		            			}
		            		}
		        			if(c-1 >= 0 && l+1 <= 7) {
		        				if(c-1==x && l+1==y) {
		        					posMenace.changeVar(c,l);
		        					return true;
		        				}
		        			}
		        		}
		        		else {
		        			if(c-1 >= 0 && l-1 >= 0) {
		        				if(c-1==x && l-1==y) {
		        					posMenace.changeVar(c,l);
		        					return true;
		        				}
		        			}
		        			if(c+1 >= 0 && l-1 >= 0) {
		        				if(c+1==x && l-1==y) {
		        					posMenace.changeVar(c,l);
		        					return true;
		        				}
		        			}
		        		}  			        		
		        	}
			    	
			    	if(grid.getPiece(c,l) instanceof Paladin) { //Deplacement du roi  		
		        		if(l+2 < 7) {
		        			if(c==x && l+2==y) {
		        				posMenace.changeVar(c,l);
		        				return true;		        		
		        			}
		        		}		        		
		        		if(l-2 > 0) {
		        			if(c==x && l-2==y) {
		        				posMenace.changeVar(c,l);
		        				return true;		  
		        			}
		        		}      		        		
		        		if(c+2 < 7) {
		        			if(c+2==x && l==y) {
		        				posMenace.changeVar(c,l);
		        				return true;	
		        			}
		        		}
		        		if(c-2 > 0) {
		        			if(c-2==x && l==y) {
		        				posMenace.changeVar(c,l);
		        				return true;
		        			}
		        		}	    			
		    			if(l < 7) {		    			
			    			if(c==x && l+1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(l > 0) {		    			
			    			if(c==x && l-1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(c < 7) {			    			
			    			if(c+1==x && l==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}
			    		if(c > 0) {			    			
			    			if(c-1==x && l==y) {
			    				posMenace.changeVar(c,l);
			    				return true;			 
			    			}
			    		}
			    		if(c+1 <= 7 && l+1 <= 7){		    			
			    			if(c+1==x && l+1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}        		
						if(c-1 >= 0 && l-1 >= 0) {							
							if(c-1==x && l-1==y) {
								posMenace.changeVar(c,l);
								return true;
							}
						}        		
			    		if(c+1 <= 7 && l-1 >= 0) {
			    			if(c+1==x && l-1==y) {
			    				posMenace.changeVar(c,l);
			    				return true;
			    			}
			    		}        		
						if(c-1 >= 0 && l+1 <= 7) {							
							if(c-1==x && l+1==y) {
								posMenace.changeVar(c,l);
								return true;
							}
						}
		    		}
			    }  	
			}		 
		}
		return false;
    }
    
    
    //Methode pour verifier si le roi a encore des possibilites de deplacement
    public boolean deplacement_RoiPossible(int x,int y) {  	  	
    	if(y-1 > 0) {
    		if(grid.getPiece(x,y-1) == null && !deplacement_nonpossible(x,y-1)) return true;
    	}
    	if(y+1 < 7) {
    		if(grid.getPiece(x,y+1) == null && !deplacement_nonpossible(x,y+1)) return true;
    	}
    	if(x-1 > 0) {
    		if(grid.getPiece(x-1,y) == null && !deplacement_nonpossible(x-1,y)) return true;
    	}
    	if(x+1 < 7) {
    		if(grid.getPiece(x+1,y) == null && !deplacement_nonpossible(x+1,y)) return true;
    	}
    	if(x-1 > 0 && y-1 > 0) {
    		if(grid.getPiece(x-1, y-1) == null && !deplacement_nonpossible(x-1,y-1)) return true;
    	}
    	if(x+1 < 7 && y-1 > 0) {
    		if(grid.getPiece(x+1, y-1) == null && !deplacement_nonpossible(x+1,y-1)) return true;
    	}
    	if(x+1 < 7 && y+1 < 7) {
    		if(grid.getPiece(x+1, y+1) == null && !deplacement_nonpossible(x+1,y+1)) return true;
    	}
    	if(y+1 < 7 && x-1 > 0) {
    		if(grid.getPiece(x-1, y+1) == null && !deplacement_nonpossible(x-1,y+1)) return true;
    	}
    	
		return false;
    }
    
    //Methode pour savoir si c'est en echec
    public boolean en_echec() {
    	
    	int posRoix = 0;
    	int posRoiy = 0;
    	
    	for(int i=0;i<8;i++) {
    		for(int j =0;j<8;j++) {
    			if(grid.getPiece(i,j) instanceof Roi) {
    				boolean couleur = grid.getPiece(i,j).Couleur();
    				if(couleur == tourNoir) {
	    				posRoix=i;
	    				posRoiy=j;
	    				break;
    				}  				
    			}   			
    		}
    	}
    	return deplacement_nonpossible(posRoix,posRoiy);
    }
    
    //Methode pour echec et mat
    public void EchecetMat() {
    	
    	int posRoix = 0;
    	int posRoiy = 0;
    	for(int i=0;i<8;i++) {
    		for(int j =0;j<8;j++) {
    			if(grid.getPiece(i,j) instanceof Roi) {
    				boolean couleur = grid.getPiece(i,j).Couleur();
    				if(couleur == tourNoir) {
	    				posRoix=i;
	    				posRoiy=j;
	    				break;
    				}  				
    			}   			
    		}
    	}
    	
    	//Si tout les deplacements du roi sont impossible et qu'il est en echec
    	if(RoiEchec && !deplacement_RoiPossible(posRoix,posRoiy)){
    		
    		//Arrete le timer pour qu'il ne nous embêtte plus
    		time.stop();
    		
    		final JFrame frame = new JFrame("Parametre pour partie standard");
    	    JPanel panel = new JPanel();
    	    
    	    String msg;
    	    
    	    if(tourNoir) msg = "Echec et Mat, victoire des blancs";
    	    else msg = "Echec et Mat, victoire des noirs";
    		
    	    JLabel lab = new JLabel(msg);
    	    
    		JButton val = new JButton("Ok");
    	    val.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	mainPanel.setVisible(false);
                	chessBoard.setVisible(false);
                	frame.dispose();
                }	
            });
    	    
    	    panel.add(lab);
    	    panel.add(val);
    	    frame.add(panel);
    	    
    	    frame.setSize(250,100);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
    	}
    }
    
    public void Abandon() {
    	final JFrame frame = new JFrame("Abandon");
	    JPanel panel = new JPanel();
	    
	    JLabel lab = new JLabel("Abandon des " + (tourNoir == true ? "noir ?" : "blanc ?"));
	    
		JButton val = new JButton("Oui");
	    val.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	time.stop();
            	frame.dispose();
            	
            	final JFrame frame2 = new JFrame("Parametre pour partie standard");
        	    JPanel panel2 = new JPanel();
        	    
        	    JLabel lab2 = new JLabel("Victoire des " + (tourNoir == true ? "blanc !" : "noir !"));
        	    
        	    JButton val3 = new JButton("Ok");
        	    val3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	frame2.dispose();
                    }
        	    });
        	    
        	    panel2.add(lab2);
        	    panel2.add(val3);
        	    frame2.add(panel2);
        	    
        	    frame2.setSize(250,100);
                frame2.setVisible(true);
                frame2.setLocationRelativeTo(null);
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	    
            	mainPanel.setVisible(false);
            	chessBoard.setVisible(false);        	
            }	
        });
	    
	    JButton val2 = new JButton("Non");
	    val2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	frame.dispose();
            }	
        });
	    
	    panel.add(lab);
	    panel.add(val);
	    panel.add(val2);
	    frame.add(panel);
	    
	    frame.setSize(220,120);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }  
    
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e) {}
    
}