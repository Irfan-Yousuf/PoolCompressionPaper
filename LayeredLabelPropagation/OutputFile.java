package LayeredLabelPropagation;

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
	 ****************************************/
	public void saveAL(ArrayList <Node> data){
		  Object obj=null;	
		  try{						
			  for(int i=0; i<data.size(); i++){
				  obj = data.get(i).getId();			  
				  for(int j=0; j< data.get(i).neighbors.size(); j++){
					  obj = obj + " " + data.get(i).neighbors.get(j).intValue();
				  }
				  outPutFile.append(obj.toString()+" ");
				  outPutFile.newLine();
			  }
			 }catch(IOException e){
				 System.out.println("Can not Write to File:");
				 e.printStackTrace();
			 }
	  }
	
	
	public void saveCommunityData(ArrayList<Node> nodeList) {
				
		Object obj = null;	
		try{						
			  for(int i=1; i<nodeList.size(); i++){
				  obj = nodeList.get(i).id+" "+nodeList.get(i).label;
				  outPutFile.append(obj.toString());
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
