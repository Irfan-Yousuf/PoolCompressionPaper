package SlashBurn;

import java.util.ArrayList;

public class TestClass{
	
	  public static String[] DS_Name = { "Example", "WordNet", "Skitter", "Amazon" ,"DBLP" ,"Gowalla","Flicker",  "Digg" ,  "BrightKite", "FourSquare","Twitter", "Flixster","Facebook", "Lastfm" ,  "YouTube",  "Hyves" };
	  public static String currentDS;
	  
	   public static void main(String[]args) throws java.io.IOException{		   
		   for(int i=1; i<2; i++){
			   currentDS = DS_Name[i];
			   comp(currentDS);
			   
		   }		
		   System.out.println("Done..."); 
	   }
	   
	   public static void comp(String DS){
		   
		    ReadGraph myGraph = new ReadGraph();
		    Utility u = new Utility();		  		  
		  	ConnectedComp CC = new ConnectedComp();
		  	
//			myGraph.ReadAL(DS + "_Graph.txt");
//			System.out.println(">>>>>>>>>>>> "+ DS+ " >>>>>>>>>>>>>>>>");			
//			CC.startPoc(myGraph.orgAL);
		  	
		  	myGraph.orgAL.clear();
		  	System.out.println(">>>>>>>>>>>> "+ DS+ " Ordering >>>>>>>>>>>>>>>>");	
		  	myGraph.ReadAL(DS +"_Graph.txt");
		  	myGraph.ReadOrder(DS + "_SB_Order.txt");
		  	
		  	CreateAdjMatrix mat = new CreateAdjMatrix();
		  	mat.startPoc(myGraph.orgAL, myGraph.orgOrder);
		  		
	   }
}