import java.util.ArrayList;
import java.util.HashSet;

public class LocationAlgo {
	
	
	public static int gH(Coordinate curr, Coordinate goal){
		int x = Math.abs(goal.x - curr.x);
		int y = Math.abs(goal.y - curr.y);
	
		return x+y;
	}
	
	Heap fringe;
	/**
	 * Gives the number of nodes expanded
	 */
	HashSet<Cell> closed;
	Map m;
	
	public LocationAlgo(Map m){
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
		if(x>=0){
			if(c.getDistance(m.board[x][y])!=null){coord.add(m.board[x][y]);}
		}
		y--;x++;
		//up
		if(y>=0){if(c.getDistance(m.board[x][y])!=null){coord.add(m.board[x][y]);}}
		x++;
		//right
		y++;
		if(x<160){if(c.getDistance(m.board[x][y])!=null){coord.add(m.board[x][y]);}}
		y++;
		//down
		x--;
		if(y<120 ){if(c.getDistance(m.board[x][y])!=null){coord.add(m.board[x][y]);}}
		return coord;
	}
	
	
	
	
	private void mainPart() {
		m.sStart.g.m=0;
		m.sStart.g.n=0;
		fringe.insert(m.sStart);
		m.sStart.parent = m.sStart;
		while(!fringe.isEmpty()){
			Cell s = fringe.pop();
			if(s.equals(m.sGoal)){
				return;
			}
			closed.add(s);
			ArrayList<Cell> neighbors = findSurroundingCells(s);
			for(int i=0; i<neighbors.size(); i++){
				Cell sprime = neighbors.get(i);
				if(!closed.contains(sprime)){
					if(sprime.index==-1){
						sprime.g.m=100000;
						sprime.g.n=100000;
						sprime.parent=null;
					}
					updateVertex(s,sprime);
				}
			}
		}
		
	}

	private void updateVertex(Cell s, Cell sprime) {
		Root2Number gs = s.g.add(s.getDistance(sprime));
		if(gs.compareTo(sprime.g)<0){
			sprime.g.m = gs.m;
			sprime.g.n = gs.n;
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
	public void AStar(){
		closed.clear();
		fringe.clear();
		mainPart();
		Cell pointer=m.sGoal;
		m.AStartpath.clear();
		while(!pointer.equals(m.sStart)){
			m.AStartpath.add(new Coordinate(pointer.x,pointer.y));
			pointer=pointer.parent;
		}
		//f = g+1*h
		return;
	}
	
	
	
}
