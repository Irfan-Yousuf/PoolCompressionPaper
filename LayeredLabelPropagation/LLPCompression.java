/*******************************************************************************************
 * Implementation of Layered Label Propagation compression method as described in the paper:
 * P. Boldi, M. Rosa, M. Santini, and S. Vigna, “Layered label propagation:
 * A multiresolution coordinate-free ordering for compressing social
 * networks,” in Proceedings of the 20th International Conference on World Wide Web, 2011, pp. 587–596.
 * 
 * Implemented in a multi-threaded environment to speed-up the detection of communities.
 * It detect communities at different levels of resolution (i.e., gamma values)
 * and then compresses the graph with the best resolution. 
 * 
 ******************************************************************************************************/
package LayeredLabelPropagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class LLPCompression{
	
	ArrayList<Node>  clusAL = new ArrayList<Node>();
	
	public LLPCompression(){}
	public LLPCompression(ArrayList<Node> orgAL){
		clusAL = orgAL;
	}
	public ArrayList<Node> compress(){
		
				
		getSortedNeighbors();
		getSortedClusters();
		getReferences();
		getCopyListing();
		getExtraNodes();
		
		return clusAL;
		
	}
	
	public  void getSortedNeighbors(){
		
		for(int i=0; i< clusAL.size(); i++){	
			Collections.sort(clusAL.get(i).neighbors, new Comparator<Integer>() {
			    @Override
			    public int compare(Integer object1, Integer object2) {			     
			    	if (object1.intValue() > object2.intValue()) return 1;
			    	else if (object1.intValue()< object2.intValue()) return -1;
			    	else return 0;
			    }
			});	
		}
		
	
		}
	
	public  void getSortedClusters(){
		
		Collections.sort(clusAL, new Comparator<Node>() {
		    @Override
		    public int compare(Node object1, Node object2) {			     
			   	if (object1.label < object2.label) return 1;
			   	else if (object1.label == object2.label && object1.order <= object2.order) return -1;
			   	else return 0;
			 }
		});	
				
		
		}
	
public void getReferences(){
		
		int ref= 0, comFr=0, maxCF=0;
		ArrayList<Integer> vList = new ArrayList<Integer>();
		ArrayList<Integer> uList = new ArrayList<Integer>();
		clusAL.remove(0);
		int window = 1;
	
		clusAL.get(0).Ref = 0; //Ref=0 for first node, shingle value here mean Ref
		for(int i=1; i<clusAL.size(); i++){
			vList = (ArrayList<Integer>) clusAL.get(i).neighbors.clone();
			
			comFr=0; maxCF=0; ref = 0;
			for(int j=i-window; j<=i-1; j++){
				uList = (ArrayList<Integer>) clusAL.get(j).neighbors.clone();
				comFr = comFriends(vList, uList);
				if(comFr >= maxCF){
					maxCF = comFr;
					ref   = j;
				}				
			}
		
			clusAL.get(i).Ref = ref;
		}
	}
	
	public int comFriends(ArrayList<Integer> vList, ArrayList<Integer> uList){
		
		int cFr = 0;
		for(int i=0; i<vList.size(); i++){
			for(int j=0; j<uList.size(); j++){
				if(vList.get(i).intValue() == uList.get(j).intValue()){
					cFr++;
					break;
				}
			}
		}
		return cFr;
	}
	
	public void getCopyListing(){
		
		String copy = new String();
		int ref = 0;
		boolean exist = false;
	
		
		ArrayList<String> copyL   = new ArrayList<String>();
		
		ArrayList<Integer> vList = new ArrayList<Integer>();
		ArrayList<Integer> uList = new ArrayList<Integer>();
		
		copyL.add(new String("0")); // for first node
		
		for(int i=1; i<clusAL.size(); i++){
			vList = (ArrayList<Integer>) clusAL.get(i).neighbors.clone();
			ref   = clusAL.get(i).Ref;
			uList = (ArrayList<Integer>) clusAL.get(ref).neighbors.clone();		
			for(int j=0; j< uList.size(); j++){
				exist = false;
				for(int k=0; k<vList.size(); k++){
					if(uList.get(j).intValue() == vList.get(k).intValue()){
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
		int sum=0;
		for(int i=1; i<copyL.size(); i++){
			sum = sum + copyL.get(i).length();
		}
		System.out.println("Copy List Bits: "+sum);
		
	}
	
	public void getExtraNodes(){
		
		
		
		ArrayList<Node> extraN = new ArrayList<Node>();
		for(int y=0; y<clusAL.size(); y++){
			extraN.add(new Node(clusAL.get(y).id, clusAL.get(y).label));
			extraN.get(y).Ref = clusAL.get(y).Ref;
			for(int z=0; z<clusAL.get(y).neighbors.size(); z++){
				extraN.get(y).addNeighbor(clusAL.get(y).neighbors.get(z).intValue());
			}
		}
		
		ArrayList<Integer> vList = new ArrayList<Integer>();
		ArrayList<Integer> uList = new ArrayList<Integer>();
		int ref = 0;
	
		for(int i=1; i<extraN.size(); i++){
			vList = (ArrayList<Integer>) clusAL.get(i).neighbors;
			ref   = clusAL.get(i).Ref;
			uList = (ArrayList<Integer>) clusAL.get(ref).neighbors;
			
			for(int j=0; j< uList.size(); j++){				
				for(int k=0; k<vList.size(); k++){
					if(uList.get(j).intValue() == vList.get(k).intValue()){
						extraN.get(i).neighbors.set(k, new Integer(0));
						break;
					}
				}				
			}
		}
		
	for(int i=0; i<extraN.size(); i++){
		for(int j=0; j<extraN.get(i).neighbors.size(); j++){
			if(extraN.get(i).neighbors.get(j).intValue() == 0){
				extraN.get(i).neighbors.remove(j);
				j--;
			}
		}
	}
	
	 	 
	 gapEncoding(extraN);
		
	}
	
	public void gapEncoding(ArrayList <Node> compAL){
		
	
		int last = 0, current=0;
		for(int i=0; i<compAL.size(); i++){
			current=0; last=0;
			for(int j=0; j<compAL.get(i).neighbors.size(); j++){
				current = compAL.get(i).neighbors.get(j).intValue();			
				compAL.get(i).neighbors.set(j, current-last);
				last = current;
			}
		}
				
				
		Utility u = new Utility();
		u.EliasCode(compAL);
		
		System.out.println("Total Bits required is the sum of CopyList Bits and extraNodes Bits:");
				
	}
}