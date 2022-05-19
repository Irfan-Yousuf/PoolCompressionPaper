package SlashBurn;

import java.util.ArrayList;

public class Vertex{

	   int vertexID;
	   int hubID;
	   boolean visited;
	   boolean removed;
	   int compNum;
	   int compSize;
	   int order=0;
	   public ArrayList <Neighbor> NeighborList;
	   
	   public Vertex(int ID){		   
		   vertexID = ID;	
		   hubID = 0;
		   visited  = false;
		   removed = false;
		   compNum  = 0;
		   compSize=0;
		   order = 0;
		   NeighborList = new ArrayList<Neighbor>();		   
	   }
}