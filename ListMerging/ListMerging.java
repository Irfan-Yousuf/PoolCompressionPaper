/**************************************************************************************
 * This class implements the List Merging compression scheme as described in the paper:
 * S. Grabowski andW. Bieniecki, “Tight and simple web graph compression
 * for forward and reverse neighbor queries,” Discrete Applied Mathematics,
 * vol. 163, pp. 298–306, 2014.
 * 
 * It creates a long list by merging adjacency lists within a given window size and then
 * creates flag list necessary to reconstruct the original graph. 
 * 
 * Those two sequences, the compacted long list and the (raw) flag sequence,
 * are concatenated and compressed with the Deflate algorithm.
 *************************************************************************************/

package ListMerging;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;



class ListMerging{
	
	ArrayList<Vertex>  LM         = new ArrayList<Vertex>();
	ArrayList<VertexLong> flagL   = new ArrayList<VertexLong>();
	
	public void startProc(ArrayList<Vertex> orgAL){
		
		long startTime=0, endTime=0, totalTime=0;
		startTime = System.currentTimeMillis();
		
		/* Un-comment the two lines if you want to order the nodes as per Shingle Node 
		 * Ordering before applying List Merging  */
		
	//	ShingleOrdering shOrder = new ShingleOrdering(); //Shingle Node Order
	//	orgAL = shOrder.startProcess(orgAL);
			
		mergeLists(orgAL,64); //input graph and window size
		
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		totalTime = totalTime/1000;
		System.out.println("Total Time (sec) = "+totalTime);
		
	}
	
	
	public void mergeLists(ArrayList<Vertex> orgList, int h){
		ArrayList<Vertex>  ML     = new ArrayList<Vertex>();
		ArrayList<Integer> nb     = new ArrayList<Integer>();
		ArrayList<Vertex> blockL  = new ArrayList<Vertex>();
		Set<Integer> nbSet        = new HashSet();
		int block = 1, index=0;
		int countL = 0, countF=0;;
		
		System.out.println("Merging Lists with window size = "+h);
		
		for(int i=0; i<orgList.size(); i++ ){
			for(int j=0; j<h; j++){
				blockL.add(new Vertex(orgList.get(index).vertexID));
				for(int k=0; k<orgList.get(index).NeighborList.size(); k++){
					nbSet.add(orgList.get(index).NeighborList.get(k).neighborID);
					blockL.get(blockL.size()-1).NeighborList.add(new Neighbor(orgList.get(index).NeighborList.get(k).neighborID));
				}
				index++;
				if(index==(orgList.size())) break;
			}
			nb = new ArrayList<Integer>(nbSet);
			countF = countF + getFlags(blockL, nb, h, block); // save Flag Sequence
			ML.add(new Vertex(block)); block++;
			for(int g=0; g<nb.size(); g++){
				ML.get(ML.size()-1).NeighborList.add(new Neighbor(nb.get(g).intValue()));
				countL++;
			}
			nb.clear();nbSet.clear(); blockL.clear();
			if(index==(orgList.size())) break;
		}
		
		differentialEncoding(ML); //sort and diffencode		
				
		System.out.println("Total Blocks = "+ML.size()+" "+flagL.size());
		System.out.println("Total Enteries =  "+countL+" "+countF);
		
		ApplyDeflate def = new ApplyDeflate();
		def.deflate(ML, flagL);
	
	}
	
		
	public int getFlags(ArrayList<Vertex> orgList, ArrayList<Integer> mrgList, int h, int block){
				
		char flag[]              = new char[h];
		int num=0;
		long numLong=0;
		String oneFlag = null;
		flagL.add(new VertexLong(block));
		//sort long list
		Collections.sort(mrgList, new Comparator<Integer>() {
		    @Override
		    public int compare(Integer object1, Integer object2) {			     
		    	if      (object1.intValue() > object2.intValue()) return 1;
		    	else if (object1.intValue() < object2.intValue()) return -1;
		    	else return 0;
		    }
		});
		
		for(int j=0; j<mrgList.size(); j++){
			for(int f=0; f<h; f++){
				flag[f] = '0';				
			}
			num = mrgList.get(j).intValue();
			for(int k=0; k<orgList.size(); k++){
				for(int l=0; l<orgList.get(k).NeighborList.size(); l++){
					if(num == orgList.get(k).NeighborList.get(l).neighborID){
						flag[k] = '1';
						break;
					}
				}
			}	
			oneFlag = new String(flag);
			numLong = Long.parseUnsignedLong(oneFlag,2);
			flagL.get(flagL.size()-1).NeighborList.add(new NeighborLong(numLong));
		}
		return flagL.get(flagL.size()-1).NeighborList.size();
	}
	
	public void differentialEncoding(ArrayList <Vertex> compAL){
		
		System.out.println("Differential Encoding of Data.....");
		
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
											
	}
} //eof