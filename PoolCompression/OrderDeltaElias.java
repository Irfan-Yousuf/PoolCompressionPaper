/************************************************************************
 * This class in not part of Pool Compression as such.
 * We only test here if Pooling reduces the number of bits or not.
 * We DO NOT perform pooling here, rather
 * In this class:
 * 1. We order the nodes (Shingle Ordering)
 * 2. We apply Delta Encoding (Gap Encoding)
 * 3. We apply Elias Encoding
 * Then we count the number of bits required to store the graph. 
 ************************************************************************/


package PoolCompression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class OrderDeltaElias{
	
	
	public void NoPoolingTest(ArrayList<Vertex> orgAL){
		
		ArrayList<Vertex>  shinAL       = new ArrayList<Vertex>();
		
		shinAL = getSortedNeighbors(orgAL);
		for(int i=0; i<shinAL.size(); i++){ //assign shingles
			shinAL.get(i).degree   = shinAL.get(i).NeighborList.size();
			shinAL.get(i).shingle1 = shinAL.get(i).NeighborList.get(0).neighborID;
			if(shinAL.get(i).NeighborList.size()>1)
				shinAL.get(i).shingle2 = shinAL.get(i).NeighborList.get(1).neighborID;			
		}
		getShingleNodeOrdering(shinAL); 
		deltaEncoding(shinAL); // Delta and Elias Coding		
	}
	
	/*******************************************************************
	 * Sort the neighbors of a node in ascending order of neighbor IDs
	 * It helps in Ordering the nodes according to Shingle Node ordering
	 * @param compAL
	 * @return
	 *******************************************************************/
	public  ArrayList<Vertex> getSortedNeighbors(ArrayList<Vertex> compAL){
		
		System.out.println("Sorting Neighbors in Ascending order of NodeIDs...");
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

		
	/***********************************************************************************************************
	 * Order the nodes as per Double Shingles node Ordering
	 * See the following paper for details
	 * F. Chierichetti, R. Kumar, S. Lattanzi, M. Mitzenmacher, A. Panconesi,
     * and P. Raghavan, “On compressing social networks,” in Proceedings of the
     * 15th ACM SIGKDD International Conference on Knowledge Discovery and Data Mining, 2009, pp. 219–228
	 ***********************************************************************************************************/
	public  void getShingleNodeOrdering(ArrayList<Vertex> shinAL){
		
		System.out.println("Performing Shingle Node Ordering...");
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
	
	public void deltaEncoding(ArrayList <Vertex> compAL){
		
		System.out.println("Delta Encoding ...");
		
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
		
		int last = 0, current=0;
		for(int i=0; i<compAL.size(); i++){
			current=0; last=0; 
			for(int j=0; j<compAL.get(i).NeighborList.size(); j++){
				current = compAL.get(i).NeighborList.get(j).neighborID;
				compAL.get(i).NeighborList.get(j).neighborID = current-last;
				last = current;
			}
		}
						
		Utility u = new Utility();
		u.EliasCode(compAL);
				
	}
}