package dreamteam.smartcart;
import java.util.ArrayList;
import java.util.HashSet;

public class LocationAlgo {
	
	
	public static int gH(Cell curr, Cell goal){
		int x = Math.abs(goal.x - curr.x);
		int y = Math.abs(goal.y - curr.y);
		return x+y;
	}
	
	Heap fringe;
	/**
	 * Gives the number of nodes expanded
	 */
	HashSet<Cell> closed;
	Cell[][] m;
	
	public LocationAlgo(Cell[][] m){
		this.m=m;
		fringe = new Heap(0);
		closed = new HashSet<Cell>();
	}
	
	/**
	 * Find the 4 surrounding coordinates -> does not include blocked cells
	 * @param c
	 * @return
	 */
	public ArrayList<Cell> findSurroundingCells(Cell c){
		ArrayList<Cell> coord = new ArrayList<Cell>();
		int x = c.x;
		int y= c.y;
		
		//left
		x--;
		if(x>=0 && m[x][y].isNotBlocked()){
			coord.add(m[x][y]);
		}
		y--;x++;
		//up
		if(y>=0&& m[x][y].isNotBlocked()){
			coord.add(m[x][y]);}
		x++;
		//right
		y++;
		if(x<m.length&& m[x][y].isNotBlocked()){
			coord.add(m[x][y]);}
		y++;
		//down
		x--;
		if(y<m[0].length && m[x][y].isNotBlocked()){coord.add(m[x][y]);}
		return coord;
	}
	
	
	
	
	private void mainPart(Cell start, Cell goal) {
		start.g=0;
		fringe.insert(start);
		start.parent = start;
		while(!fringe.isEmpty()){
			Cell s = fringe.pop();
			if(s.equals(goal)){
				return;
			}
			closed.add(s);
			ArrayList<Cell> neighbors = findSurroundingCells(s);
			for(int i=0; i<neighbors.size(); i++){
				Cell sprime = neighbors.get(i);
				if(!closed.contains(sprime)){
					if(sprime.index==-1){
						sprime.g=100000;
						sprime.parent=null;
					}
					updateVertex(s,sprime);
				}
			}
		}
		
	}

	private void updateVertex(Cell s, Cell sprime) {
		double gs = s.g+1;
		if(gs<sprime.g){
			sprime.g = gs;
			sprime.parent=s;
		
			if(sprime.index!=-1){
				fringe.remove(sprime);
			}
			fringe.insert(sprime);
		}
		
	}
	

	/**
	 * Fills out Astarpath in map
	 */
	public ArrayList<Cell> AStar(Cell start, Cell goal){
		closed.clear();
		fringe.clear();
		mainPart(start, goal);
		Cell pointer=goal;
		ArrayList<Cell> ans = new ArrayList<Cell>();
		while(!pointer.equals(start)){
			ans.add(pointer);
			pointer=pointer.parent;
		}
		return ans;
	}
	
	
	
}
