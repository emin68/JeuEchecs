package JeuEchec;

/** Classe contenant la grille.
 *  Une grille est caracterisée par une taille en X et Y, les pieces qu'elle contient
 *   et les cases de la grille à mettre en evidence.
 */
public class Grid{
	
    static final int SIZE_LINE_GRID = 8;
    static final int SIZE_ROW_GRID = 8;
    
    /** Liste des pièces sur la grille, chaque pièces étant représentée par une class Pion */
    Piece[][] piece = new Piece[SIZE_LINE_GRID][SIZE_ROW_GRID];
    /** Liste des cases de la grille à mettre en valeur */
    public boolean[][] highLightCase = new boolean[SIZE_LINE_GRID][SIZE_ROW_GRID];
    
    //Pour recuperer la piece en position(c,l)
    Piece getPiece(int colonne, int ligne) { return piece[colonne][ligne]; }
    
    //Pour enlever une piece de la grille
    void removePiece(int c, int l) { piece[c][l] = null; }
    
    //Pour mettre une piece
    void setPiece(Piece p,int c, int l) { piece[c][l] = p; }
    
    //Pour deplacer une piece selon une position
    void movePiece(int fromc, int froml, int toc, int tol) {
    	piece[toc][tol] = piece[fromc][froml];
        piece[fromc][froml] = null;   
    }
    
    //Pour reset toutes les pieces qui etaient en rouge 
    void resetHighlight() {
        for(int i = 0; i< SIZE_LINE_GRID; i++)
            for(int j = 0; j< SIZE_ROW_GRID; j++)
                highLightCase[i][j]=false;
    } 
}



