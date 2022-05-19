package ListMerging;

import java.util.ArrayList;

public class Vertex{

	   int vertexID;	
	   int bNum; //block number
	   boolean visited;
	   public ArrayList <Neighbor> NeighborList;
	   
	   int shingle1;
	   int shingle2;
	   
	   public Vertex(int ID){
		   
		   vertexID = ID;	
		   bNum = 0;
		   visited  = false;
		   NeighborList = new ArrayList<Neighbor>();	
		   shingle1=0;
		   shingle2=0;
	   }
}