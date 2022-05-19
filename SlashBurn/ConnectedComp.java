/**************************************************************************
 * Implementation of Slash Burn algorithm as explained in the paper:
 * Y. Lim, U. Kang, and C. Faloutsos, “Slashburn: Graph compression and
 * mining beyond caveman communities,” IEEE Transactions on Knowledge
 * and Data Engineering, vol. 26, no. 12, pp. 3077–3089, 2014.
 * 
 * In this class, we decompose the graph into hubs and spokes, and order the nodes.
 * This is a very time consuming process, therefore, we perform it once for any graph
 * and save the node ordering in a separate file (SB_Order.txt). We then read the original graph and 
 * node ordering to create Adjacency Matrix for compression.
 ******************************************************************************/

package SlashBurn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;


public class ConnectedComp{
	
	ArrayList<Vertex>  shinAL     = new ArrayList<Vertex>();	
	ArrayList <Integer> hubs      = new ArrayList<Integer>();
	ArrayList <Integer> spokes    = new ArrayList();
		
	//Hub Ordering, here k=0.005*|G| hubs are removed per iteration
	public void startPoc(ArrayList<Vertex> orgAL){
		
		int kHubs = (int) java.lang.Math.ceil(0.005*orgAL.size());
		int iteration = 1;		
		int hID = 1;		
		int stop = 0;
		shinAL = degSort(orgAL);
		System.out.println("K-Hubs = "+kHubs+" Iteration: "+iteration);
		while(true){
	//		System.out.println("K-Hubs = "+kHubs+" Iteration: "+iteration);
			while(hID <= kHubs*iteration){
				removeHub(shinAL.get(0).vertexID, hID); //ID of hub node in G
				hID++;
			}
			stop = connectedComponents();
			iteration++;
			if(stop <= kHubs) break;
		}
		//remaining nodes are of next GCC
		for(int i=0; i<shinAL.size(); i++){
			hubs.add(shinAL.get(i).vertexID);
		}
		for(int i=0; i<spokes.size(); i++){
			hubs.add(spokes.get(i));
		}
		System.out.println("Node Ordering for size= "+hubs.size());		
//		for(int i=0; i<hubs.size(); i++){
//			System.out.println(hubs.get(i));
//		}
		
		OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "SB_Order.txt");
		out.saveHubInfo(hubs);
		out.closeFile();
	}
	
	public ArrayList<Vertex> degSort(ArrayList<Vertex> oldAL){
		
		System.out.println("Sorting in Degree sort....");
		//Sorting nodes in descending order of degree
		Collections.sort(oldAL, new Comparator<Vertex>() {
		    @Override
		    public int compare(Vertex object1, Vertex object2) {			    
		    	if      (object1.NeighborList.size()  < object2.NeighborList.size())    return 1;
		    	else if (object1.NeighborList.size()  > object2.NeighborList.size())    return -1;
		    	else return 0;
		    }
		});	
				
		return oldAL;
	}
		
	public void removeHub(int hubNode, int hID){
				
		int hubIndex = getIndex(hubNode);
	//	System.out.println("Removing Hub: "+hubNode+" of degree: "+shinAL.get(hubIndex).NeighborList.size());
		hubs.add(hubNode);
		int hubFIndex = 0;
		for(int i=0; i<shinAL.get(hubIndex).NeighborList.size(); i++){
			hubFIndex = getIndex(shinAL.get(hubIndex).NeighborList.get(i).neighborID);
			shinAL.get(hubFIndex).hubID = hID;
			for(int j=0; j<shinAL.get(hubFIndex).NeighborList.size(); j++){
				if(hubNode == shinAL.get(hubFIndex).NeighborList.get(j).neighborID){
					shinAL.get(hubFIndex).NeighborList.remove(j); //if not removed, BFS can't work properly
					break;
				}				
			}
		}
		shinAL.remove(hubIndex);
   }
   public int connectedComponents(){
		
		int compID = 1;	
		System.out.println("Finding Componenst using BFS...");
		//get CC
		for(int i=0; i<shinAL.size(); i++){
			if(shinAL.get(i).visited == false){
				BFS(shinAL.get(i).vertexID, compID++);				
			}
		}
		System.out.println("Total Components: "+(compID-1));
		
		int CC[] = new int[compID];		
		for(int i=0; i<shinAL.size(); i++){
		//	System.out.println(shinAL.get(i).vertexID+" "+shinAL.get(i).compNum+" "+shinAL.get(i).hubID);
			CC[shinAL.get(i).compNum]++;
		}
		
		int X = manageSpokes(CC);
		return X;
	}
	
	
	public void BFS(int seed, int compID){
		
		ArrayList<Integer> fifo = new ArrayList<Integer>();
		ArrayList<Integer> bfs  = new ArrayList<Integer>();	
				
		fifo.add(seed);
		shinAL.get(getIndex(seed)).visited = true;
		shinAL.get(getIndex(seed)).compNum = compID;
		while(fifo.size()>0){
			bfs.add(fifo.get(0));
			for(int i=0; i<shinAL.size(); i++){
				if(fifo.get(0) == shinAL.get(i).vertexID){
					for(int j=0; j<shinAL.get(i).NeighborList.size(); j++){
						if(shinAL.get(i).NeighborList.get(j).visited == false){
							fifo.add(shinAL.get(i).NeighborList.get(j).neighborID);
							shinAL.get(i).NeighborList.get(j).visited = true;
							shinAL.get(getIndex(shinAL.get(i).NeighborList.get(j).neighborID)).compNum = compID;
							shinAL.get(getIndex(shinAL.get(i).NeighborList.get(j).neighborID)).visited = true;							
						}
					}
					break;
				}
			}
			fifo.remove(0); 				
		}
	}
	
	public int getIndex(int vID){
		
		int index = 0;
		for(int i=0; i<shinAL.size(); i++){
			if(shinAL.get(i).vertexID == vID){
				index = i;
				break;
			}			
		}
		return index;
	}
	
	public int manageSpokes(int CC[]){
	//	System.out.println("Managing Spokes....");
	
		int maxHubID = 0;		
		int counter = 0;
		for(int i=1; i<CC.length; i++){
			counter = 0; //count nodes in a CC
			maxHubID = 0;
			for(int j=0; j<shinAL.size(); j++){
				if(shinAL.get(j).compNum == i){
					shinAL.get(j).compSize = CC[i];
					counter++;
					if(shinAL.get(j).hubID > maxHubID){
						maxHubID = shinAL.get(j).hubID;
					}
					if(counter >= CC[i]) break;
				}
			}
			//harmonize hubID by giving all nodes in a CC the highest hub id
			counter = 0;
			for(int j=0; j<shinAL.size(); j++){
				if(shinAL.get(j).compNum == i){
					counter++;
					shinAL.get(j).hubID = maxHubID;					
					if(counter >= CC[i]) break;
				}
			}
			
		}
		//sort by hubID and then by CC size for spokes in Descending
		Collections.sort(shinAL, new Comparator<Vertex>() {
		    @Override
		    public int compare(Vertex object1, Vertex object2) {			     
		    	if      (object1.hubID < object2.hubID) return 1; 
		    	else if (object1.hubID > object2.hubID) return -1;
		    	else {
		    		if      (object1.compSize < object2.compSize) return 1; 
			    	else if (object1.compSize > object2.compSize) return -1;
			    	else return 0;
		    	}
		    }
		});	
		//GCC will be at top after sorting, it gets the maximum hubID 
		// Spokes of hubID=1 with smallest size will be at the end of spokes as we have sorted shinAL
		int GCC_Size = shinAL.get(0).compSize;	
		for(int i=GCC_Size; i<shinAL.size(); i++){
			spokes.add(shinAL.get(i).vertexID);
			shinAL.remove(i);
			i--;			
		}

		System.out.println("GCC Size= "+shinAL.size());
		for(int i=0; i<shinAL.size(); i++){
			shinAL.get(i).visited = false;
			shinAL.get(i).removed = false;
			shinAL.get(i).compNum = 0;
			shinAL.get(i).compSize = 0;
			for(int j=0; j<shinAL.get(i).NeighborList.size(); j++){
				shinAL.get(i).NeighborList.get(j).visited = false;				
			}
		}
		return GCC_Size;
	}
}