package ListMerging;

import java.util.ArrayList;

public class VertexLong{

	   long vertexID;		  
	   public ArrayList <NeighborLong> NeighborList;
	   
	   int shingle1;
	   int shingle2;
	   
	   public VertexLong(long ID){
		   
		   vertexID = ID;	
		   NeighborList = new ArrayList<NeighborLong>();	
		   shingle1=0;
		   shingle2=0;
	   }
}