/****************************************************************************
 * TestClass.java is the entry point.
 * It reads the original graph and starts compressing it under List merging scheme.
 * 
 * See the following paper for details:
 * S. Grabowski andW. Bieniecki, “Tight and simple web graph compression
 * for forward and reverse neighbor queries,” Discrete Applied Mathematics,
 * vol. 163, pp. 298–306, 2014.
 ****************************************************************************/

package ListMerging;

import java.util.ArrayList;


public class TestClass{
	
	  public static String[] DS_Name = { "Example", "WordNet", "Gowalla", "DBLP" ,"Amazon" , "FourSquare", "Digg" , "Twitter",
										 "Lastfm" ,"Hyves", "Skitter", "Flicker", "Flixster", "Facebook", "YouTube", "LiveJournal" };
	  public static String currentDS;
	  
	   public static void main(String[]args) throws java.io.IOException{		   
		   for(int i=2; i<3; i++){
			   currentDS = DS_Name[i];
			   comp(currentDS);
			   
		   }		
		   System.out.println("Done..."); 
	   }
	   
	   public static void comp(String DS){
		   
		    ArrayList<Vertex> rGraph = new ArrayList<Vertex>();
		    ReadGraph myGraph = new ReadGraph();  
		  	ListMerging LM = new ListMerging();
		  	
			rGraph = myGraph.ReadAL(DS +"_Graph.txt");	
			LM.startProc(rGraph);
			
		
	   }
}