package dreamteam.smartcart;
import java.util.ArrayList;

public class Heap {

	private ArrayList<Cell> arr;
	double w;
	
	public Heap(double w){
		arr = new ArrayList<Cell>();
		this.w=w;
	}
	
	public void clear(){
		for(int i=0;i<arr.size();i++){
			arr.get(i).index=-1;
		}
		arr.clear();
	}
	
	public boolean isEmpty(){
		return arr.isEmpty();
	}
	
	public void insert(Cell c){
		arr.add(c);
		c.index = arr.size()-1;
		siftUp(arr.size()-1);
	}

	private void siftUp(int i) {
		if(i<=0){
			return;
		}
		int pind= (i-1)/2;
		//check if you are less than your parent
		Cell me = arr.get(i);
		Cell parent = arr.get(pind);
		double myf = me.g +me.h*w;
		double pf = parent.g +parent.h*w;
		if(myf<pf || myf==pf && me.g>parent.g ){
			arr.set(i, parent);
			parent.index=i;
			me.index=pind;
			arr.set(pind, me);
			siftUp(pind);
		}
		
		
	}
	
	public Cell pop(){
		Cell head = arr.get(0);
		head.index=-1;
		arr.set(0, arr.get(arr.size()-1));
		arr.get(0).index=0;
		arr.remove(arr.size()-1);
		siftDown(0);
		return head;
	}

	private void siftDown(int i) {
		if(i>=arr.size()){
			return;
		}
		int leftchild= i*2+1;
		int rightchild= i*2+2;
		Cell left,right;
		double lf,rf;
		if(leftchild>=arr.size()){
			return;
		}
		else if(rightchild<arr.size()){
			left = arr.get(leftchild);
			right = arr.get(rightchild);
			lf = left.g+ left.h*w;
			rf = right.g+ right.h*w;
			if(rf<lf || rf==lf && right.g>left.g ){ //right side is heavier
				leftchild=rightchild;
				left = right;
				lf=rf;
			}
			
		}else{
			left = arr.get(leftchild);
			lf = left.g+ left.h*w;
		}
		Cell me = arr.get(i);
		double myf = me.g+me.h*w;
		//check if you are greater than your child
		if(lf<myf || myf==lf && left.g>me.g){
			arr.set(i, left);
			left.index=i;
			me.index=leftchild;
			arr.set(leftchild, me);
			siftDown(leftchild);
		}
	}
	
	public String toString(){
		return arr.toString();
	}
	
	public void remove(Cell c){
		if(arr.size()==1){
			clear();
			return;
		}
		arr.set(c.index, arr.get(arr.size()-1));
		arr.get(c.index).index=c.index;
		arr.remove(arr.size()-1);
		siftDown(c.index);
		c.index=-1;
	}
	
}
