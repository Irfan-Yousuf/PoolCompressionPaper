/********************************************************************************
 * Implementation of BackLinks (BL) compression scheme as described in the paper:
 * F. Chierichetti, R. Kumar, S. Lattanzi, M. Mitzenmacher, A. Panconesi,
 * and P. Raghavan, “On compressing social networks,” in Proceedings of the
 * 15th ACM SIGKDD International Conference on Knowledge Discovery and Data Mining, 2009, pp. 219–228.
 * 
 * Compression Format
 * 1. CopyList....Degree(Elias) + log(w) + CopyBits
 * 2. extraNodes...SignBits(0 or 1), N1-V, N2-N1, N3-N2 (we follow Shingle paper)
 * For de-compressing, prototype_ID = ref+1
 *******************************************************************************/

package BackLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


class ShingleOrder{
	
	ArrayList<Vertex>  shinAL = new ArrayList<Vertex>();
	
	public void shingleOrdering(ArrayList<Vertex> orgAL){
		
		long startTime=0, endTime=0, totalTime=0;
		startTime = System.currentTimeMillis();
		
		shinAL = getSortedNeighbors(orgAL); //sort neighbors
		
		for(int i=0; i<shinAL.size(); i++){ //assign shingles
			shinAL.get(i).degree   = shinAL.get(i).NeighborList.size();
			shinAL.get(i).shingle1 = shinAL.get(i).NeighborList.get(0).neighborID;
			if(shinAL.get(i).NeighborList.size()>1)
				shinAL.get(i).shingle2 = shinAL.get(i).NeighborList.get(1).neighborID;
		}
		getSortedShingles2();
		reLabel(); //in Shingle Order
		getReferences();
		getCopyListing();
		getExtraNodes();
			
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		totalTime = totalTime/1000;
		System.out.println("Total Time (sec) = "+totalTime);
		
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
		
		/*OutputFile out = new OutputFile();
		out.openFile(out.outPutPath + "ShinAL"+".txt");
		out.saveAL(shinAL);
		out.closeFile();*/
		
	}
	/*************************************************************************
	 * Relabel in Shingle order so we can identify a node with its position
	 * in the ordering. We will not encode node IDs.
	 ************************************************************************/
	public void reLabel(){
		
		HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();		
		int newID=1;
		
		System.out.println("Re-Labelling in Shingle Order....");
				
		for(int i=0; i<shinAL.size(); i++){			
				hmap.put(shinAL.get(i).vertexID, newID++); //<Key, Value>				
		} 
		
		//Now re-label the whole graph with saved <key, value> pair		
		for(int j=0; j<shinAL.size(); j++){
		       if(shinAL.get(j).visited == false){
					shinAL.get(j).vertexID = hmap.get(shinAL.get(j).vertexID); //returns value of key
					shinAL.get(j).visited  = true;
				}
				for(int k=0; k<shinAL.get(j).NeighborList.size(); k++){
					if(shinAL.get(j).NeighborList.get(k).visited == false){					
						shinAL.get(j).NeighborList.get(k).neighborID = hmap.get(shinAL.get(j).NeighborList.get(k).neighborID);
						shinAL.get(j).NeighborList.get(k).visited    = true;
				}
			}
		}
		
		//sort neighbors 
		getSortedNeighbors(shinAL);
				
	}
	/**************************************************************
	 * Get References in a window, check if it helps in compression
	 * Here ref means the index of the prototype node
	 * When de-compressing we can find the prototype_nodeID = ref+1
	 ************************************************************/
	public void getReferences(){
		
		int ref= 0, comFr=0, maxCF=0;
		ArrayList<Neighbor> vList = new ArrayList<Neighbor>(); //node to be encoded
		ArrayList<Neighbor> uList = new ArrayList<Neighbor>(); //prototype node
		int window = 1;
		System.out.println("Getting Refrences...");
		shinAL.get(0).ref = -1; //Ref=-1 for first node, 
		for(int i=1; i<shinAL.size(); i++){
			vList = (ArrayList<Neighbor>) shinAL.get(i).NeighborList.clone();
			if(i>7) window = 8; //Window size as recommended in the original paper
			comFr=0; maxCF=0; ref = 0;
			for(int j=i-window; j<=i-1; j++){
				uList = (ArrayList<Neighbor>) shinAL.get(j).NeighborList.clone();
				comFr = comFriends(vList, uList);
				if(comFr >= maxCF){
					maxCF = comFr;
					ref   = j;
				}				
			}
			uList.clear();
			uList = (ArrayList<Neighbor>) shinAL.get(ref).NeighborList.clone();			
			maxCF = checkItExtraNodes(vList, uList); //check if it suits			
			if(maxCF == 0) shinAL.get(i).ref= -1;
			else 		   shinAL.get(i).ref = ref;//we encode it with lg(w)=3 bits. This is for convenience to create extraNodes
		}		
		
	}
	
	public int comFriends(ArrayList<Neighbor> vList, ArrayList<Neighbor> uList){
		
		int cFr = 0;
		for(int i=0; i<vList.size(); i++){
			for(int j=0; j<uList.size(); j++){
				if(vList.get(i).neighborID == uList.get(j).neighborID){
					cFr++;
					break;
				}
			}
		}
		return cFr;
	}

	public int checkItExtraNodes(ArrayList<Neighbor> vList, ArrayList<Neighbor> uList){
		
		int sum=0, cval=0;
		int vListElias = 0, current=0, last=0,val=0, sumE=0;
		Collections.sort(vList, new Comparator<Neighbor>() {
		    @Override
		    public int compare(Neighbor object1, Neighbor object2) {			     
		    	if      (object1.neighborID > object2.neighborID) return 1;
		    	else if (object1.neighborID < object2.neighborID) return -1;
		    	else return 0;
		    }
		});	
		Collections.sort(uList, new Comparator<Neighbor>() {
		    @Override
		    public int compare(Neighbor object1, Neighbor object2) {			     
		    	if      (object1.neighborID > object2.neighborID) return 1;
		    	else if (object1.neighborID < object2.neighborID) return -1;
		    	else return 0;
		    }
		});
		//encoding V without reference
		for(int i=0; i<vList.size(); i++){
			current = vList.get(i).neighborID;
			val     = current-last;
			cval    =  (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval    = 2*cval + 1;
			sumE    = sumE + cval;
			last    = current;
		}
		//Encoding with reference
		for(int i=0; i<uList.size(); i++){
			for(int j=0; j<vList.size(); j++){
				if(vList.get(j).neighborID == uList.get(i).neighborID){
					vList.remove(j);
					break;
				}
			}
		}
		current=0; last=0;
		for(int i=0; i<vList.size(); i++){
			current = vList.get(i).neighborID;
			val     = current-last;
			cval    =  (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval    = 2*cval + 1;
			sum     = sum + cval;
			last    = current;
		}
		sum = sum + uList.size(); //also add copyBits
		if(sum < sumE) return 1; //use reference
		else return 0; //Do not use reference, absolute encoding
	}

	/********************************************************************
	 * Format is
	 * Degree(V).....Reference(u-v).....copyBits
	 * Degree(V)--------Elias Encoded
	 * Reference(u-v)----log(w)=3  bits
	 * copyBits----------FlagBits
	 *******************************************************************/
	public void getCopyListing(){
		
		String copy = new String("");
		int ref = 0;
		boolean exist = false;
		System.out.println("Creating Copy List...");
		
		ArrayList<String> copyL   = new ArrayList<String>();		
		ArrayList<Neighbor> vList = new ArrayList<Neighbor>();
		ArrayList<Neighbor> uList = new ArrayList<Neighbor>();
		
		copyL.add(new String("")); // for first node
		
		for(int i=1; i<shinAL.size(); i++){
			vList = (ArrayList<Neighbor>) shinAL.get(i).NeighborList.clone();
			ref   = shinAL.get(i).ref;
			if(ref==-1){
				copyL.add(new String(""));
				copy = "";
			}
			else{
				uList = (ArrayList<Neighbor>) shinAL.get(ref).NeighborList.clone();
				for(int j=0; j< uList.size(); j++){
					exist = false;
					for(int k=0; k<vList.size(); k++){
						if(uList.get(j).neighborID == vList.get(k).neighborID){
							exist = true;
							break;
						}
					}
					if(exist == true) copy = copy + "1";
					else              copy = copy + "0";
				}
				copyL.add(new String(copy));
				copy = "";	
			}
			
		}
		int sum=0, cval;
		//calculate Degree, log(w) and copy Bits
		for(int i=0; i<copyL.size(); i++){
			sum = sum + copyL.get(i).length()+ 3 ; //3 is for log(w) bits for reference to u node			
		}
		for(int i=0; i<shinAL.size(); i++){
			cval = (int) java.lang.Math.floor(java.lang.Math.log(shinAL.get(i).degree)/java.lang.Math.log(2));
			cval = 2*cval + 1; //Elias Code
			sum = sum + cval;
		}
		System.out.println("Copy List Bits: "+sum);
	}
	/******************************************************************
	 * Get list of extraNodes, format is
	 * SignBit......gapEncoding is V1-V, V2-V1, V3-V2
	 *****************************************************************/
	public void getExtraNodes(){
		
		ArrayList<Vertex> extraN = new ArrayList<Vertex>();
		extraN = (ArrayList<Vertex>) ReadGraph.cloneAL(shinAL);
		
		ArrayList<Neighbor> vList = new ArrayList<Neighbor>();
		ArrayList<Neighbor> uList = new ArrayList<Neighbor>();
		Neighbor ng;
		int ref = 0;
		System.out.println("Creating Extra Nodes...");
		for(int i=1; i<extraN.size(); i++){
			vList = (ArrayList<Neighbor>) shinAL.get(i).NeighborList.clone();
			ref   = shinAL.get(i).ref;
			if(ref==-1) continue;
			uList = (ArrayList<Neighbor>) shinAL.get(ref).NeighborList.clone();
			for(int j=0; j< uList.size(); j++){				
				for(int k=0; k<vList.size(); k++){
					if(uList.get(j).neighborID == vList.get(k).neighborID){
						extraN.get(i).NeighborList.set(k, new Neighbor(0));
						break;
					}
				}				
			}
		}
		
	for(int i=0; i<extraN.size(); i++){
		for(int j=0; j<extraN.get(i).NeighborList.size(); j++){
			if(extraN.get(i).NeighborList.get(j).neighborID == 0){
				extraN.get(i).NeighborList.remove(j);
				j--;
			}
		}
	}
	 	 
	 gapEncoding(extraN);
		
	}
	
	public void gapEncoding(ArrayList <Vertex> compAL){
		
		System.out.println("Gap Encoding of extraNodes...");
		
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
			current=0; 
			last=compAL.get(i).vertexID; 
			if(compAL.get(i).NeighborList.size()>0 && compAL.get(i).vertexID > compAL.get(i).NeighborList.get(0).neighborID)
				compAL.get(i).sBit = 1; //when de-compressing we will do V-V1_delta else V+V1_delta
			for(int j=0; j<compAL.get(i).NeighborList.size(); j++){
				current = compAL.get(i).NeighborList.get(j).neighborID;
				compAL.get(i).NeighborList.get(j).neighborID = java.lang.Math.abs(current-last);
				last = current;
			}
		}
							
		Utility u = new Utility();
		u.EliasCodeEN(compAL);
		
		System.out.println("Total Bits required is the sum of CopyList Bits and extraNodes Bits:");
				
	}
}//eof