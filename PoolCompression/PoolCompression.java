/***********************************************************************************
 * Implementation of Algorithm 1 of paper "Pool COmpression for Undirected Graphs"
 * We create 1) pool of data and 2) position data here
 * and then perform Delta and Elias encoding on them. 
 **********************************************************************************/

package PoolCompression;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


class PoolCompression{
	
	public void ApplyPoolCompression(ArrayList<Vertex> orderedGraph, int winSize){
		
		ArrayList<Vertex> PoolData     = new ArrayList<Vertex>();
		ArrayList<Vertex> PositionData = new ArrayList<Vertex>();
		
		PoolData     = createPoolData(orderedGraph, winSize);
		PositionData = createPositionData(PoolData, orderedGraph);
		
		deltaEncodePoolData(PoolData);
		deltaEncodePositionData(PositionData);
	}
		
	public ArrayList<Vertex> createPoolData(ArrayList<Vertex> orgList, int h){
		ArrayList<Vertex>  PoolD  = new ArrayList<Vertex>(); //Pool Data
		ArrayList<Integer> nb     = new ArrayList<Integer>();
		Set<Integer> nbSet        = new HashSet();
		int block = 1, index=0;
		
		System.out.println(">>> Creating Pool with Window Size = "+h);
		
		for(int i=0; i<orgList.size(); i++ ){
			for(int j=0; j<h; j++){				
				for(int k=0; k<orgList.get(index).NeighborList.size(); k++){
					nbSet.add(orgList.get(index).NeighborList.get(k).neighborID);					
				}
				orgList.get(index).blockID = block;
				index++;
				if(index==(orgList.size())) break;
			}
			nb = new ArrayList<Integer>(nbSet);	
			PoolD.add(new Vertex(block));
			block++;
			for(int g=0; g<nb.size(); g++){
				PoolD.get(PoolD.size()-1).NeighborList.add(new Neighbor(nb.get(g).intValue()));
			}
			nb.clear();nbSet.clear(); 
			if(index==(orgList.size())) break;
		}
		System.out.println("Total Blocks in the Pool = "+PoolD.size());

		PoolD = getSortedNeighbors(PoolD); //nbSet disturbs order
		OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "PoolData"+".txt");
		out.savePoolData(PoolD);
		out.closeFile();
		
		return PoolD;
		
	}
	
	public void deltaEncodePoolData(ArrayList <Vertex> compAL){
		
		System.out.println("Delta Encoding of Pool Data ...");
		
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
			current=0; last=0; //we don't use block id for encoding
			for(int j=0; j<compAL.get(i).NeighborList.size(); j++){
				current = compAL.get(i).NeighborList.get(j).neighborID;
				compAL.get(i).NeighborList.get(j).neighborID = current-last;
				last = current;
			}
		}
				
		OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "PoolData_Delta"+".txt");
		out.savePoolData(compAL);
		out.closeFile();
				
		Utility u = new Utility();
		u.EliasCodePoolData(compAL); //Elias Coding of Pool Data
				
	}
	
public ArrayList<Vertex> createPositionData(ArrayList<Vertex> sPool, ArrayList<Vertex> shinAL){
		
		ArrayList<Vertex> posList = new ArrayList<Vertex>();
		int ref = 0;
		boolean done = false;
		System.out.println(">>> Creating Position Data ...");
		for(int i=0; i<shinAL.size(); i++){
			posList.add(new Vertex(shinAL.get(i).vertexID));			
			ref = shinAL.get(i).blockID;
			posList.get(posList.size()-1).blockID= ref;	
			posList.get(posList.size()-1).degree = shinAL.get(i).degree;
			done = false;
			for(int j=0; j<sPool.size(); j++){
				if(sPool.get(j).vertexID == ref){//Here vertexID has block Info					
					for(int m=0; m<shinAL.get(i).NeighborList.size(); m++){
						for(int n=0; n<sPool.get(j).NeighborList.size(); n++){
							if(shinAL.get(i).NeighborList.get(m).neighborID == sPool.get(j).NeighborList.get(n).neighborID){
								posList.get(posList.size()-1).NeighborList.add(new Neighbor(n+1)); //start position with 1
								break;
							}
						}
					}
					done = true;
				}
				if(done==true) break;
			}
		}
		
		OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "PositionData"+".txt");
		out.savePositionData(posList);
		out.closeFile();
		
		return posList;
	
	}
	
	public void deltaEncodePositionData(ArrayList <Vertex> compAL){
		
		System.out.println("Delta Encoding of Position Data ... ");
		
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
		u.EliasCodePositionData(compAL); //Elias Coding of Position Data
		
		OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "PositionData_Delta"+".txt");
		out.savePositionData(compAL);
		out.closeFile();	
				
	}
	
	public  ArrayList<Vertex> getSortedNeighbors(ArrayList<Vertex> compAL){
	
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
} //eof