/*************************************************************************************
 * TestClass.java is the entry point as it contains the main method.
 * We read a graph in the form of adjacency list.
 ************************************************************************************/

package PoolCompression;

import java.util.ArrayList;

public class TestClass{
	
	  public static String[] DS_Name = {"Example", "WordNet", "Skitter",
		                                "Amazon" ,"DBLP" ,"Gowalla",
		                                "Flicker",  "Digg" , "Facebook", 
		                                "BrightKite", "FourSquare","Lastfm" ,  
		                                "Flixster", "YouTube",  "Hyves", "Twitter" };
	  public static String currentDS;
	  
	   public static void main(String[]args) throws java.io.IOException{
		   ArrayList<Vertex> rGraph = new ArrayList<Vertex>();
		   
		    ReadGraph RG      = new ReadGraph();		  
		  	PreProcess PP     = new PreProcess();
		 // 	OrderDeltaElias ODE = new OrderDeltaElias();
		  	
		   for(int i=0; i<1; i++){
			   currentDS = DS_Name[i];
			    rGraph = RG.ReadAL(currentDS + "_Graph.txt");				
				PP.startProcess(rGraph);
			 //   ODE.NoPoolingTest(rGraph); //to check the effect of ordering, delta and elias encoding only (NO Pooling)
		   }		
		   System.out.println("Done..."); 
	   }
}