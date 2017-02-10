package athiraharidas.example.com.canvastest;

/**
 * Represents on element, cell in the map in its array. 
 *
 */
public class Cell {
	/**
	 * 0 -> blocked
	 * 1 -> unblocked
	 */
	char cellType;
	
	/**
	 * x value(column number) of cell in map
	 */
	int x;
	
	/**
	 * y value(row number) of cell in map
	 */
	int y;
	
	/**
	 * Index of this cell in the heap
	 */
	int index;
	
	/**
	 * parent(s) -> pointer to parent cell should give away the path using A*
	 */
	Cell parent;
	
	/**
	 * g(n) -> distance from start node to me
	 */
	double g;
	
	/**
	 * h(n) -> distance from me to goal node
	 */
	double h;
	
	
	public Cell(int x, int y){
		this.x = x;
		this.y = y;
		cellType = '1';
		index=-1;
		parent=null;
		g= 1000000;
	}
	
	public boolean isNotBlocked(){
		return cellType=='1';
	}
	
	public boolean equals(Cell c){
		if(x==c.x && y ==c.y){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return "("+x+","+y+") ="+cellType+ " g = "+g;
	}
	
}
