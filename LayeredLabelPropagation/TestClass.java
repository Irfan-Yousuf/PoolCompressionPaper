/*************************************************************************
 * Implementation of Layered Label Propagation compression scheme
 * See the following paper for details:
 * P. Boldi, M. Rosa, M. Santini, and S. Vigna, “Layered label propagation:
 * A multiresolution coordinate-free ordering for compressing social
 * networks,” in Proceedings of the 20th International Conference on World
 * Wide Web, 2011, pp. 587–596.
 *******************************************************************************/
package LayeredLabelPropagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class TestClass{
	
	  public static String[] DS_Name = { "Gowalla","WordNet", "DBLP", "Amazon", "Digg", "YouTube", "Hyves", "Flixster", "Skitter", "Flicker"};
	  public static int[]    DS_Size = {196592,146006,317081,334864,770800,1134891,1402674,2523387,1696416,1715256};	 
	  public static String currentDS;
	  
	  public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
			LabelPropagation lp        = new LabelPropagation();
			ArrayList<Node> myNodeList = null;
			int numNodes=0, numThreads=16;
			long startTime=0, endTime=0, totalTime=0;
			String Path = "D:\\InputData\\";
			String inPath = "";
			String outPath = "";
			for(int i=0; i<1; i++){
				numNodes = DS_Size[i]; //Number of nodes in the network
				currentDS = DS_Name[i];
				System.out.println(">>>>>>>>> Reading Data: "+DS_Name[i]+" >>>>>>>>>>>>>>");
				inPath = Path.toString()+DS_Name[i]+"//"+DS_Name[i]+"_Graph.txt";
				lp.readEdges(numNodes, inPath);
				myNodeList = lp.nodeList;
				double gamma[] = {1.0,0.5,0.25,0.125,0.0625,0.03125,0.015625,0.0078125,0.00390625,0.001953125 };				
				for(int g=0; g<gamma.length; g++){					
					System.out.println(">>>>>>>>>>>>>> gamma="+ gamma[g]+"<<<<<<<<<<<<<");
					startTime = System.currentTimeMillis();
					myNodeList = lp.findCommunities(numThreads, gamma[g], myNodeList); 				
					outPath = Path.toString()+DS_Name[i]+"//"+"SortMemb.txt";
					myNodeList =  lp.writeMembershipsSmart(myNodeList);				
				
					System.out.println("---Start Compressing---");				
					LLPCompression cLLP = new LLPCompression(myNodeList);
					myNodeList = cLLP.compress();
					
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					totalTime = totalTime / 1000;
					System.out.println("Total Time (sec) = "+totalTime); 
					myNodeList.add(new Node(0,0));
					myNodeList.get(myNodeList.size()-1).addNeighbor(0);
				}
				System.out.println("Done for this set...."+ currentDS);
			}
			System.out.println("All Done...");
		}
}