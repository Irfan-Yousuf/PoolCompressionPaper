/***************************************************************************
 * For reading input graph and writing the outputs to a .txt file
 * Input graph should be in Adjacency List format
 * Node_ID    Neighbor1_ID    Neighbor2_ID     Neighbor3_ID
 **************************************************************************/


package PoolCompression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


class OutputFile{
	
	 BufferedWriter outPutFile = null;

	 String inPutPath;
	 String outPutPath;
	
	
	public OutputFile(){
		
		 inPutPath =  "D:\\InputData\\" + TestClass.currentDS +"\\";
		 outPutPath = "D:\\InputData\\" + TestClass.currentDS +"\\";
		 
	}
	
	public void openFile(String name){
		
		
		  try {
				outPutFile = new BufferedWriter(new FileWriter(name));
			  } catch (IOException e) {			
				e.printStackTrace();
			  }
	  }
	 
	
	
	/**********************************************************
	 * Save the Pool Data as adjacency list
	 * Pattern is:  blockID     pool_size      pool_members
	 * @param data
	 *********************************************************/
	public void savePoolData(ArrayList <Vertex> data){
		  Object obj=null;	
		  try{						
			  for(int i=0; i<data.size(); i++){				  
				  obj = data.get(i).vertexID+" "+data.get(i).NeighborList.size();
				  for(int j=0; j< data.get(i).NeighborList.size(); j++){
					  obj = obj + " " + data.get(i).NeighborList.get(j).neighborID;
				  }
				  outPutFile.append(obj.toString()+" ");
				  outPutFile.newLine();
			  }
			 }catch(IOException e){
				 System.out.println("Can not Write to File:");
				 e.printStackTrace();
			 }
	  }
	/**********************************************************
	 * Save the Position Data as adjacency list
	 * Pattern is:  nodeID      node_degree        neigbor_list
	 * @param data
	 *********************************************************/
	public void savePositionData(ArrayList <Vertex> data){
		  Object obj=null;	
		  try{						
			  for(int i=0; i<data.size(); i++){	
				  obj = data.get(i).vertexID +" "+data.get(i).degree;
				  for(int j=0; j< data.get(i).NeighborList.size(); j++){
					  obj = obj + " " + data.get(i).NeighborList.get(j).neighborID;
				  }
				  outPutFile.append(obj.toString()+" ");
				  outPutFile.newLine();
			  }
			 }catch(IOException e){
				 System.out.println("Can not Write to File:");
				 e.printStackTrace();
			 }
	  }
	
	/**********************************************************
	 * Save the graph as adjacency list
	 * Pattern is:  nodeID     node_degree      neigbor_list
	 * @param data
	 *********************************************************/
	public void saveData(ArrayList <Vertex> data){
		  Object obj=null;	
		  try{						
			  for(int i=0; i<data.size(); i++){				  
				  obj = data.get(i).vertexID+" "+data.get(i).NeighborList.size();
				  for(int j=0; j< data.get(i).NeighborList.size(); j++){
					  obj = obj + " " + data.get(i).NeighborList.get(j).neighborID;
				  }
				  outPutFile.append(obj.toString()+" ");
				  outPutFile.newLine();
			  }
			 }catch(IOException e){
				 System.out.println("Can not Write to File:");
				 e.printStackTrace();
			 }
	  }
	  public void closeFile(){
		  
		  try {
				outPutFile.close();
			} catch (IOException e) {			
				e.printStackTrace();
			} 
		  
	  }
}
