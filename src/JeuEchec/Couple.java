package JeuEchec;

//Une classe permettant de garder temporairement la position des d'une piece le temps qu'on la deplace
class Couple{
	
	int x;
	int y;
	
	public Couple(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void changeVar(int x,int y) {
		this.x = x;
		this.y = y;
	}
}