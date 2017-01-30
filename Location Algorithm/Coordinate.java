
public class Coordinate {

	int x;
	int y;
	
	public Coordinate(int x2, int y2) {
		x=x2;
		y=y2;
	}

	public Coordinate() {x=0; y=0;	}

	public boolean equals(Coordinate c){
		if(x==c.x && y ==c.y){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return "("+x+","+y+")";
	}
}
