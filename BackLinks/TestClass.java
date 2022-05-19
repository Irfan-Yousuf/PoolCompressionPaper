/*********************************************************************************************
 * Exact Implementation of BL Compression as given in Shingle Paper
 * See the following paper for details
 * F. Chierichetti, R. Kumar, S. Lattanzi, M. Mitzenmacher, A. Panconesi,
 * and P. Raghavan, “On compressing social networks,” in Proceedings of the
 * 15th ACM SIGKDD International Conference on Knowledge Discovery and Data Mining, 2009, pp. 219–228
 ****************************************************************************************************/


package BackLinks;

import java.util.ArrayList;

public class TestClass{
	
	  public static String[] DS_Name = {"Example", "WordNet", "Skitter", "Amazon" ,"DBLP" ,"Gowalla","Flicker",  "Digg" , "Facebook", "BrightKite", "FourSquare","Lastfm" ,  "Flixster", "YouTube",  "Hyves", "Twitter" };
	  public static String currentDS;
	  
	   public static void main(String[]args) throws java.io.IOException{		   
		   for(int i=5; i<6; i++){
			   currentDS = DS_Name[i];
			   comp(currentDS);
			   
		   }		
		   System.out.println("Done..."); 
	   }
	   
	   public static void comp(String DS){
		   
		    ArrayList <Vertex> rGraph = new ArrayList<Vertex>();
		    ReadGraph myGraph = new ReadGraph();
		  	ShingleOrder S = new ShingleOrder();
		  	
		  	
			rGraph = myGraph.ReadAL(DS +"_Graph.txt");
			S.shingleOrdering(rGraph);
			
		
	   }
}