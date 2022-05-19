package BackLinks;

import java.util.ArrayList;

public class Vertex{

	   int vertexID;	
	   int shingle1;
	   int shingle2;
	   int ref;
	   int degree;
	   int sBit=0;
	   boolean visited;
	   public ArrayList <Neighbor> NeighborList;
	   
	   public Vertex(int ID){
		   
		   vertexID = ID;	
		   shingle1 = 0;
		   shingle2 = 0;
		   ref=0;
		   degree=0;
		   sBit=0;
		   visited  = false;
		   NeighborList = new ArrayList<Neighbor>();		   
	   }
}