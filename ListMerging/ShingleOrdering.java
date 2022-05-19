package ListMerging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class ShingleOrdering{
	
	ArrayList<Vertex>  shinAL       = new ArrayList<Vertex>();
	
	
	public ArrayList<Vertex> startProcess(ArrayList<Vertex> orgAL){
				
		shinAL = getSortedNeighbors(orgAL);
		for(int i=0; i<shinAL.size(); i++){ //assign shingles			
			shinAL.get(i).shingle1 = shinAL.get(i).NeighborList.get(0).neighborID;
			if(shinAL.get(i).NeighborList.size()>1)
				shinAL.get(i).shingle2 = shinAL.get(i).NeighborList.get(1).neighborID;			
		}
		getSortedShingles2(); //Double Shingles 
		return shinAL;
				
	}
	
	public  ArrayList<Vertex> getSortedNeighbors(ArrayList<Vertex> compAL){
		
		System.out.println("Sorting Neighbors in Ascending...");
		for(int i=0; i< compAL.size(); i++){
			Collections.sort(compAL.get(i).NeighborList, new Comparator<Neighbor>() {
			    @Override
			    public int compare(Neighbor object1, Neighbor object2) {			     
			    	if      (object1.neighborID > object2.neighborID) return 1;
			    	else if (object1.neighborID < object2.neighborID) return -1;
			    	else return 0;
			    }
			});	
		}
				
		return compAL;
		}

	public  void getSortedShingles2(){
		
		System.out.println("Sorting Shingles Double in Ascending...");
			Collections.sort(shinAL, new Comparator<Vertex>() {
			    @Override
			    public int compare(Vertex object1, Vertex object2) {			     
			    	if      (object1.shingle1 > object2.shingle1) return 1; 
			    	else if (object1.shingle1 < object2.shingle1) return -1;
			    	else {
			    		if      (object1.shingle2 > object2.shingle2) return 1; 
				    	else if (object1.shingle2 < object2.shingle2) return -1;
				    	else return 0;				    	
			    	}
			    }
			});	
			
		}
}