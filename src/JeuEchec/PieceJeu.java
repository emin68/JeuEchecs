package JeuEchec;

/*
 * Avec cette classe nous definissons tout
 * les pions disponibles pour une partie
 */

abstract class Piece{
	
	/*
	 * Seul parametre permettant de savoir si
	 * la piece est noir ou non
	 */
	protected boolean noir;
	protected boolean deplacement2;
	
	public Piece(boolean noir) {
		this.noir = noir;
	}
	
	//Methode pour svoir si c'est noir ou non
	public boolean Couleur(){
		return noir;
	}
}

class Pion extends Piece{
	
	protected boolean deplacement2 = false;
	
	public Pion(boolean noir) {
		super(noir);
	}
}

class Reine extends Piece{
	
	public Reine(boolean noir) {
		super(noir);
	}
}

class Roi extends Piece{

	public Roi(boolean noir) {
		super(noir);
	}
}

class Tour extends Piece{
	
	public Tour(boolean noir) {
		super(noir);
	}	
}

class Fou extends Piece{
	
	public Fou(boolean noir) {
		super(noir);
	}
}

class Cavalier extends Piece{
	
	public Cavalier(boolean noir) {
		super(noir);
	}
}

//////////Pieces féeriques
class Catapulte extends Piece{
	
	public Catapulte(boolean noir) {
		super(noir);
	}
}

class JeuneFou extends Piece{
	
	public JeuneFou(boolean noir) {
		super(noir);
	}
}

class PionC extends Piece{
	
	public PionC(boolean noir) {
		super(noir);
	}
}

class Paladin extends Piece{
	
	public Paladin(boolean noir) {
		super(noir);
	}
}