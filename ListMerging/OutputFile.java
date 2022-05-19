package ListMerging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



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
	 
	
	
	/****************************************
	 * Save a graph as Adjacency List
	 * @param data
	 ***************************************/
	public void saveAL(ArrayList <Vertex> data){
		  Object obj=null;	
		  try{						
			  for(int i=0; i<data.size(); i++){
				  obj = data.get(i).vertexID;				  
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
