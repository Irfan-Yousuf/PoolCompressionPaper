package LayeredLabelPropagation;

import java.util.ArrayList;

public class Node {
	
	public int id;
	public int label;
	public int order;
	public int Ref;
	public ArrayList<Integer> neighbors;
	
	
	public Node(int id, int label) {
		this.id=id;
		this.label=label;
		this.neighbors = new ArrayList<Integer>();
		Ref = 0;
		order = 0;
	}

	public int getId() {
		return id;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(int id) {
		this.neighbors.add(Integer.valueOf(id));
	}
	
}