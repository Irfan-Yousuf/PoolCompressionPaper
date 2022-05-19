package LayeredLabelPropagation;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LabelPropagation {
	
	public ArrayList<Node> nodeList;
	public ArrayList<Integer> nodeOrder;
	
	public LabelPropagation() {
		
	}	
		
	public void readEdges(int numNodes, String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		nodeList = new ArrayList<Node>();				
		String line = br.readLine();
		String s[] = {"0", "0"};
		this.makeAL(s, 0);
		int lab = 1;
		while (line!=null) {
			String[] parts = line.split("\\s+");			
			if(line!=null) this.makeAL(parts, lab++);		
			line=br.readLine();
		}
		
		System.out.println("Graph Size:  "+nodeList.size());
		
		nodeOrder = new ArrayList<Integer>(nodeList.size());
		for (int i=0; i<numNodes; i++) {			
			nodeOrder.add(Integer.valueOf(i));
			nodeList.get(i).order = i;
		}		
		br.close();
	}
	
	public void makeAL(String tokens[], int lab){
		
		this.nodeList.add(new Node(Integer.parseInt(tokens[0]),lab)); //Add vertex
		int index = this.nodeList.size()-1;		
		for(int i=1; i<tokens.length; i++){
			this.nodeList.get(index).neighbors.add((Integer.parseInt(tokens[i])));
		}
	}

	/************************************************************************************
	 * write Membership with community membership labels are renumbered to be sequential 
	 * from 1 to the number of communities prior to writing the output to file.
	 *  
	 * @param file filename to write output to (existing file will be overwritten)
	 * @throws IOException
	 **********************************************************************************/
	public ArrayList<Node> writeMembershipsSmart(ArrayList<Node> nodeList) throws IOException {
			
		Map<Integer,Integer> labelMap = new HashMap<Integer,Integer>();
		int labelCount=0;
		for (int i=0; i<nodeList.size(); i++) {
			int label = nodeList.get(i).getLabel();
			Integer val =  labelMap.get(Integer.valueOf(label));
			if (val==null) {
				labelCount++;
				labelMap.put(Integer.valueOf(label), Integer.valueOf(labelCount));
			}
		}
		System.out.println("Found " + labelCount + " communities.");
				
		ArrayList<Node> nodeListCopy = new ArrayList<Node>();
		for(int y=0; y<nodeList.size(); y++){
			nodeListCopy.add(new Node(nodeList.get(y).id,nodeList.get(y).label));
			nodeListCopy.get(y).order = nodeList.get(y).order;
			for(int z=0; z<nodeList.get(y).neighbors.size(); z++){
				nodeListCopy.get(y).addNeighbor(nodeList.get(y).neighbors.get(z).intValue());
			}
		}
		return (ArrayList<Node>) nodeListCopy;
	}
	
	/***************************************************************************
	 * Start the community detection process.
	 * 
	 * @param basepath Intermediate community memberships will be written to files in this directory after each pass.
	 * 					Specify "null" to not write intermediate membership information.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 *************************************************************************************************/
	public ArrayList<Node> findCommunities(int numThreads, double gamma, ArrayList<Node> nodeList) throws InterruptedException, ExecutionException, IOException {
		
		ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);		

		
		ArrayList<LabelPropagationWorker> workers = new ArrayList<LabelPropagationWorker>(numThreads);
		for (int j=0; j<numThreads; j++) {
			workers.add(new LabelPropagationWorker(nodeList));	
			workers.get(j).gamma = gamma;
		}
		
		int iter=0;
		int nodesChanged=1;		
		while (nodesChanged>0) {
						
			System.out.println("Running " + (++iter) + " iteration at " + System.currentTimeMillis() + ".");			
		//	Collections.shuffle(nodeOrder);//DO NOT SHUFFLE nodeList
			
			for (int i=0; i<nodeList.size(); i+=numThreads) {
				for (int j=0; j<numThreads; j++) {
					if ((j+i)<nodeList.size()) {
						workers.get(j).setNodeToProcess(nodeOrder.get(i+j).intValue());
					} else {
						workers.get(j).setNodeToProcess(-1);
					}
				}
				List<Future<Boolean>> results = threadPool.invokeAll(workers);
				nodesChanged=0;
				for (int j=0; j<results.size()-2; j++) {
					Boolean r = results.get(j).get();
					if (r!=null && r.booleanValue()==true) {
						nodesChanged++;						
						if(nodesChanged==1) break;
					}
				}			
			}
						
	  }
	  System.out.println("Detection complete!");
	  threadPool.shutdown();
	  
	  return nodeList;
	}
} // eof