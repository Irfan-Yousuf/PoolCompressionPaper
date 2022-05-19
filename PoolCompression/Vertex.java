package PoolCompression;

import java.util.ArrayList;

public class Vertex{

	   int vertexID;	
	   int shingle1;
	   int shingle2;	   
	   int degree;
	   int blockID;
	  
	   public ArrayList <Neighbor> NeighborList;
	   
	   public Vertex(int ID){
		   
		   vertexID  = ID;	
		   shingle1  = 0;
		   shingle2  = 0;
		   degree    = 0;
		   blockID   = 0;
		 
		   NeighborList = new ArrayList<Neighbor>();		   
	   }
}