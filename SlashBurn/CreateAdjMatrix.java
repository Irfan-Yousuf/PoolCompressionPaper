package SlashBurn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.*;


class CreateAdjMatrix{

	ArrayList<Vertex>  sbAL = new ArrayList<Vertex>();
	ArrayList<Integer>  nzValues = new ArrayList<Integer>();
	int nonEBlocks;
	int blockSize;
	
	public void startPoc(ArrayList<Vertex> orgAL, ArrayList<Integer> order){
		
		blockSize = 4096;
		int chunkSize = blockSize;
		
		nonEBlocks = 0;
		sbAL = orderNodes(orgAL, order);

		for(int i=0; i<sbAL.size(); i++) System.out.println(sbAL.get(i).vertexID);
		reLabel();
		int end = orgAL.size()/chunkSize;
		for(int i=0; i<end; i++){
			createMatrix(chunkSize,i);
		}
		System.out.println("Remaining nodes for matrix: "+(sbAL.size()-chunkSize*end));
		System.out.println("Non-Empty Blocks Found: "+nonEBlocks);
		countBits();
		
	}
	
	public ArrayList<Vertex> orderNodes(ArrayList<Vertex> orgAL, ArrayList<Integer> order){
		
		System.out.println("Re-Ordering in Slashburn...");
		
		//we use <K,V> where K is ID of nodes and V is the order in ascending
		//K is node IDs in SlashBurn order as read in file SB_order.txt
		HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
		int val = 1;
		for(int i=0; i<order.size(); i++){
			hmap.put(order.get(i).intValue(), val++);
		}
		for(int i=0; i<orgAL.size(); i++){
			orgAL.get(i).order = hmap.get(orgAL.get(i).vertexID);
		}
		
		Collections.sort(orgAL, new Comparator<Vertex>() {
		    @Override
		    public int compare(Vertex object1, Vertex object2) {			     
		    	if      (object1.order > object2.order) return 1; 
		    	else if (object1.order < object2.order) return -1;
		    	else return 0;
		    }
		});
		
		return orgAL;
	}
	
	public void createMatrix(int chunk, int rowNum){
		
		int mat[][] = new int[chunk][sbAL.size()];
		System.out.println("Block being Processed: "+rowNum);
		rowNum = rowNum * chunk;		
		for(int i=0; i<chunk; i++){
			for(int j=0; j<sbAL.get(rowNum).NeighborList.size(); j++){
				mat[i][sbAL.get(rowNum).NeighborList.get(j).neighborID-1] = 1;
			}
			rowNum++;
		}
		
//		for(int i=0; i<chunk; i++){
//			for(int j=0; j<sbAL.size(); j++){
//				System.out.print(mat[i][j]+" ");
//			}
//			System.out.println();
//		}
		countEachBlock(mat);
	}
		
		public void countBits(){
			
			int T = nzValues.size();
			int b = blockSize;
			int n = sbAL.size();
			
			double p=0, H=0, sum=0, sumF = 0;
			for(int i=0; i<nzValues.size(); i++){
				p = (double) nzValues.get(i) / (b*b);
				H = p * (Math.log((double)1/p)/Math.log(2)) + (1-p)*(Math.log((double)1/(1-p))/Math.log(2));
				sum = sum + (b*b)*H;
				
			}			
			sumF = T*2*(Math.log((double)n/b)/Math.log(2));
			
			System.out.println("1st term Bits= "+sumF);
			System.out.println("2nd term Bits= "+sum);
			System.out.println("Total Bits= "+(sum+sumF));
			
		
	}
	
	public void countEachBlock(int[][] array){
		int bw = blockSize;
		int offSet = array[0].length%bw;
		int eBlocks = 0;
		int tBlocks = 0;
		int nz = 0;
		boolean empty = true;
		//System.out.println("Skipped Columns: "+offSet);
	    for (int i = 0; i < array.length; i = i+bw){           
	        for (int j = 0; j < array[i].length-offSet; j = j+bw){
	          /* Here we are finding which block we are standing at.*/	         	         
	         int newRow = 0;
	         empty = true;
	         for (int k = i; k < (i + bw); k++){	                
	            int newColumn = 0;
	            for (int l = j; l < (j + bw); l++){	                    
	               // This is where you are getting your array inside the given block.
	                if(array[k][l] == 1){
	                	empty = false;
	                	nz++;
	                }
	             }
	             newRow++;	           
	          }
	                // Here you can send your newArray for VALIDATION, thingy.
	                // So that we can move on to the next Block for further processing.
	         if(empty == true) eBlocks++;
	         tBlocks++;
	         if(nz>0) nzValues.add(nz); 
	         nz=0;
	      }
	   }
	    System.out.println("Empty Blocks Found: "+eBlocks+" Total Blocks= "+tBlocks);
//	    for(int i=0; i<nzValues.size(); i++){
//	    	System.out.print(nzValues.get(i)+" ");
//	    }
//	    System.out.println("nzValues size= "+nzValues.size());
	    nonEBlocks = nonEBlocks + (tBlocks-eBlocks);
	}



	public void reLabel(){
		
		HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();		
		int newID=1;
		
		System.out.println("Re-Labelling in SlashBurn Order....");
				
		for(int i=0; i<sbAL.size(); i++){			
				hmap.put(sbAL.get(i).vertexID, newID++); //<Key, Value>				
		} 
		
		//Now re-label the whole graph with saved <key, value> pair		
		for(int j=0; j<sbAL.size(); j++){
		       if(sbAL.get(j).visited == false){
					sbAL.get(j).vertexID = hmap.get(sbAL.get(j).vertexID); //returns value of key
					sbAL.get(j).visited  = true;
				}
				for(int k=0; k<sbAL.get(j).NeighborList.size(); k++){
					if(sbAL.get(j).NeighborList.get(k).visited == false){					
						sbAL.get(j).NeighborList.get(k).neighborID = hmap.get(sbAL.get(j).NeighborList.get(k).neighborID);
						sbAL.get(j).NeighborList.get(k).visited    = true;
				}
			}
		}
		
		//Add filler nodes
		int size = blockSize - (sbAL.size()%blockSize);
		for(int i=0; i<size; i++){
			sbAL.add(new Vertex(newID));
		}
		System.out.println("Graph Size after filler: "+sbAL.size());
		//sort neighbors 
		getSortedNeighbors(sbAL);
				
	}
	public  ArrayList<Vertex> getSortedNeighbors(ArrayList<Vertex> compAL){
		System.out.println("Sorting Neighbors in Ascending ...");
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
	
}
	