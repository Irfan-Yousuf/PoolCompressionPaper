package SlashBurn;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ReadGraph implements Cloneable{
		
	ArrayList<Vertex>   orgAL    = new ArrayList<Vertex>();
	ArrayList<Integer>  orgOrder = new ArrayList<Integer>();
	
	public ReadGraph(){	
		orgAL    = new ArrayList<Vertex>();
		
	}
	
	public Object cloneAL(){
		ArrayList<Vertex>  copy = new ArrayList<Vertex>();
		 Vertex v;
		 Neighbor nr;
		 for(int i=0; i<this.orgAL.size(); i++){
			 v = new Vertex(this.orgAL.get(i).vertexID);
			 copy.add(v);
			 for(int j=0; j<this.orgAL.get(i).NeighborList.size(); j++){
				 nr = new Neighbor(this.orgAL.get(i).NeighborList.get(j).neighborID);
				 copy.get(i).NeighborList.add(nr);
			 }
		 }		
		 return copy;		
	}
	
	public static Object cloneAL(ArrayList<Vertex> AL){
		ArrayList<Vertex>  copy = new ArrayList<Vertex>();
		for(int i=0; i<AL.size(); i++){
			 copy.add(new Vertex(AL.get(i).vertexID));
			 for(int j=0; j<AL.get(i).NeighborList.size(); j++){
				 copy.get(i).NeighborList.add(new Neighbor(AL.get(i).NeighborList.get(j).neighborID));
			 }
		 }		
		 return copy;		
	}
	
	public void ReadAL(String file){
		
		System.out.println("Reading Graph: "+file);
		OutputFile out = new OutputFile();
		   try (BufferedReader br = new BufferedReader(new FileReader(out.outPutPath+file))) {			
			    String line;
			    String delimit = "\\s+";			   
			    while ((line = br.readLine()) != null) {
			   
			      String tokens[] =  line.split(delimit);
			      if(line!=null) this.makeAL(tokens);
			    }
			} catch (Exception e) {	e.printStackTrace();} 
		   System.out.println("Graph Size: "+this.orgAL.size());
	   }
	
	public void makeAL(String tokens[]){
		
		this.orgAL.add(new Vertex(Integer.parseInt(tokens[0]))); //Add vertex
		int index = this.orgAL.size()-1;		
		for(int i=1; i<tokens.length; i++){
			this.orgAL.get(index).NeighborList.add(new Neighbor(Integer.parseInt(tokens[i])));
		}
	}
	
	public void ReadOrder(String file){
		
		System.out.println("Reading Order: "+file);
		OutputFile out = new OutputFile();
		   try (BufferedReader br = new BufferedReader(new FileReader(out.outPutPath+file))) {			
			    String line;
			    String delimit = "\\s+";			   
			    while ((line = br.readLine()) != null) {
			   
			      String tokens[] =  line.split(delimit);
			      if(line!=null) this.orgOrder.add(Integer.parseInt(tokens[0]));
			    }
			} catch (Exception e) {	e.printStackTrace();} 
		   System.out.println("Graph Size: "+this.orgAL.size());
	   }

}//end class